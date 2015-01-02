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

import models.collection.CollectorValue;
import collectors.enums.CollectorKey;

public class GoogleTrendsJavaWSCollector extends GoogleTrendsCollector<String>
                                implements  GoogleTrendsCollectorStrategy {

    /**
      * Fetches the JSON String with the Timeseries for the given keyphrase
      */
    public String fetchTimeseries(String keyphrase) {

        throw new UnsupportedOperationException("Not implemented!");

    }

    @Override
    public Map<CollectorKey, CollectorValue> extractFromRaw() {
        return null;
    }

}
