package collectors;

/*
 * This class sends HTTP requests to Google Trends and collects
 * data from the response using the Play JavaWS Library
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

public class GoogleTrendsJavaWSCollector extends GoogleTrendsCollector {

   
    
    /**
      * Fetches the JSON String with the Timeseries for the given keyphrase
      */
    public String fetchTimeseries(String keyphrase) {
    
        Logger.debug("Request Response from GoogleTrends Timeseries as JOSN for keyphrase :: " + keyphrase);

        /*// Set up the client config (you can also use a parser here):
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
        AsyncHttpClientConfig secureDefaults = secureBuilder.build(); */

        // You can directly use the builder for specific options once you have secure TLS defaults...
        /*AsyncHttpClientConfig customConfig = new AsyncHttpClientConfig.Builder(secureDefaults)
                 .setProxyServer(new com.ning.http.client.ProxyServer(http.proxyHost, http.proxyPort))
                 .setCompressionEnabled(true)
                 .build(); */
                 
       // WSClient customClient = new play.libs.ws.ning.NingWSClient(customConfig);
                
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
        String response = textResponsePromise.get(20000L);
        Logger.debug("response is :: " + response);
        
        return response;
        
    }

}
