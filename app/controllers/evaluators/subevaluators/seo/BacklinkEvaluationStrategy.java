package evaluators.subevaluators.seo;

/**
 *
 */

import java.util.List;
import models.persistency.Hyperlink;
import models.persistency.WebPage;
import collectors.WebsiteHtmlCollectorFactory;
import collectors.WebsiteHtmlCollector;

public interface BacklinkEvaluationStrategy {

    /**
     * Finds the number of links on a webPage that link to a
     * page which contains at least one link that links back to the
     * original webPage
     * @param   webPage     original webPage that is to be explored
     * @param   links       links on the original webPage
     * @param   COLLECTORFACTORY   Collectorfactory to be used to create collectors for fetching linkedToWebsites
     * @return  number of links that link to a page with backlinks
     */
    int getNumberOfPagesWithBacklink(WebPage webPage, List<Hyperlink> links, final WebsiteHtmlCollectorFactory COLLECTORFACTORY);

}
