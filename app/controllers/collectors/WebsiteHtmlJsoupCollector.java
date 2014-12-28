
package collectors;

/**
 * Implementation of a WebsiteHtmlCollector
 * using the Jsoup Library ::  http://jsoup.org/
 */

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

import java.io.IOException;
import java.lang.NullPointerException;
import java.lang.IllegalArgumentException;
import java.lang.String.*;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import collectors.enums.CollectorKey;
import collectors.enums.WebsiteHtmlCollectorKey;

import play.Logger; 

public class WebsiteHtmlJsoupCollector extends AbstractCollector<Element> 
                                        implements WebsiteHtmlCollector {

    
    Element body;
    Elements links;
    
    @Override
    protected Map<? extends CollectorKey, CollectorValue> extract() {
        
        //Instantiate result collection with WebsiteHTMLKeys
        Map<WebsiteHtmlCollectorKey, CollectorValue> results = new HashMap<>();
        
        results.put(WebsiteHtmlCollectorKey.URL, new CollectorValue(this.url()));
        
        //Get Website Title    
        results.put(WebsiteHtmlCollectorKey.TITLE, 
            /* new CollectorValue(raw().getElementsByTag("title").first().text())) */
            new CollectorValue(((Document)raw()).title()));
                
        //Links
        results.put(WebsiteHtmlCollectorKey.LINKTEXTS, getLinktexts());
        results.put(WebsiteHtmlCollectorKey.LINKHREFS, getLinkHrefs());
        
        return results;
        
    }
    
    private Element getBody() {
                
        if (this.body == null) {
            try {
                this.body = ((Document)raw()).body();
            } catch (NullPointerException e) {
                Logger.error("extracting Hyperlink Elements from raw failed!");
            }
        }
            
        return this.body;
    
    }
    
    private Elements getLinks() {
        
        Logger.debug("Get all links from " + this.url());
        
        if (this.links == null) {
            try {
                this.links = ((Document)raw()).getElementsByTag("a");
            } catch (NullPointerException e) {
                Logger.error("extracting Hyperlink Elements from raw failed!");
            }
        }
        return this.links;
        
    }
    
    @Override
    public CollectorValue getLinktexts() {
    
        CollectorValue linktexts = new CollectorValue();
        Iterator<Element> it = this.getLinks().iterator();
        while (it.hasNext()) {
            String text = it.next().text();
            linktexts.add(text);
        }
        return linktexts;
        
    }
    
    @Override
    public CollectorValue getLinkHrefs() {
    
        CollectorValue linkhrefs = new CollectorValue();
        Iterator<Element> it = this.getLinks().iterator();
        while (it.hasNext()) {
            String href = it.next().attr("href");
            linkhrefs.add(href);
        }
        return linkhrefs;
    
    }

}
