package collectors;

/**
 * A collector is a class that is responsible for 
 * obtaining data from a website (with the help of a HTTPConnector)
 * and extracting useful data from it
 */

import java.lang.RuntimeException;
import java.util.Map;
import java.util.List;
import collectors.enums.Key;

public interface Collector {   
    
    /**
     * Defines the method that is accessed publicly.
     * Takes the url stored in the Collector object (handed through constructor)
     * @returns: a Map with a String Keys and List<String> Values
     *      that contains all necessary data which is later processed by
     *      an Evaluator
     */
    public Map<? extends Key, List<String>> get() throws RuntimeException;
        
    /**
      * Sets the url
      */
    public Collector url(String url);
    
    /**
     * @returns: the Collectors accessed url
     */
    public String url();
        
    /**
      * @returns: the raw data returned by the HTTPConnector as a String
      */
    public String raw();
    
    /**
      * This method needs to be implemented by Collectors that have
      * to assemble the URL to be requested themselves.
      * This is necessary when the Class that invokes the get method 
      * of the Controller does not already deliver a URL.
      */
    void buildUrl();

}
