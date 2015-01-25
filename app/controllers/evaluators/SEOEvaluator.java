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
import models.persistency.Hyperlink;
import models.persistency.WebPage;
import models.evaluation.EvaluationValueContainer;
import models.collection.CollectorValue;
import models.collection.CollectorValue;
import models.evaluation.EvaluationValue;
import ticketing.TicketStatus;

public class SEOEvaluator extends AbstractEvaluator {

    public EvaluationValue get() {

        this.result = new EvaluationValueContainer();

        WebPage webPage = (WebPage) collected().get(WebsiteHtmlCollectorKey.WEBPAGE).getValue();

        this.updateTicketStatus(TicketStatus.LINK_AMOUNT_EVAL);
        LinkAmountEvaluator linkAmountEvaluator = new LinkAmountEvaluator(webPage.hyperlinks.size());
        linkAmountEvaluator.setTicketNumber(this.ticketNumber);
        this.result.add(EvaluatorKey.LINK_AMOUNT, linkAmountEvaluator.get());

        List<Hyperlink> externalLinks = (List<Hyperlink>) collected().get(WebsiteHtmlCollectorKey.EXTERNAL_LINKS).getList();

        this.updateTicketStatus(TicketStatus.EXTERN_BACKLINK_RATIO_EVAL);
        BacklinkEvaluator extBacklinkEvaluator = new BacklinkEvaluator(webPage, externalLinks);
        extBacklinkEvaluator.setTicketNumber(this.ticketNumber);
        this.result.add(EvaluatorKey.EXTERNAL_BACKLINK_RATIO, extBacklinkEvaluator.get());

        return this.result;
    }


}
