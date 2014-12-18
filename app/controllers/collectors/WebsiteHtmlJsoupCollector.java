
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

import play.Logger; 

public class WebsiteHtmlJsoupCollector extends AbstractCollector<Document> {

    public WebsiteHtmlJsoupCollector(String url) {
        this.url = URL;
    }
    
    

}
