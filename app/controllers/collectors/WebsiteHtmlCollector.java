package collectors;

    /**
     * Interface for WebsiteHtml Collectors
     * Defines some Methods that are a minimum requirement
     * for WebsiteHtmlCollectors
     */

import java.util.List;
import models.persistency.Hyperlink;

public interface WebsiteHtmlCollector {

    /**
     * @ returns a List with all HyperlinkObjects (models.Hyperlinks)
     * of a WebPage
     */
    List<Hyperlink> getHyperlinks();

}
