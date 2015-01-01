
package collectors;
/*
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.NTCredentials;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.freaknet.gtrends.api.GoogleAuthenticator;
import org.freaknet.gtrends.api.GoogleTrendsClient;
import org.freaknet.gtrends.api.GoogleTrendsCsvParser;
import org.freaknet.gtrends.api.GoogleTrendsRequest;
import org.freaknet.gtrends.api.exceptions.GoogleTrendsClientException;

/*
 * This class sends HTTP requests to Google Trends and collects
 * data from the response using an unofficial API provided by
 * https://github.com/elibus/j-google-trends-api
 */

import java.util.Map;

import collectors.enums.CollectorKey;
import models.collection.CollectorValue;

public class GoogleTrendsElibusCollector extends GoogleTrendsCollector<String>
                                    implements  GoogleTrendsCollectorStrategy {


    @Override
    public Map<CollectorKey, CollectorValue> extract() {
        throw new UnsupportedOperationException("Not implemented");
    }

}
