package collectors;

    /**
     * Interface for WebsiteHtml Collectors
     * Defines some Methods that are a minimum requirement
     * for WebsiteHtmlCollectors
     */

import java.util.List;
import java.util.Map;
import collectors.enums.CollectorKey;
import models.collection.CollectorValue;
import models.persistency.*;
import play.Logger;
import play.Logger.ALogger;

public class WebsiteHtmlCollector<T>    extends AbstractCollector<T> {

    private static final ALogger logger = Logger.of(WebsiteHtmlCollector.class);

    WebsiteHtmlCollectorStrategy strategy;

    public WebsiteHtmlCollector(){}
    public WebsiteHtmlCollector(WebsiteHtmlCollectorStrategy strategy) {
        this.strategy = strategy;
    }

    private void passData() {
        logger.debug("passing data to concrete collector strategy...");
        ((Collector)this.strategy).raw(this.raw());
        ((Collector)this.strategy).url(this.url());
        this.strategy.setResponseTime(CONNECTOR.getTimeToRespond());
        logger.debug("passData() to strategy - response time is :: " + CONNECTOR.getTimeToRespond());
    }

    @Override
    public Map<? extends CollectorKey, CollectorValue> extract() {
        if (this.strategy == null) throw new NullPointerException("No WebsiteHtmlCollectorStrategy found");
        this.passData();
        return this.strategy.extractFromHtml();
    }

    public List<Hyperlink> getHyperlinks() {
        if (this.strategy == null) throw new NullPointerException("No WebsiteHtmlCollectorStrategy found");
        this.passData();
        return this.strategy.getHyperlinks();
    }


    public WebPage getWebPage() {
        if (this.strategy == null) throw new NullPointerException("No WebsiteHtmlCollectorStrategy found");
        this.passData();
        return this.strategy.getWebPage();
    }

}
