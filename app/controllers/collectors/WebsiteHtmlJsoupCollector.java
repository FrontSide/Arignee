
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
import models.persistency.Hyperlink;
import models.persistency.WebPage;
import models.collection.CollectorValue;
import models.collection.CollectorStringValue;
import models.collection.CollectorModelValue;
import ticketing.TicketStatus;
import url.URLHandler;

import play.Logger;

public class WebsiteHtmlJsoupCollector extends WebsiteHtmlCollector<Element>
                                    implements WebsiteHtmlCollectorStrategy {


    Element collectedBody;
    Elements collectedLinks;

    @Override
    public Map<? extends CollectorKey, CollectorValue> extractFromHtml() {

        this.updateTicketStatus(TicketStatus.EXTRACTING);

        if (this.raw() == null) throw new NullPointerException("No raw data found. Try raw(Element raw).");
        if (this.url() == null) throw new NullPointerException("No url found. Try url(String url).");

        Logger.debug("Start extracting ...");

        Map<WebsiteHtmlCollectorKey, CollectorValue> results = new HashMap<>();

        /* Add eval-Page Url */
        results.put(WebsiteHtmlCollectorKey.WEBPAGE,
                    new CollectorModelValue(getWebPage()));

        results.put(WebsiteHtmlCollectorKey.INTERNAL_LINKS,
                    new CollectorModelValue(getAllInternalLinks()));

        results.put(WebsiteHtmlCollectorKey.EXTERNAL_LINKS,
                    new CollectorModelValue(getAllExternalLinks()));

        /* Add extracted Title */
        results.put(WebsiteHtmlCollectorKey.TITLE,
                    new CollectorStringValue(((Document)this.raw()).title()));

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

        this.collectedLinks = new Elements();

        try {
            this.collectedLinks.addAll(((Document)raw()).getElementsByTag("a"));
            this.collectedLinks.addAll(((Document)raw()).getElementsByTag("link"));
        } catch (NullPointerException e) {
            Logger.error("Extracting Hyperlink Elements from \"raw\" failed!");
            Logger.warn("Is the element \"raw\" null?");
        }

        return this.collectedLinks;

    }

    @Override
    public WebPage getWebPage() {
        WebPage webPage = new WebPage(url());
        webPage.hyperlinks = getHyperlinks();
        return webPage;
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
            h.href = linkElement.attr("href");
            hyperlinks.add(h);
        }

        return hyperlinks;
    }

    private List<Hyperlink> internalLinks;
    private List<Hyperlink> externalLinks;

    /**
    * Filters all the Hyperlinks from the full Hyperlink-List
    * of the source page that can be identified as Domain-Internal-Links and
    * puts those in the global "internalLinks"-List and the rest in the
    * "externalLinks"-List
    */
    private void seperateInternalFromExternalLinks() {
        this.internalLinks = new ArrayList<>();
        this.externalLinks = new ArrayList<>();
        for (Hyperlink h : getHyperlinks()) {
            if (URLHandler.isInternalUrl(url(), h.href))
                this.internalLinks.add(h);
            else    this.externalLinks.add(h);
        }
    }

    private List<Hyperlink> getAllInternalLinks() {
        if (this.internalLinks == null) seperateInternalFromExternalLinks();
        return this.internalLinks;
    }

    private List<Hyperlink> getAllExternalLinks() {
        if (this.externalLinks == null) seperateInternalFromExternalLinks();
        return this.externalLinks;
    }

}
