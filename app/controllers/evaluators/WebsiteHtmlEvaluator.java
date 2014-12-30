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

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import collectors.CollectorValue;
import collectors.enums.CollectorKey;
import collectors.enums.WebsiteHtmlCollectorKey;
import evaluators.enums.Rating;
import evaluators.enums.EvaluatorKey;
import evaluators.enums.WebsiteHtmlEvaluatorKey;
import evaluators.subevaluators.HtmlLinkEvaluator;
import models.Hyperlink;

public class WebsiteHtmlEvaluator extends AbstractEvaluator {

    public EvaluationResult get() {

        this.result = new EvaluationResultContainer();

        //Get Map passed in from collector
        Map<? extends CollectorKey, CollectorValue> collected = this.collected();

        //Get LinkEvaluation Results
        List<Hyperlink> links = (List<Hyperlink>) collected.get(WebsiteHtmlCollectorKey.LINKS).getList();
        String url = (String) collected.get(WebsiteHtmlCollectorKey.URL).getValue();
        HtmlLinkEvaluator linkEvaluator = new HtmlLinkEvaluator(links, url);
        this.result.add(WebsiteHtmlEvaluatorKey.LINKS, linkEvaluator.get());

        return this.result;
    }


}
