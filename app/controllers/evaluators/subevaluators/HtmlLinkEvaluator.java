package evaluators.subevaluators;

/**
  * Used by WebsiteHtmlEvaluator
  */

import evaluators.AbstractEvaluator;
import evaluators.EvaluationResult;
import evaluators.EvaluationResultContainer;
import evaluators.EvaluationResultContainer.*;
import evaluators.EvaluationFigure;
import evaluators.enums.WebsiteHtmlEvaluatorKey;
import evaluators.enums.Rating;
import java.util.List;

public class HtmlLinkEvaluator extends AbstractSubEvaluator {

    private List<String> links;

    public HtmlLinkEvaluator(){}    
    public HtmlLinkEvaluator(List<String> links) {
        this.links = links;
    }
    
    public void pass(List<String> links) {
        if (this.links != null)
            throw new IllegalArgumentException("There already seems to ba a Links-List!" + 
                                " You may not overwrite the existing one!");
        this.links = links;
    }

    /**
     * Calculates the rating for the Links
     * @returns a map with all the evaluation results for the Links
     *      which is later integrated in the full evaluation list
     */
    public EvaluationResult get() {
    
        if (this.links == null)
            throw new IllegalStateException("There is no Links-List."+
                                            " Use the pass(...) method");
            
        this.result = new EvaluationResultContainer();
        
        //Call concrete Link-Evaluation Methods
        rateLinkAmount(this.links.size());
        
        return this.result;
        
    }
    
    private void rateLinkAmount(int linkAmount) {
        
        /* Rate the amount of links by percentual divergence
         * Assume that an amount around 17 would be perfect
         */
        final int LINKS_AMOUNT_IDEAL = 17;
        float linkAmountDiv = AbstractEvaluator.percentualDivergence(
                                    LINKS_AMOUNT_IDEAL, 
                                    Math.abs(LINKS_AMOUNT_IDEAL-linkAmount));
                                            
        this.result.add(WebsiteHtmlEvaluatorKey.LINK_AMOUNT, 
                        (EvaluationResult) new EvaluationFigure(linkAmount));        
        this.result.add(WebsiteHtmlEvaluatorKey.LINK_AMOUNT_IDEAL, 
                                            new EvaluationFigure(LINKS_AMOUNT_IDEAL));
        this.result.add(WebsiteHtmlEvaluatorKey.LINK_AMOUNT_DIV, 
                                            new EvaluationFigure(linkAmountDiv));
        Rating linkAmountRating;
        
        if (linkAmountDiv > 200) linkAmountRating = Rating.POOR;
        else if (linkAmountDiv > 150) linkAmountRating = Rating.TENUOUS;
        else if (linkAmountDiv > 100) linkAmountRating = Rating.OK;
        else if (linkAmountDiv > 50) linkAmountRating = Rating.GOOD;
        else linkAmountRating = Rating.EXCELLENT;
        
        this.result.add(WebsiteHtmlEvaluatorKey.LINK_AMOUNT_RATING, 
                                    new EvaluationFigure(linkAmountRating));
        
    }

}
