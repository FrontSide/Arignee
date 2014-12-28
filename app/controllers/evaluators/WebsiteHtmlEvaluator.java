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

public class WebsiteHtmlEvaluator extends AbstractEvaluator {

    public EvaluationResult get() {
    
        this.result = new EvaluationResultContainer();
                        
        //Get Map passed in from collector
        Map<? extends CollectorKey, CollectorValue> collected = this.collected();
        
        /* Add Map for link-evluation figures with result from the
         * HtmlLinkEvaluator
         */
        ((EvaluationResultContainer) this.result)
                    .add(WebsiteHtmlEvaluatorKey.LINKS,
                            new HtmlLinkEvaluator(
                                collected.get(
                                    WebsiteHtmlCollectorKey.LINKTEXTS),
                                collected.get(
                                    WebsiteHtmlCollectorKey.LINKHREFS),
                                collected.get(
                                    WebsiteHtmlCollectorKey.URL)).get());
        
        return this.result;
    }
   

}
