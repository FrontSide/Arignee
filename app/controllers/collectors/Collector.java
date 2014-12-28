package collectors;

/**
 * A collector is a class that is responsible for 
 * obtaining data from a website (with the help of a HTTPConnector)
 * and extracting useful data from it
 */

import java.lang.RuntimeException;
import java.util.Map;
import collectors.enums.CollectorKey;

public interface Collector {   
    
    /**
     * Defines the method that is accessed publicly.
     * Takes the url stored in the Collector object (handed through constructor)
     * @returns: a Map with Enum-Keys and List<String> Values
     *      that contains all necessary data which is later processed by
     *      an Evaluator
     */
    Map<? extends CollectorKey, CollectorValue> get();
        
    /**
      * Sets the url
      */
    Collector url(String url);
    
    /**
     * @returns: the Collectors accessed url
     */
    String url();
    
    /**
      * Triggers the HTTPRequestor,fetches the HTTPResponse and 
      * stores it in the "raw" variable
      */
    Collector fetch() throws RuntimeException;
        
    /**
      * This method needs to be implemented by Collectors that have
      * to assemble the URL to be requested themselves.
      * This is necessary when the Class that invokes the get method 
      * of the Controller does not already deliver a URL.
      */
    void buildUrl();

}
