
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

public class GoogleTrendsElibusCollector extends GoogleTrendsCollector {


    protected Map<CollectorKey, CollectorValue> extract() {

        return null;

    }

/*
    private String action(String keyphrase) throws GoogleTrendsClientException {

        Logger.debug("Request Response of GoogleTrends Timeseries as JOSN for keyphrase :: " + keyphrase);
        Logger.debug("METHOD :: Elibus");

        // SET USERNAME
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // SET PASSWORD !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        DefaultHttpClient httpClient = new DefaultHttpClient();

        /* Creates a new authenticator
        GoogleAuthenticator authenticator = new GoogleAuthenticator(u, p, httpClient);

        /* Creates a new Google Trends Client
        GoogleTrendsClient client = new GoogleTrendsClient(authenticator, httpClient);
        GoogleTrendsRequest request = new GoogleTrendsRequest(keyphrase);

        /* Here the default request params can be modified with getter/setter methods
        String content = client.execute(request);

        /* The default request downloads a CSV available in content
        GoogleTrendsCsvParser csvParser = new GoogleTrendsCsvParser(content);
        /* Get a specific section of the CSV
        String section = csvParser.getSectionAsString("Top searches for", true);
         Logger.debug("done. SECTION :: " + section);

        return section;

    }
    */
}
