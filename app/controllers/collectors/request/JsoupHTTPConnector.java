package request;

/*
 * This is an HTTP Connector that sends and receives HTTP Requests/Responses
 * throught the Jsoup Library :: http://jsoup.org/
 */

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

public JsoupHTTPConnector implements HTTPConnector<Document> {

    public Document request(final String URL) {
    
        Logger.debug("Request url :: " + URL);
        
        Document doc = null;
                
        try {    
            doc = Jsoup.connect(URL).get();
        } catch (IOException e) {
            Logger.error("Error when trying to connect to URL :: " + URL);
        } catch (IllegalArgumentException e) {
            /* TODO: Checking if the URL is malformed should happen somewhere 
             * else or at least additionally on client side --> real-time feedback */
            Logger.warn("Malformed URL :: " + URL);
            Logger.debug("Checking if url starts with \"http://\" or \"https://\" and trying variations...");
            if (!URL.startsWith("http://") && !URL.startsWith("https://")) 
                return fetchHtml("http://" + URL);
            if (URL.startsWith("http://")) 
                return fetchHtml("https://" + URL.substring(7));
            if (URL.startsWith("https://")) 
                return fetchHtml("https://" + URL.substring(8));
            Logger.error("Invalid URL :: " + URL);
            return null;
        }
    
        Logger.debug("Request successfully finished.");
        return doc;
    
    }

}
