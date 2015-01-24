package evaluators;

/**
  * This class is (among others) the heart of Arignee
  * It takes the Data extracted from the Html from
  * the Collector (handed in by the Controller) and evaluates and
  * analyses it by scientific criteria and performance indicators
  * returned is a Map with the evaluation results that might be
  * further processed by other Evaluators or merged with other
  * Data by the controller
  */

import java.net.URL;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import models.collection.CollectorValue;
import models.evaluation.EvaluationValue;
import collectors.enums.CollectorKey;
import collectors.enums.WebsiteHtmlCollectorKey;
import evaluators.enums.Rating;
import evaluators.enums.EvaluatorKey;
import evaluators.enums.WebsiteHtmlEvaluatorKey;
import evaluators.subevaluators.HtmlLinkEvaluator;
import models.persistency.Hyperlink;
import models.persistency.WebPage;
import models.evaluation.EvaluationValueContainer;
import models.collection.CollectorValue;

public class WebsiteHtmlEvaluator extends AbstractEvaluator {

    public EvaluationValue get() {

        this.result = new EvaluationValueContainer();

        //Get Map passed in from collector
        Map<? extends CollectorKey, CollectorValue> collected = this.collected();

        //Get LinkEvaluation Results
        List<Hyperlink> links = (List<Hyperlink>) collected.get(WebsiteHtmlCollectorKey.LINKS).getList();
        WebPage wp = (WebPage) collected.get(WebsiteHtmlCollectorKey.WEBPAGE).getValue();
        HtmlLinkEvaluator linkEvaluator = new HtmlLinkEvaluator(links, wp);
        linkEvaluator.setTicketNumber(this.ticketNumber);
        this.result.add(WebsiteHtmlEvaluatorKey.LINKS, linkEvaluator.get());

        return this.result;
    }


}
