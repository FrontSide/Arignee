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
import collectors.CollectorValue;
import java.util.List;

public class HtmlLinkEvaluator extends AbstractSubEvaluator {

    private final int LINKS_AMOUNT_IDEAL = 17; //assumption

    private List<String> links;
    private String url;

    public HtmlLinkEvaluator(){}    
    public HtmlLinkEvaluator(CollectorValue links, CollectorValue url) {
        this.setLinks(links);
        this.setUrl(url);
    }
    
    private void setLinks(CollectorValue links) {
        if (links.getValue() instanceof List) this.links = (List<String>) links.getValue();
        else throw new IllegalArgumentException("\"links\" must be an instance of List<String>");
    }
    
    private void setUrl(CollectorValue url) {
        if (url.getValue() instanceof String) this.url = (String) url.getValue();
        else throw new IllegalArgumentException("\"url\" must be an instance of String");
    }
    
    public void pass(CollectorValue links, CollectorValue url) {
        if (this.links != null)
            throw new IllegalArgumentException("There already seems to ba a Links-List!" + 
                                " You may not overwrite the existing one!");
        this.setLinks(links);
        
        if (this.url != null)
            throw new IllegalArgumentException("There already seems to ba a URL!" + 
                                " You may not overwrite the existing one!");
        this.setUrl(url);
    }

    /**
     * Calculates the rating for the Links
     * @returns a map with all the evaluation results for the Links
     *      which is later integrated in the full evaluation list
     */
    @Override
    public EvaluationResult get() {
    
        if (this.links == null)
            throw new IllegalStateException("There is no Links-List."+
                                            " Use the pass(...) method");
            
        this.result = new EvaluationResultContainer();
        
        //Call concrete Link-Evaluation Methods
        this.result.add(WebsiteHtmlEvaluatorKey.AMOUNT,
                                            rateLinkAmount(this.links.size()));
        
        return this.result;
        
    }
    
    /** 
      * Rate the amount of links by percentual divergence
      * @param linkAmount : amount of Links on the website
      * @returns : An EvaluationResult for the LinkAmount
      */
    private EvaluationResult rateLinkAmount(int linkAmount) {
        
        EvaluationResultContainer linkAmountResults = new EvaluationResultContainer();
    
        float linkAmountDiv = AbstractEvaluator.percentualDivergence(
                                    LINKS_AMOUNT_IDEAL, 
                                    Math.abs(LINKS_AMOUNT_IDEAL-linkAmount));
                                            
        linkAmountResults.add(WebsiteHtmlEvaluatorKey.ACTUAL, 
                                    new EvaluationFigure(linkAmount));
        linkAmountResults.add(WebsiteHtmlEvaluatorKey.IDEAL, 
                                    new EvaluationFigure(LINKS_AMOUNT_IDEAL));
        linkAmountResults.add(WebsiteHtmlEvaluatorKey.DIV, 
                                    new EvaluationFigure(linkAmountDiv));
        Rating linkAmountRating;
        
        if (linkAmountDiv > 200 || linkAmount < 4) linkAmountRating = Rating.POOR;
        else if (linkAmountDiv > 150) linkAmountRating = Rating.TENUOUS;
        else if (linkAmountDiv > 100) linkAmountRating = Rating.OK;
        else if (linkAmountDiv > 50) linkAmountRating = Rating.GOOD;
        else linkAmountRating = Rating.EXCELLENT;
        
        linkAmountResults.add(WebsiteHtmlEvaluatorKey.RATING, 
                                    new EvaluationFigure(linkAmountRating));
                                    
        return linkAmountResults;
        
    }
    
    /**
      * Rates the Ratio of ExternalLinks to InternalLinks
      * @returns : EvaluationResult for the External-Link-Ratio
      */
    private EvaluationResult rateExternalLinkRatio() {
        return null;
    }
    
}
