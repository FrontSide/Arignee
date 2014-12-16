
package controllers;

/**
 * This class parses and renders the responses from the WebsiteHtmlCollector
 * which fetches the HTML content from Websites
 */
 
import collectors.*;
import play.mvc.*;
import java.util.Map;

public class WebsiteHtmlController extends Controller {

    public Map<String, String> evalHtml(final String URL) {
        final String html = new WebsiteHtmlCollectorFactory().create().fetchHtml(URL);
        
        
        
    }

}
