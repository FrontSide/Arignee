package collectors;

/**
  * The Abstraction for Collectors
  * The Generic Type T is the Type that is expected to be delivered
  * by the HTTPConnector and used by the Collector for the extraction process
  */

import java.util.Map;
import java.util.List;
import java.net.MalformedURLException;
import collectors.enums.CollectorKey;
import collectors.request.*;
import models.collection.CollectorValue;
import ticketing.TicketProcessor;
import ticketing.TicketHandler;
import ticketing.TicketStatus;

import play.Logger;
import play.Logger.ALogger;

public abstract class AbstractCollector<T> implements Collector<T>, TicketProcessor {

    private static final ALogger logger = Logger.of(AbstractCollector.class);

    private final HTTPConnector CONNECTOR
                        = HTTPConnectorFactory.getInstance().create();

    private T raw;

    private String url;

    /* ------------ Impl of TicketProcessor ------------ */
    private final TicketHandler TICKETHANDLER = TicketHandler.getInstance();
    protected String ticketNumber;

    @Override
    public void setTicketNumber(String number) {
        this.ticketNumber = number;
    }

    @Override
    public void updateTicketStatus(TicketStatus status) {
        if (this.ticketNumber == null) return;
        this.TICKETHANDLER.updateStatus(this.ticketNumber, status);
    }
    /* ------------ ----------------------- ------------ */

    /**
     * Invokes the fetch method if "raw" data is not already available
     * and triggers the extract method to generate a result map
     * @return collected and extracted data from "extract()"-Method as Map
     */
    public Map<? extends CollectorKey, CollectorValue> get() {

        if (this.raw == null) {
            try {
                this.fetch();
            } catch (RuntimeException e) {
                logger.error("Fetching raw data via HTTPConnector failed!");
            }
        }
        return this.initExtract();
    }

    public Map<? extends CollectorKey, CollectorValue> initExtract() {
        return this.executeExtract();
    }
    public abstract Map<? extends CollectorKey, CollectorValue> executeExtract();

    /**
     * Uses an in the object instanziated HTTPConnector
     * and triggers and HTTP-Request
     * The response is saved in the "raw" attribute
     * @return this
     */
    public Collector fetch() throws RuntimeException {

        this.updateTicketStatus(TicketStatus.REQUESTING);

        if (this.url == null) throw new RuntimeException("No URL found!");
        T response = null;

        try {
            logger.debug("triggering HTTPConnector request...");
            response = (T) this.CONNECTOR.executeRequest(this.url);
        } catch (ClassCastException e) {
            logger.error("Type-Missmatch between Object delivered " +
                            "from HTTPConnector and Collector!");
            return this;
        } catch (MalformedURLException e) {
            this.updateTicketStatus(TicketStatus.INVALID_URL);
            return this;
        }

        if (response == null)
            throw new RuntimeException("Requesting HTTP Response for URL " +
                                                this.url + " failed");

        logger.debug("response from HTTPConnector received.");
        this.raw(response);
        return this;
    }

    public Collector url(String url) {
        this.url = url;
        return this;
    }

    public String url() {
        return this.url;
    }

    public void buildUrl() {
        throw new UnsupportedOperationException("buildUrl() not implemented");
    }

    public T raw() {
        if (this.raw == null) logger.error("No raw data in collector found!");
        return this.raw;
    }

    public Collector raw(T raw) {
        if (this.raw != null)
            throw new IllegalArgumentException("raw data is already available"
                            + "for this collector and cannot be overwritten!");
        this.raw = raw;
        return this;
    }

    /************************************************************************/


    /**
     * Checks if a URL is within a domain i.e. has the same Base-URL
     * @param  domainUrl    the base-url of the referenced domain
     * @param  URL          the url or path that is checked
     * @return              true if the passed url is considered an
     *                      internal url or merely a path
     */
    public static boolean isInternalUrl(String domainUrl, final String URL) {
        if (domainUrl.equals(URL)) return true;
        if (AbstractCollector.isUrlAppendix(URL)) return true;
        domainUrl = AbstractCollector.trimToBaseUrl(domainUrl);
        return URL.contains(domainUrl);
    }

    /**
     * Checks if a String is merely a Path, Parameter or
     * Resource Identifier (i.e. #) rather than a full URL
     * @param   s  String to check
     * @returns    true if String is a url- path,
     *             parameter or resource identifier
     */
    public static boolean isUrlAppendix(String s) {

        if (s == null || s.equals("")) {
            logger.warn("Empty-String encountered in isUrlAppendix()");
            return true;
        }

        return "#?/".contains("" + s.charAt(0));
    }

    /**
     * Removes paths, protocols and subdomains from a URL to leave just the base
     * and Top-Level Domain (e.g http://dary.info/blog/bac1 --> dary.info)
     * @param  url url to trim
     * @return     trimmed url
     */
    public static String trimToBaseUrl(String url) {
        url = removeProtocols(url);
        url = url.replace("www.", "");

        int shlashIndex = url.indexOf('/');
        if (shlashIndex == -1) return url;
        return url.substring(0, shlashIndex);
    }

    /**
     * Checks if two URLs are the same i.e. directing to the same Page
     * @param   urla
     * @param   urlb
     * @return  true if both urls point to the same Page
     */
    public static boolean pointsToSameWebpage(String urla, String urlb) {

        logger.debug("pointsToSameWebpage() :: " + urla + " :: " + urlb);

        if (urla == null || urlb == null || urla.equals("") || urlb.equals("")) return false;
        if (urla.equals(urlb)) return true;

        urla = AbstractCollector.removeProtocols(urla);
        urlb = AbstractCollector.removeProtocols(urlb);

        logger.debug("pointsToSameWebpage() no Prot :: " + urla + " :: " + urlb);

        if (urla.startsWith("www.")) urla = urla.replaceFirst("www.", "");
        if (urlb.startsWith("www.")) urlb = urlb.replaceFirst("www.", "");

        logger.debug("pointsToSameWebpage() no www :: " + urla + " :: " + urlb);

        int idx =  urla.indexOf('#');
        if (idx > 0) urla = urla.substring(0, idx);

        idx = urlb.indexOf('#');
        if (idx > 0) urlb = urlb.substring(0, idx);

        if(urla.charAt(urla.length()-1) == '/') urla = urla.substring(0, urla.length()-1);
        if(urlb.charAt(urlb.length()-1) == '/') urlb = urlb.substring(0, urlb.length()-1);

        logger.debug("pointsToSameWebpage() trimmed :: " + urla + " :: " + urlb);

        return (urla.equals(urlb) || urla.equals("/") || urlb.equals("/"));

    }

    /**
     * Removes the Protocols from a url
     * @param   url url with protocol
     * @return      url withour protocol
     */
    private static String removeProtocols(String url) {
        String[] urlarray = url.split("://");
        if (urlarray.length > 1) url = urlarray[1];
        return url;
    }

}
