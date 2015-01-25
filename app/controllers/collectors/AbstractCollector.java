package collectors;

/**
  * The Abstraction for Collectors
  * The Generic Type T is the Type that is expected to be delivered
  * by the HTTPConnector and used by the Collector for the extraction process
  */

import java.util.Map;
import java.util.List;
import java.net.URL;
import java.net.MalformedURLException;

import url.URLHandler;
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

    private URL url;

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
                this.updateTicketStatus(TicketStatus.INVALID_URL);
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

    public Collector url(URL url) {
        this.url = url;
        return this;
    }

    public Collector url(String url) {
        try {
            return url(URLHandler.getInstance().create(url));
        } catch (IllegalArgumentException e) {
            logger.error("Could not create collector with url :: " + url);
        }
        return this;
    }

    public URL url() {
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

}
