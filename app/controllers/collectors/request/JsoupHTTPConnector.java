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
import java.net.MalformedURLException;

public class JsoupHTTPConnector extends HTTPConnector<Element>
                                implements HTTPConnectorStrategy<Element>{

    private static final ALogger logger = Logger.of(JsoupHTTPConnector.class);

    private final int MAX_CONNECTION_ATTEMPTS = 5;

    public Element request(final String URL) throws MalformedURLException {
        logger.debug("sending http request to :: " + URL + "...");
        return request(URL, 1);
    }

    private Element request(final String URL, int attempts) throws MalformedURLException {

        if (attempts++ > MAX_CONNECTION_ATTEMPTS) {
            logger.error("Maximum number of HTTP requests exceeded!");
            return null;
        }

        Element doc = null;

        try {
            doc = Jsoup.connect(URL).get();
        } catch (IOException e) {
            throw new MalformedURLException("Invlid URL :: " + URL);
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
            throw new MalformedURLException("Invlid URL :: " + URL);
        }

        logger.info("HTTP-request successfully finished :: " + URL);
        return doc;

    }

}
