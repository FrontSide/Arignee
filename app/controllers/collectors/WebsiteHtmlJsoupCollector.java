
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
    protected Map<? extends CollectorKey, CollectorValue> extract(Document raw) {
        
        //Instantiate result collection with WebsiteHTMLKeys
        Map<WebsiteHtmlCollectorKey, CollectorValue> results = new HashMap<>();
        
        results.put(WebsiteHtmlCollectorKey.URL, new CollectorValue(this.url()));
        
        //Get Website Title
        CollectorValue title = new CollectorValue(raw.title());
        
        results.put(WebsiteHtmlCollectorKey.TITLE, title);
        
        //Extract Body
        final Element BODY = raw.body();
        
        //Extract Links from Body
        final Elements LINKS = BODY.getElementsByTag("a");
        
        //Get Text from all Links and save in linkstexts-List
        CollectorValue linkstexts = new CollectorValue();
        for (Element e : LINKS) {
            String text = e.text();
            linkstexts.add(text);
        }
        results.put(WebsiteHtmlCollectorKey.LINKTEXTS, linkstexts);
        
        return results;
        
    }

}
