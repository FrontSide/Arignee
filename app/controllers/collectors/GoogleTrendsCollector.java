
package collectors;

    /**
     * This is class for Abstracting the GoogleTrends Controller
     * it overloads the get method so that a keyphrase can be accepted from 
     * the controllers and implements the urlBuilder which creates the url
     * out of the keyphrase and given url parameters
     */

import java.util.Map;
import java.util.List;
import collectors.enums.CollectorKey;

public abstract class GoogleTrendsCollector extends AbstractCollector<String> {

     //Base URL and paths For Google Trends to fetch components (add "q=" for keyphrase)
    //private static final String BASEURL = "http://weather.yahooapis.com/forecastrss?p=80020&u=f";
    protected static final String BASEURL = "http://www.google.com/trends/fetchComponent";
    
    //The CID to fetch the timeseries of a keyword (add with "cid=")
    protected static final String TIMESERIES_CID = "TIMESERIES_GRAPH_0";
    
    //The Number to Export the result as JSON (add with "export=")
    protected static final String JSON_EXPORT_NUMBER = "3";
    
    private String keyphrase;
    
    /* GoogleTrendsCollectors OVERLOAD the get Method from the AbstractCollector
     * with a String parameter for the Keyphrase that should be searched for */
    public Map<? extends CollectorKey, List<String>> get(String kephrase) throws RuntimeException {
        this.keyphrase = keyphrase;
        this.buildUrl();
        return this.get();
    }
    
    @Override
    public void buildUrl() {
        this.url("http://www.google.com/trends/fetchComponent"+
                        "?q=asdf,qwerty&cid=TIMESERIES_GRAPH_0&export=3");
        /*this.url = BASEURL + "?q=" + this.keyphrase + 
                  "&cid=" + TIMESERIES_CID + "&export=" + JSON_EXPORT_NUMBER;*/
    }
    
}
