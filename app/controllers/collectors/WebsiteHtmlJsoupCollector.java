
package collectors;

/**
 * Implementation of a WebsiteHtmlCollector
 * using the Jsoup Library ::  http://jsoup.org/
 */

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

import java.io.IOException;
import java.lang.IllegalArgumentException;
import java.lang.String.*;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import collectors.enums.CollectorKey;
import collectors.enums.WebsiteHtmlCollectorKey;

import play.Logger; 

public class WebsiteHtmlJsoupCollector extends AbstractCollector<Document> {

    
    @Override
    protected Map<? extends CollectorKey, List<String>> extract(Document raw) {
        
        //Instantiate result collection with WebsiteHTMLKeys
        Map<WebsiteHtmlCollectorKey, List<String>> results = 
                new HashMap<WebsiteHtmlCollectorKey, List<String>>();
        
        //Get Website Title
        List<String> title = new ArrayList<String>();
        title.add(raw.title());
        results.put(WebsiteHtmlCollectorKey.TITLE, title);
        
        //Extract Body
        final Element BODY = raw.body();
        
        //Extract Links from Body
        final Elements LINKS = BODY.getElementsByTag("a");
        
        //Get Text from all Links and save in linkstexts-List
        List<String> linkstexts = new ArrayList<String>();
        for (Element e : LINKS) {
            String text = e.text();
            Logger.debug("Link found with text :: " + text);
            linkstexts.add(text);
        }
        results.put(WebsiteHtmlCollectorKey.LINKTEXTS, linkstexts);
        
        return results;
        
    }

}
