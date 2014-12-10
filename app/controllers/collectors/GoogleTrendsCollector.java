package collectors;

/*
 * This class sends HTTP requests to Google Trends and collects
 * data from the response
 */
import play.Logger; 
import play.libs.ws.*;
import play.libs.F.Function;
import play.libs.F.Promise;

public class GoogleTrendsCollector {

    //Base URL and paths For Google Trends to fetch components (add "q=" for keyphrase)
    private static final String URL = "https://www.google.com/trends/fetchComponent";
    
    //The CID to fetch the timeseries of a keyword (add with "cid=")
    private static final String TIMESERIES_CID = "TIMESERIES_GRAPH_0";
    
    //The Number to Export the result as JSON (add with "export=")
    private static final String JSON_EXPORT_NUMBER = "3";
    
    /**
      * Fetches the JSON String with the Timeseries for the given keyphrase
      */
    public Sting fetchTimeseries(Sting keyphrase) {
    
        Logger.debug("Request Response of GoogleTrends Timeseries as JOSN for keyphrase :: " + keyphrase);
    
        Promise<String> textResponsePromise =
                    WS.url(URL).setQueryParameter("q", keyphrase)
                    .setQueryParameter("cid", TIMESERIES_CID)
                    .setQueryParameter("export", JSON_EXPORT_NUMBER).get()
                    .map(new Function<WSResponse, String>() {
                        public String apply(WSResponse response) {
                            String result = response.getBody();
                            return result;
                        }
                    });
        Logger.debug("done.");
        Logger.debug("receive response...");
        String response = textResponsePromise.get(1000L);
        Logger.debug("response is :: " + response);
        
    }

}
