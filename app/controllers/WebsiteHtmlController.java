
package controllers;

/**
 * This class parses and renders the responses from the WebsiteHtmlCollector
 * which fetches the HTML content from Websites
 * The html is parsed with the Jsoup library http://jsoup.org/
 */
 
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
 
import collectors.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import play.mvc.*;
import play.Logger; 

public class WebsiteHtmlController extends Controller {

    /*TODO: EVALUATION of HTML by Analytic Criteria after Parsing !!!*/
    public Map<String, List<String>> evalHtml(final String URL) {    
        final Document HTML = (Document) new WebsiteHtmlCollectorFactory().create().fetchHtml(URL);
        
        Map<String, List<String>> results = new HashMap<String, List<String>>();
        
        List<String> title = new ArrayList<String>();
        title.add(HTML.title());
        results.put("title", title);
        
        final Element BODY = HTML.body();
        final Elements LINKS = BODY.getElementsByTag("a");
        List<String> linkstexts = new ArrayList<String>();
                
        for (Element e : LINKS) {
            String text = e.text();
            Logger.debug("LINK FOUND WITH TEXT :: " + text);
            linkstexts.add(text);
        }
        
        results.put("linktexts", linkstexts);
        
        return results;        
    }

}
