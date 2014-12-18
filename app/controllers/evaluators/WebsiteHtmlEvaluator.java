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

public class WebsiteHtmlEvaluator implements Evaluator {

    public Map<WebsiteHtmlEvaluatorKey, Map<WebsiteHtmlEvaluatorKey, Rating>> get() {
        Map<WebsiteHtmlCollectorKey, List<String>> collected =
                this.collected();
        
        Map<WebsiteHtmlEvaluatorKey, Rating> linkEvaluations = 
                    rateLinks(collected.get(WebsiteHtmlCollectorKey.LINKS));
    }
    
    /**
     * Calculates the rating for the Links
     * @returns a map with all the evaluation results for the Links
     *      which is later integrated in the full evaluation list
     */
    private Map<WebsiteHtmlEvaluatorKey, Rating> evaluateLinks(List<String> links){
        
        Map<WebsiteHtmlEvaluatorKey, Rating> results = new HashMap<WebsiteHtmlEvaluatorKey, Rating>();
        
        /* Rate the amount of links 
         * Assume that an amount between 15 and 20 would be perfect
         */
        int linkAmount = links.size();
        Rating linkAmountRating;
        if (linkAmount > 50 || linkAmount < 5) linkAmountRating = Rating.POOR;
        else if (linkAmount > 40 || linkAmount < 10) linkAmountRating = Rating.TENUOUS;
        else if (linkAmount > 30 || linkAmount < 15) linkAmountRating = Rating.OK;
        else if (linkAmount > 20) linkAmountRating = Rating.GOOD;
        else linkAmountRating = Rating.EXCELLENT;        
        results.put(WebsiteHtmlEvaluatorKey.LINK_AMOUNT_RATING, linkAmountRating);
        
        return results;
        
    }

}
