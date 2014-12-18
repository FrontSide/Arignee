package collectors;

/**
 * A collector is a class that is responsible for 
 * obtaining data from a website (with the help of a HTTPConnector)
 * and extracting useful data from it
 */

import java.lang.RuntimeException;

public interface Collector {   

    /**
     * Defines the method that is accessed publicly.
     * Takes the url stored in the Collector object (handed through constructor)
     * @returns: a Map with a String Keys and List<String> Values
     *      that contains all necessary data which is later processed by
     *      an Evaluator
     */
    public Map<String, List<String>> get() throws RuntimeException;

    /**
     * Uses an before instanziated HTTPConnector and triggers its HTTP-Request
     * Result is saved in 
     */
    protected void fetch();
    
    /**
     * Extracts the useful data that is later used by an Evaluator
     * from the fetched (raw) data returned from the HTTPConnector
     */
    protected void extract();
    
    /**
     * @returns: the Collectors accessed url
     */
    public String url();
    
    /**
      * @returns: the raw data returned by the HTTPConnector as a String
      */
    public String raw();
    

}
