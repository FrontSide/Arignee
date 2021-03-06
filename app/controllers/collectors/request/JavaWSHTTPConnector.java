package collectors.request;

/*
 * This is an HTTP Connector that sends and receives HTTP Requests/Responses
 * throught the Play JabaWS Library
 */

import play.Logger;
import play.libs.ws.*;
import play.libs.F.Function;
import play.libs.F.Promise;
import java.net.URL;
import java.net.MalformedURLException;

 public class JavaWSHTTPConnector implements HTTPConnectorStrategy<String>{

    @Override
    public String request(final URL URL) throws MalformedURLException {

        Logger.debug("Request url :: " + URL);

        Promise<String> textResponsePromise =
                WS.url(URL.toString()).get().map(new Function<WSResponse, String>() {
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

    @Override
    public long getTimeToRespond() {
        return -1;
    }

 }
