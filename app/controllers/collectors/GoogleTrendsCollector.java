
public abstract class GoogleTrendsCollector {

     //Base URL and paths For Google Trends to fetch components (add "q=" for keyphrase)
    //private static final String URL = "http://www.google.com/trends/fetchComponent";
    //private static final String URL = "http://weather.yahooapis.com/forecastrss?p=80020&u=f";
    //private static final String URL = "http://www.google.com/trends/fetchComponent?q=asdf,qwerty&cid=TIMESERIES_GRAPH_0&export=3";
    
    //The CID to fetch the timeseries of a keyword (add with "cid=")
    protected static final String TIMESERIES_CID = "TIMESERIES_GRAPH_0";
    
    //The Number to Export the result as JSON (add with "export=")
    protected static final String JSON_EXPORT_NUMBER = "3";
    
    public abstract String fetchTimeseries(String keyphrase);
    
}
