
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
import models.Hyperlink;

import play.Logger;

public class WebsiteHtmlJsoupCollector extends AbstractCollector<Element>
                                        implements WebsiteHtmlCollector {


    Element collectedBody;
    Elements collectedLinks;

    @Override
    protected Map<? extends CollectorKey, CollectorValue> extract() {

        Map<WebsiteHtmlCollectorKey, CollectorValue> results = new HashMap<>();

        /* Add eval-Page Url */
        results.put(WebsiteHtmlCollectorKey.URL,
                    new CollectorStringValue(this.url()));

        /* Add extracted Title */
        results.put(WebsiteHtmlCollectorKey.TITLE,
                    new CollectorStringValue(((Document)raw()).title()));

        /* Add extracted Hyperlinks */
        results.put(WebsiteHtmlCollectorKey.LINKS,
                    new CollectorModelValue<Hyperlink>(getHyperlinks()));

        return results;

    }

    /**
     * Fetches the Body Element incl. content out of the raw html
     * @return The Body as jsoup-Element object
     */
    private Element collectBody() {

        try {
            this.collectedBody = ((Document)raw()).body();
        } catch (NullPointerException e) {
            Logger.error("Extracting Hyperlink Elements from \"raw\" failed!");
            Logger.warn("Is the element \"raw\" null?");
        }

        return this.collectedBody;

    }

    /**
     * Fetches the "<a"-Tags incl. content out of the raw html
     * @return all the Hyperlinks as jsoup-Elements object
     */
    private Elements collectHyperlinks() {

        try {
            this.collectedLinks = ((Document)raw()).getElementsByTag("a");
        } catch (NullPointerException e) {
            Logger.error("Extracting Hyperlink Elements from \"raw\" failed!");
            Logger.warn("Is the element \"raw\" null?");
        }

        return this.collectedLinks;

    }

    @Override
    public List<Hyperlink> getHyperlinks() {

        if (this.collectedLinks == null) collectHyperlinks();
        List<Hyperlink> hyperlinks = new ArrayList<>();
        Iterator<Element> it = this.collectedLinks.iterator();
        while (it.hasNext()) {
            Element linkElement = it.next();
            Hyperlink h = new Hyperlink();
            h.text = linkElement.text();
            h.target = linkElement.attr("href");
            hyperlinks.add(h);
        }

        return hyperlinks;

    }

}
