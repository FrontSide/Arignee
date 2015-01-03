package collectors;

/**
  * The Abstraction for Collectors
  * The Generic Type T is the Type that is expected to be delivered
  * by the HTTPConnector and used by the Collector for the extraction process
  */

import java.util.Map;
import java.util.List;
import collectors.enums.CollectorKey;
import collectors.request.*;
import models.collection.CollectorValue;

import play.Logger;
import play.Logger.ALogger;

public abstract class AbstractCollector<T> implements Collector<T> {

    private static final ALogger logger = Logger.of(AbstractCollector.class);

    private final HTTPConnector CONNECTOR
                        = HTTPConnectorFactory.getInstance().create();

    private T raw;

    private String url;

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

        if (this.url == null) throw new RuntimeException("No URL found!");
        T response = null;

        try {
            logger.debug("triggering HTTPConnector request...");
            response = (T) this.CONNECTOR.executeRequest(this.url);
        } catch (ClassCastException e) {
            logger.error("Type-Missmatch between Object delivered " +
                            "from HTTPConnector and Collector!");
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
        if (urla.equals(urlb)) return true;
        urla = AbstractCollector.removeProtocols(urla);
        urlb = AbstractCollector.removeProtocols(urlb);

        int hashtagIndex =  urla.indexOf('#');
        if (hashtagIndex != -1) urla = urla.substring(0, hashtagIndex);

        hashtagIndex = urlb.indexOf('#');
        if (hashtagIndex != -1) urlb = urlb.substring(0, hashtagIndex);

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
