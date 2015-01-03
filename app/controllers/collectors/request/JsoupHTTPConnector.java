package collectors.request;

/*
 * This is an HTTP Connector that sends and receives HTTP Requests/Responses
 * throught the Jsoup Library :: http://jsoup.org/
 */

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

import play.Logger;
import play.Logger.ALogger;

import java.io.IOException;

public class JsoupHTTPConnector extends HTTPConnector<Element>
                                implements HTTPConnectorStrategy<Element>{

    private static final ALogger logger = Logger.of(JsoupHTTPConnector.class);

    private final int MAX_CONNECTION_ATTEMPTS = 5;

    public Element request(final String URL) {
        logger.debug("sending http request to :: " + URL + "...");
        return request(URL, 1);
    }

    private Element request(final String URL, int attempts) {

        if (attempts++ > MAX_CONNECTION_ATTEMPTS) {
            logger.error("Maximum number of HTTP requests exceeded!");
            return null;
        }

        Element doc = null;

        try {
            doc = Jsoup.connect(URL).get();
        } catch (IOException e) {
            logger.error("Error encountered when trying to connect to URL :: " + URL);
        } catch (IllegalArgumentException e) {
            /* TODO: Checking if the URL is malformed should happen somewhere
             * else or at least additionally on client side --> real-time feedback */
            logger.warn("malformed URL encountered :: " + URL);
            logger.debug("trying variations...");
            if (!URL.startsWith("http://") && !URL.startsWith("https://"))
                return request("http://" + URL, attempts);
            if (URL.startsWith("http://"))
                return request("https://" + URL.substring(7), attempts);
            if (URL.startsWith("https://"))
                return request("https://" + URL.substring(8), attempts);
            logger.error("invalid URL encountered :: " + URL);
            return null;
        }

        logger.debug("HTTP-request successfully finished.");
        return doc;

    }

}
