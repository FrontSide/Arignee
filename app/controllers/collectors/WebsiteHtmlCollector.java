package collectors;

    /**
     * Interface for WebsiteHtml Collectors
     * Defines some Methods that are a minimum requirement
     * for WebsiteHtmlCollectors
     */

import java.util.Map;
import java.util.List;
import collectors.enums.CollectorKey;

public interface WebsiteHtmlCollector {
    
    /**
     * @ returns a List with all Linktexts
     */
    CollectorValue getLinktexts();
    
    /**
     * @ returns a List with all Link-Hrefs
     */
    CollectorValue getLinkHrefs();
    

}
