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
import collectors.enums.CollectorKey;
import collectors.enums.WebsiteHtmlCollectorKey;
import evaluators.enums.Rating;
import evaluators.enums.EvaluatorKey;
import evaluators.enums.WebsiteHtmlEvaluatorKey;

public class WebsiteHtmlEvaluator extends AbstractEvaluator {

    public EvaluationResult get() {
    
        this.result = new EvaluationResultContainer();
                        
        //Get Map passed in from collector
        Map<? extends CollectorKey, List<String>> collected =
                this.collected();
        
        /* Add Map for link-evluation figures with result from the
         * evaluateLinks() method. Pass "Links" from collected-list
         * to full evaluation-result-Map.
         */
        ((EvaluationResultContainer) this.result).add(WebsiteHtmlEvaluatorKey.LINKS_EVAL_RESULTS, 
                      evaluateLinks(collected.get(WebsiteHtmlCollectorKey.LINKTEXTS)));
        
        return this.result;
    }
    
    /**
     * Calculates the rating for the Links
     * @returns a map with all the evaluation results for the Links
     *      which is later integrated in the full evaluation list
     */
    private EvaluationResultContainer evaluateLinks(List<String> links){
        
        EvaluationResultContainer results = new EvaluationResultContainer();
        
        /* Rate the amount of links by percentual divergence
         * Assume that an amount around 17 would be perfect
         */
        final int LINKS_AMOUNT_IDEAL = 17;
        int linkAmount = links.size();
        float linkAmountdiv = Math.abs(percentualDivergence(LINKS_AMOUNT_IDEAL, linkAmount));
        results.add(WebsiteHtmlEvaluatorKey.LINK_AMOUNT, new EvaluationFigure(linkAmount));
        Rating linkAmountRating;
        if (linkAmountdiv > 200) linkAmountRating = Rating.POOR;
        else if (linkAmountdiv > 150) linkAmountRating = Rating.TENUOUS;
        else if (linkAmountdiv > 100) linkAmountRating = Rating.OK;
        else if (linkAmountdiv > 50) linkAmountRating = Rating.GOOD;
        else linkAmountRating = Rating.EXCELLENT;        
        results.add(WebsiteHtmlEvaluatorKey.LINK_AMOUNT_RATING, new EvaluationFigure(linkAmountRating));
        
        return results;
        
    }

}
