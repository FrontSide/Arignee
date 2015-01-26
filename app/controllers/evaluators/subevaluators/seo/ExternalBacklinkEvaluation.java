package evaluators.subevaluators.seo;

/**
*
*/

import java.util.List;

import models.persistency.Hyperlink;
import models.persistency.WebPage;
import collectors.WebsiteHtmlCollectorFactory;
import url.URLHandler;

import play.Logger;
import play.Logger.ALogger;

public class ExternalBacklinkEvaluation implements BacklinkEvaluationStrategy {

    private static final ALogger logger = Logger.of(ExternalBacklinkEvaluation.class);

    public int getNumberOfPagesWithBacklink(WebPage webPage, List<Hyperlink> links, final WebsiteHtmlCollectorFactory COLLECTORFACTORY) {

        logger.warn("ExternalBacklinkEvaluationStrategy NOT IMPLEMENTED");

        return 0;

    }

}
