package collectors.request;

/*
 * This is an HTTP Connector that sends and receives HTTP Requests/Responses
 * throught the Jsoup Library :: http://jsoup.org/
 */

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

import play.Logger;

import java.io.IOException;

public class JsoupHTTPConnector implements HTTPConnector<Element> {

    private final int MAX_CONNECTION_ATTEMPTS = 5;

    public Element request(final String URL) {
        return request(URL, 1);
    }

    private Element request(final String URL, int attempts) {
    
        if (attempts++ > MAX_CONNECTION_ATTEMPTS) {
            Logger.error("MAXIMUM OF ATTEMPTS EXCEEDED!");
            return null;
        }
    
        Logger.debug("Request url :: " + URL);        
        Element doc = null;
                
        try {    
            doc = Jsoup.connect(URL).get();
        } catch (IOException e) {
            Logger.error("Error when trying to connect to URL :: " + URL);
        } catch (IllegalArgumentException e) {
            /* TODO: Checking if the URL is malformed should happen somewhere 
             * else or at least additionally on client side --> real-time feedback */
            Logger.warn("Malformed URL :: " + URL);
            Logger.debug("Checking if url starts with \"http://\" or " + 
                                    " \"https://\" and trying variations...");
            if (!URL.startsWith("http://") && !URL.startsWith("https://")) 
                return request("http://" + URL, attempts);
            if (URL.startsWith("http://")) 
                return request("https://" + URL.substring(7), attempts);
            if (URL.startsWith("https://")) 
                return request("https://" + URL.substring(8), attempts);
            Logger.error("Invalid URL :: " + URL);
            return null;
        }
    
        Logger.debug("Request successfully finished.");
        return doc;
    
    }

}
