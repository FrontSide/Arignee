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
import java.util.List;
import evaluators.enums.WebsiteHtmlEvaluatorKey;

public class WebsiteHtmlEvaluator extends AbstractEvaluator {

    public Map<WebsiteHtmlEvaluatorKey, Map<WebsiteHtmlEvaluatorKey, EvaluationFigure>> get() {
        
        
        //Get Map passed in from collector
        Map<WebsiteHtmlCollectorKey, List<String>> collected =
                this.collected();
        
        /* Add Map for link-evluation figures with result from the
         * evaluateLinks() method. Pass "Links" from collected-list
         * to full evaluation-result-Map.
         */
        this.result.put(WebsiteHtmlEvaluatorKey.LINK_EVAL_RESULTS , 
                      rateLinks(collected.get(WebsiteHtmlCollectorKey.LINKS)));
        
        return this.result;
    }
    
    /**
     * Calculates the rating for the Links
     * @returns a map with all the evaluation results for the Links
     *      which is later integrated in the full evaluation list
     */
    private Map<WebsiteHtmlEvaluatorKey, String> evaluateLinks(List<String> links){
        
        Map<WebsiteHtmlEvaluatorKey, String> results = 
                                new HashMap<WebsiteHtmlEvaluatorKey, Rating>();
        
        /* Rate the amount of links by percentual divergence
         * Assume that an amount around 17 would be perfect
         */
        final int LINKS_AMOUNT_IDEAL = 17;
        int linkAmount = links.size();
        float linkAmountdiv = Math.abs(percentualDivergence(LINKS_AMOUNT_IDEAL, linkAmount));
        reults.put(WebsiteHtmlEvaluatorKey.LINK_AMOUNT, linkAmount);
        Rating linkAmountRating;
        if (linkAmountdiv > 200) linkAmountRating = Rating.POOR;
        else if (linkAmountdiv > 150) linkAmountRating = Rating.TENUOUS;
        else if (linkAmountdiv > 100) linkAmountRating = Rating.OK;
        else if (linkAmountdiv > 50) linkAmountRating = Rating.GOOD;
        else linkAmountRating = Rating.EXCELLENT;        
        results.put(WebsiteHtmlEvaluatorKey.LINK_AMOUNT_RATING, linkAmountRating);
        
        return results;
        
    }

}
