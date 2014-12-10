package collectors;

/*
 * This class sends HTTP requests to Google Trends and collects
 * data from the response
 */
import play.Logger; 
import play.libs.ws.*;
import play.libs.F.Function;
import play.libs.F.Promise;

import com.ning.http.client.*;
import play.api.libs.ws.WSClientConfig;
import play.api.libs.ws.DefaultWSClientConfig;
import play.api.libs.ws.ssl.SSLConfig;
import play.api.libs.ws.ning.NingAsyncHttpClientConfigBuilder;

public class GoogleTrendsCollector {

    //Base URL and paths For Google Trends to fetch components (add "q=" for keyphrase)
    //private static final String URL = "http://www.google.com/trends/fetchComponent";
    private static final String URL = "http://www.dary.info";
    
    //The CID to fetch the timeseries of a keyword (add with "cid=")
    private static final String TIMESERIES_CID = "TIMESERIES_GRAPH_0";
    
    //The Number to Export the result as JSON (add with "export=")
    private static final String JSON_EXPORT_NUMBER = "3";
    
    /**
      * Fetches the JSON String with the Timeseries for the given keyphrase
      */
    public String fetchTimeseries(String keyphrase) {
    
        Logger.debug("Request Response of GoogleTrends Timeseries as JOSN for keyphrase :: " + keyphrase);
        /*
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
        
        com.ning.http.client.AsyncHttpClientConfig.useProxyProperties = true;
        
        com.ning.http.client.AsyncHttpClientConfig customConfig =
            new com.ning.http.client.AsyncHttpClientConfig.Builder()
                .setProxyServer(new com.ning.http.client.ProxyServer("localhost", 80))
                .setCompressionEnabled(true)
                .build();
                
        WSClient customClient = new play.libs.ws.ning.NingWSClient(customConfig);
        
        Promise<WSResponse> homePage = customClient.url(URL).get();
        
        Promise<String> textResponsePromise = 
            homePage.map(new Function<WSResponse, String>() {
                public String apply(WSResponse response) {
                    //Logger.debug("status code from \"" + URL + "\" is :: " + response.getStatus());
                    String result = response.getBody();
                    //return result;
                    return "semiresult";
                }
            });          
        
        
        // Set up the client config (you can also use a parser here):
        scala.Option<Object> none = scala.None$.empty();
        scala.Option<String> noneString = scala.None$.empty();
        scala.Option<SSLConfig> noneSSLConfig = scala.None$.empty();
        WSClientConfig clientConfig = new DefaultWSClientConfig(
            none, // connectionTimeout
            none, // idleTimeout
            none, // requestTimeout
            none, // followRedirects
            none, // useProxyProperties
            noneString, // userAgent
            none, // compressionEnabled
            none, // acceptAnyCertificate
            noneSSLConfig);

         // Build a secure config out of the client config and the ning builder:
         AsyncHttpClientConfig.Builder asyncHttpClientBuilder = new AsyncHttpClientConfig.Builder();
         NingAsyncHttpClientConfigBuilder secureBuilder = new NingAsyncHttpClientConfigBuilder(clientConfig,
                 asyncHttpClientBuilder);
         AsyncHttpClientConfig secureDefaults = secureBuilder.build();

         // You can directly use the builder for specific options once you have secure TLS defaults...
        AsyncHttpClientConfig customConfig = new AsyncHttpClientConfig.Builder(secureDefaults)
                         .setProxyServer(new com.ning.http.client.ProxyServer("127.0.0.1", 80))
                         .setCompressionEnabled(true)
                         .build();
         WSClient customClient = new play.libs.ws.ning.NingWSClient(customConfig);

         Promise<WSResponse> responsePromise = customClient.url("http://example.com/feed").get();
        
         Promise<String> textResponsePromise = 
            responsePromise.map(new Function<WSResponse, String>() {
                public String apply(WSResponse response) {
                    Logger.debug("status code from \"" + URL + "\" is :: " + response.getStatus());
                    String result = response.getBody();
                    return result;
                    //return "semiresult";
                }
            }); */
        
        Promise<WSResponse> responsePromise = WS.url("http://www.dary.info:80").get();
        
        /*Promise<String> textResponsePromise = 
            responsePromise.map(new Function<WSResponse, String>() {
                public String apply(WSResponse response) {
                    Logger.debug("status code from \"" + URL + "\" is :: " + response.getStatus());
                    String result = response.getBody();
                    return result;
                    //return "semiresult";
                }
            });*/
        WSResponse wsres = responsePromise.get(1000L);
        
        Logger.debug("done.");
        Logger.debug("receive response...");
        String response = wsres.getBody();
        Logger.debug("response is :: " + response);
        
        return "response";
        
    }

}
