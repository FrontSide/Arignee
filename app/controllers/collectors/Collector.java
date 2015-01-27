package collectors;

/**
 * A collector is a class that is responsible for
 * obtaining data from a website (with the help of a HTTPConnector)
 * and extracting useful data from it
 */

import java.net.URL;
import java.lang.RuntimeException;
import java.util.Map;
import collectors.enums.CollectorKey;
import models.collection.CollectorValue;

public interface Collector<T> {

    /**
     * Defines the method that is accessed publicly.
     * Takes the url stored in the Collector object (handed through constructor)
     * @return     a Map with Enum-Keys and List<String> Values
     *             that contains all necessary data which is later processed by
     *             an Evaluator
     */
    Map<? extends CollectorKey, CollectorValue> get();

    /**
     * Setter for the URL
     * @param  url  url to set
     * @return      this
     */
    Collector url(URL url);
    Collector url(String url);

    /**
     * @returns: the Collectors accessed url
     */
    URL url();

    /**
     * Setter for raw Data
     * @param  raw raw data to set
     * @return     this
     */
    Collector raw(T raw);

    /**
      * Triggers the HTTPRequestor,fetches the HTTPResponse and
      * stores it in the "raw" variable
      * @return     this
      */
    Collector fetch() throws RuntimeException;

    /**
      * This method needs to be implemented by Collectors that have
      * to assemble the URL - to be requested - themselves.
      * This is necessary when the Class that invokes the get method
      * of the Controller does not already deliver a full URL.
      */
    void buildUrl();

    /**
     * Initiates the extraction of the collector data from the raw data
     * from the fetched (raw) data returned from the HTTPConnector
     * @return     Extracted Collector-Data as Map
     */
    Map<? extends CollectorKey, CollectorValue> extract() throws RuntimeException;



}
