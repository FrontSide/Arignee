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
import collectors.AbstractCollector;
import collectors.CollectorValue;
import collectors.WebsiteHtmlCollector;
import collectors.WebsiteHtmlCollectorFactory;

import java.util.List;
import play.Logger;

public class HtmlLinkEvaluator extends AbstractSubEvaluator {

    private final int LINKS_AMOUNT_IDEAL = 17; //assumption
    private final float BACKLINK_RATIO_IDEAL = 100; //assumption //WRONG!! Distiction of WHICH page links back is needed !!!!

    private int linkAmount;

    private List<String> linktexts;
    private List<String> linkhrefs;
    private String url;

    public HtmlLinkEvaluator(){}    
    public HtmlLinkEvaluator(CollectorValue linktexts, CollectorValue linkhrefs, CollectorValue url) {
        this.setLinkTexts(linktexts);
        this.setLinkHrefs(linkhrefs);
        this.setUrl(url);
    }
    
    private void setLinkTexts(CollectorValue linktexts) {
        if (linktexts.getValue() instanceof List) this.linktexts = (List<String>) linktexts.getValue();
        else throw new IllegalArgumentException("\"linktexts\" must be an instance of List<String>");
    }
    
    private void setLinkHrefs(CollectorValue linkhrefs) {
        if (linkhrefs.getValue() instanceof List) this.linkhrefs = (List<String>) linkhrefs.getValue();
        else throw new IllegalArgumentException("\"linkhrefs\" must be an instance of List<String>");
    }
    
    private void setUrl(CollectorValue url) {
        if (url.getValue() instanceof String) this.url = (String) url.getValue();
        else throw new IllegalArgumentException("\"url\" must be an instance of String");
    }
    
    public void pass(CollectorValue linktexts, CollectorValue linkhrefs, CollectorValue url) {
        this.setLinkTexts(linktexts);
        this.setLinkHrefs(linkhrefs);
        this.setUrl(url);
    }

    /**
     * Calculates the rating for the Links
     * @returns a map with all the evaluation results for the Links
     *      which is later integrated in the full evaluation list
     */
    @Override
    public EvaluationResult get() {
    
        if (this.linkhrefs == null || this.linktexts == null || this.url == null)
            throw new IllegalStateException("Some variables are missing, try pass(...)");
            
        this.result = new EvaluationResultContainer();
        this.linkAmount = this.linktexts.size();
        
        //Call concrete Link-Evaluation Methods
        this.result.add(WebsiteHtmlEvaluatorKey.AMOUNT, rateLinkAmount());
        this.result.add(WebsiteHtmlEvaluatorKey.BACKLINK_RATIO, rateBackLinkRatio());
        
        return this.result;
        
    }
    
    /** 
      * Rate the amount of links by percentual divergence
      * @param linkAmount : amount of Links on the website
      * @returns : An EvaluationResult for the LinkAmount
      */
    private EvaluationResult rateLinkAmount() {
        
        Logger.info("Start linkAmount-Rating...");
        
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
      * Rates the Ratio of the Backlinks from the Pages that are linked to
      * from the evaluated website (this.url)
      * @returns : EvaluationResult for the External-Link-Ratio
      */      
    /*** WRONG !!! PAGE OF BACKLINK DISTINCTION NEEDED !!! ***/
    /*** ONE ORE MORE BACKLINKS PER LINKED-TO PAGE NEEDED !!!! */
    private EvaluationResult rateBackLinkRatio() {
        
        Logger.info("Start backLinkRatio-Rating...");
        
        int numOfBacklinks = 0;
        
        final WebsiteHtmlCollectorFactory COLLECTORFACTORY = 
                                WebsiteHtmlCollectorFactory.getInstance();
                                
        //Go through all HREFS on the website to evaluate
        for (String h : this.linkhrefs) {
            
            //First check if this link just refers to its own page
            //skip this iteration if so!
            if (AbstractCollector.isSameUrl(this.url, h)) continue;
            
            collectors.Collector collector = COLLECTORFACTORY.create();
        
            boolean isInternalLink = false;
            if (AbstractCollector.isPath(h)) {
                Logger.debug("\"" + h + "\" is a Path");
                h = AbstractCollector.trimToBaseUrl(this.url) + h;
                Logger.debug("URL to request has been assembled to \"" + h + "\"");
                isInternalLink = true;
            }
        
            else isInternalLink = AbstractCollector.isInternalUrl(this.url, h);

            if (isInternalLink) Logger.debug("\"" + h + "\" is an internal Link");

            CollectorValue targetWebsiteLinkHrefs = new CollectorValue();
            try {
                collector = collector.url(h).fetch();
                //Get all the hrefs of the Links from the linked-to page
                targetWebsiteLinkHrefs.add((
                            (WebsiteHtmlCollector) collector).getLinkHrefs());
            } catch (RuntimeException e) {
                Logger.error("Failed to fetch hrefs for \"" + h + "\"");
                continue; //We skip the rest of this iteration
            }
            
            int numOfBacklinksBefore = numOfBacklinks;
            //Go through all the HREFS on the Link's target Website                
            for (String th :(List<String>) targetWebsiteLinkHrefs.getList()) {
                if (AbstractCollector.isSameUrl(this.url, th)) numOfBacklinks++;
            }
            
            Logger.info("The URL \"" + h + "\" has " + (numOfBacklinks-numOfBacklinksBefore) + " backlinks");
            
        }
        
        /*** WRONG !!! PAGE OF BACKLINK DISTINCTION NEEDED !!! ***/
        /*** ONE ORE MORE BACKLINKS PER LINKED-TO PAGE NEEDED !!!! */
                
        float backlinkRatio = AbstractEvaluator.percentualDivergence(this.linkAmount, numOfBacklinks);
        
        EvaluationResultContainer backlinkRatioResults = new EvaluationResultContainer();
                                            
        backlinkRatioResults.add(WebsiteHtmlEvaluatorKey.ACTUAL, 
                                    new EvaluationFigure(backlinkRatio));
        backlinkRatioResults.add(WebsiteHtmlEvaluatorKey.IDEAL, 
                                    new EvaluationFigure(BACKLINK_RATIO_IDEAL));
        backlinkRatioResults.add(WebsiteHtmlEvaluatorKey.DIV, 
                                    new EvaluationFigure(Math.abs(BACKLINK_RATIO_IDEAL-backlinkRatio)));
        
        Rating backlinkRatioRating;
        
        if (backlinkRatio < BACKLINK_RATIO_IDEAL*0.1) backlinkRatioRating = Rating.POOR;
        else if (backlinkRatio < BACKLINK_RATIO_IDEAL*0.3) backlinkRatioRating = Rating.TENUOUS;
        else if (backlinkRatio < BACKLINK_RATIO_IDEAL*0.6) backlinkRatioRating = Rating.OK;
        else if (backlinkRatio < BACKLINK_RATIO_IDEAL*0.9) backlinkRatioRating = Rating.GOOD;
        else backlinkRatioRating = Rating.EXCELLENT;
        
        backlinkRatioResults.add(WebsiteHtmlEvaluatorKey.RATING,
                                     new EvaluationFigure(backlinkRatioRating));
                                     
        return backlinkRatioResults;
        
    }
    
}
