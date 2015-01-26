package evaluators;

/**
* Conducts the Search-Engine-Optimization Evaluations
*/

import java.net.URL;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import collectors.enums.CollectorKey;
import collectors.enums.WebsiteHtmlCollectorKey;
import evaluators.enums.Rating;
import evaluators.enums.EvaluatorKey;
import evaluators.subevaluators.LinkAmountEvaluator;
import evaluators.subevaluators.seo.BacklinkEvaluator;
import evaluators.subevaluators.seo.EBacklinkEvaluationStrategy;
import models.persistency.Hyperlink;
import models.persistency.WebPage;
import models.evaluation.EvaluationValueContainer;
import models.collection.CollectorValue;
import models.collection.CollectorValue;
import models.evaluation.EvaluationValue;
import ticketing.TicketStatus;

public class UsabilityEvaluator extends AbstractEvaluator {

    public EvaluationValue get() {

        this.result = new EvaluationValueContainer();

        WebPage webPage = (WebPage) collected().get(WebsiteHtmlCollectorKey.WEBPAGE).getValue();
        List<Hyperlink> externalLinks = (List<Hyperlink>) collected().get(WebsiteHtmlCollectorKey.INTERNAL_LINKS).getList();

        this.updateTicketStatus(TicketStatus.INTERN_BACKLINK_RATIO_EVAL);
        BacklinkEvaluator extBacklinkEvaluator = new BacklinkEvaluator(webPage, externalLinks, EBacklinkEvaluationStrategy.INTERNAL);
        extBacklinkEvaluator.setTicketNumber(this.ticketNumber);
        this.result.add(EvaluatorKey.INTERNAL_BACKLINK_RATIO, extBacklinkEvaluator.get());

        return this.result;
    }


}
