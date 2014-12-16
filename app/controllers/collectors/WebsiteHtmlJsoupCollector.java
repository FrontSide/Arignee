
package collectors;

/**
 * This class fetches the HTML content from a website using the 
 * Jsoup library http://jsoup.org/
 */

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

import java.io.IOException;
import java.lang.IllegalArgumentException;
import java.lang.String.*;

import play.Logger; 

public class WebsiteHtmlJsoupCollector extends WebsiteHtmlCollector {

    public String fetchHtml(final String URL) {
    
        Logger.debug("Request HTML from URL :: " + URL);
    
        Document doc = null;
        
        try {    
            doc = Jsoup.connect(URL).get();
        } catch (IOException e) {
            Logger.error("Error when trying to connect to URL :: " + URL);
        } catch (IllegalArgumentException e) {
            /* TODO: Checking if the URL is malformed should happen somewhere 
             * else or at least additionally on client side --> real-time feedback */
            Logger.warn("Malformed URL :: " + URL);
            Logger.debug("Check if url starts with \"http://\" or \"https://\" and try variations...");
            if (!URL.startsWith("http://") && !URL.startsWith("https://")) return fetchHtml("http://" + URL);
            if (URL.startsWith("http://")) return fetchHtml("https://" + URL.substring(7));
            if (URL.startsWith("https://")) return fetchHtml("https://" + URL.substring(8));
            Logger.error("Invalid URL :: " + URL);
            return null;
        }
        
        Logger.debug("HTML is ::" + doc.html());
        return doc.html();
    
    }

}
