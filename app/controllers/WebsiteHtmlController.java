
package controllers;

/**
 * This class parses and renders the responses from the WebsiteHtmlCollector
 * which fetches the HTML content from Websites
 * The html is parsed with the Jsoup library http://jsoup.org/
 */
 
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
 
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import play.mvc.*;
import play.Logger; 

import collectors.WebsiteHtmlCollectorFactory;
import collectors.enums.Key;
import collectors.enums.WebsiteHtmlKey;

public class WebsiteHtmlController extends Controller {

    private final WebsiteHtmlCollectorFactory COLLECTORFACTORY = 
                                WebsiteHtmlCollectorFactory.getInstance();

    /**
      * Receives a Map with all relevent data from the Controller
      * passes it to the Evaluator and returns
      * a Map that is ready to be rendered by the template
      */
    public Map<String, List<String>> evaluate(final String URL) {
    
        // Create Collector and obtain extracted data
        Logger.debug("Invoke Collector for URL :: " + URL);
        collectors.Collector collector = this.COLLECTORFACTORY.create();
        Map<? extends Key, List<String>> data = collector.url(URL).get();
        
        Logger.debug("Collected data is :: " + data.toString());
        
        //TODO: pass data to evaluator
        return new HashMap<String, List<String>>();
        
    }

}
