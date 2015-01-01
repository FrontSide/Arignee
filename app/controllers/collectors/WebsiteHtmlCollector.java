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
import models.persistency.Hyperlink;
import play.Logger;

public class WebsiteHtmlCollector<T> extends AbstractCollector<T> {

    WebsiteHtmlCollectorStrategy strategy;

    public WebsiteHtmlCollector(){}
    public WebsiteHtmlCollector(WebsiteHtmlCollectorStrategy strategy) {
        this.strategy = strategy;
    }

    private void passData() {
        ((Collector)this.strategy).raw(this.raw());
        ((Collector)this.strategy).url(this.url());
    }

    public Map<? extends CollectorKey, CollectorValue> executeExtract() {
        if (this.strategy == null) throw new NullPointerException("No WebsiteHtmlCollectorStrategy found");
        this.passData();
        return this.strategy.extractFromHtml();
    }

    public List<Hyperlink> getHyperlinks() {
        this.passData();
        return this.strategy.getHyperlinks();
    }

}
