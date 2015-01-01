package collectors;

/**
 *
 */

import java.util.List;
import java.util.Map;
import collectors.enums.CollectorKey;
import models.collection.CollectorValue;
import models.persistency.Hyperlink;

public interface WebsiteHtmlCollectorStrategy {

    /**
     * Extract collector data from raw html coming from HTTPRequestor
     * @return     Collector Data in Maps
     */
    Map<? extends CollectorKey, CollectorValue> extractFromHtml();

    /**
    * @ returns a List with all HyperlinkObjects (models.Hyperlinks)
    * of a WebPage
    */
    List<Hyperlink> getHyperlinks();

}
