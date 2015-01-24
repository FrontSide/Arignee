package collectors.request;

/*
 * This is an HTTP Connector that sends and receives HTTP Requests/Responses
 * throught the Jsoup Library :: http://jsoup.org/
 */

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

import url.URLHandler;

import play.Logger;
import play.Logger.ALogger;

import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;

public class JsoupHTTPConnector extends HTTPConnector<Element>
                                implements HTTPConnectorStrategy<Element>{

    private static final ALogger logger = Logger.of(JsoupHTTPConnector.class);

    private final int MAX_CONNECTION_ATTEMPTS = 5;

    public Element request(final URL URL) throws MalformedURLException {
        logger.debug("sending http request to :: " + URL + "...");
        return request(URL, 1);
    }

    private Element request(final URL URL, int attempts) throws MalformedURLException {

        if (attempts++ > MAX_CONNECTION_ATTEMPTS) {
            logger.error("Maximum number of HTTP requests exceeded!");
            return null;
        }

        Element doc = null;

        try {
            doc = Jsoup.connect(URL.toString()).get();
        } catch (IOException e) {
            throw new MalformedURLException("Could not connect to :: " + URL);
        } catch (IllegalArgumentException e) {
            request(URLHandler.swapSecureHttpProtocolFromUrl(URL), attempts);
            throw new MalformedURLException("Target does not seem to exist :: " + URL);
        }

        logger.info("HTTP-request successfully finished :: " + URL);
        return doc;

    }

}
