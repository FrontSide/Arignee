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

import java.util.Map;
import java.util.List;
import collectors.enums.CollectorKey;

public class GoogleTrendsJavaWSCollector extends GoogleTrendsCollector {
    
    /**
      * Fetches the JSON String with the Timeseries for the given keyphrase
      
    public String fetchTimeseries(String keyphrase) {
    
        String url = WS.url(URL).setQueryParameter("q", keyphrase)
                .setQueryParameter("cid", TIMESERIES_CID)
                .setQueryParameter("export", JSON_EXPORT_NUMBER).getUrl();

        String response = new HTTPConnectorFactory(this).create().request(url);
                
    } */
    
    @Override
    protected Map<CollectorKey, List<String>> extract(String url) { return null; }

}
