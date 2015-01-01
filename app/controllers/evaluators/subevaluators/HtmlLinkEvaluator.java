package evaluators.subevaluators;

/**
  * Used by WebsiteHtmlEvaluator
  */

import models.evaluation.EvaluationValue;
import models.evaluation.EvaluationValueFigure;
import models.evaluation.EvaluationValueContainer;
import models.evaluation.EvaluationValueContainer.*;
import evaluators.AbstractEvaluator;
import evaluators.enums.WebsiteHtmlEvaluatorKey;
import evaluators.enums.Rating;
import collectors.AbstractCollector;
import collectors.WebsiteHtmlCollector;
import collectors.WebsiteHtmlCollectorFactory;
import models.persistency.Hyperlink;
import models.collection.CollectorValue;

import java.util.List;
import play.Logger;

public class HtmlLinkEvaluator extends AbstractSubEvaluator {

    private final int LINKS_AMOUNT_IDEAL = 17; //assumption

    //WRONG!! Distiction of WHICH page links back is needed !!!!
    private final float BACKLINK_RATIO_IDEAL = 100; //assumption
    //WRONG!! Distiction of WHICH page links back is needed !!!!

    private int linkAmount;

    private List<Hyperlink> hyperlinks;
    private String url;

    public HtmlLinkEvaluator(){}
    public HtmlLinkEvaluator(List<Hyperlink> hyperlinks, String url) {
        this.setHyperlinks(hyperlinks);
        this.setUrl(url);
    }

    private void setHyperlinks(List<Hyperlink> hyperlinks) {
        this.hyperlinks = hyperlinks;
    }

    private void setUrl(String url) {
        this.url = url;
    }

    public void pass(List<Hyperlink> hyperlinks, String url) {
        this.setHyperlinks(hyperlinks);
        this.setUrl(url);
    }

    /**
     * Calculates the rating for the Links
     * @returns a map with all the evaluation results for the Links
     *      which is later integrated in the full evaluation list
     */
    @Override
    public EvaluationValue get() {

        if (this.hyperlinks == null)
            throw new IllegalStateException("Hyperlinks are missing, try pass(...)");

        if (this.url == null)
            throw new IllegalStateException("URL is missing, try pass(...)");

        this.result = new EvaluationValueContainer();
        this.linkAmount = this.hyperlinks.size();

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
    private EvaluationValue rateLinkAmount() {

        Logger.info("Start linkAmount-Rating...");

        EvaluationValueContainer linkAmountResults = new EvaluationValueContainer();

        float linkAmountDiv = AbstractEvaluator.percentualDivergence(
                                    LINKS_AMOUNT_IDEAL,
                                    Math.abs(LINKS_AMOUNT_IDEAL-linkAmount));

        linkAmountResults.add(WebsiteHtmlEvaluatorKey.ACTUAL,
                                    new EvaluationValueFigure(linkAmount));
        linkAmountResults.add(WebsiteHtmlEvaluatorKey.IDEAL,
                                    new EvaluationValueFigure(LINKS_AMOUNT_IDEAL));
        linkAmountResults.add(WebsiteHtmlEvaluatorKey.DIV,
                                    new EvaluationValueFigure(linkAmountDiv));
        Rating linkAmountRating;

        if (linkAmountDiv > 200 || linkAmount < 4) linkAmountRating = Rating.POOR;
        else if (linkAmountDiv > 150) linkAmountRating = Rating.TENUOUS;
        else if (linkAmountDiv > 100) linkAmountRating = Rating.OK;
        else if (linkAmountDiv > 50) linkAmountRating = Rating.GOOD;
        else linkAmountRating = Rating.EXCELLENT;

        linkAmountResults.add(WebsiteHtmlEvaluatorKey.RATING,
                                    new EvaluationValueFigure(linkAmountRating));

        return linkAmountResults;

    }

    /**
      * Rates the Ratio of the Backlinks from the Pages that are linked to
      * from the evaluated website (this.url)
      * @returns : EvaluationResult for the External-Link-Ratio
      */
    /*** WRONG !!! PAGE OF BACKLINK DISTINCTION NEEDED !!! ***/
    /*** ONE ORE MORE BACKLINKS PER LINKED-TO PAGE NEEDED !!!! */
    private EvaluationValue rateBackLinkRatio() {

        Logger.info("Start backLinkRatio-Rating...");

        int numOfBacklinks = 0;

        final WebsiteHtmlCollectorFactory COLLECTORFACTORY =
                                WebsiteHtmlCollectorFactory.getInstance();

        //Go through all HREFS on the website to evaluate
        for (Hyperlink h : this.hyperlinks) {

            String href = h.target;

            //First check if this link just refers to its own page
            //skip this iteration if so!
            if (AbstractCollector.pointsToSameWebpage(this.url, href)) continue;

            collectors.Collector collector = COLLECTORFACTORY.create();

            boolean isInternalLink = false;
            /* Check if href is an url-appendix (parameter, path, #)
             * If so, build a complete url with the url of the eval-WebPage */
            if (AbstractCollector.isUrlAppendix(href)) {
                href = AbstractCollector.trimToBaseUrl(this.url) + href;
                isInternalLink = true;
            } else isInternalLink = AbstractCollector.isInternalUrl(this.url, href);

            List<Hyperlink> targetWebsiteLinks = null;

            try {
                //Get all the hrefs of the Links from the linked-to page
                targetWebsiteLinks =
                        ((WebsiteHtmlCollector) collector.url(href).fetch())
                        .getHyperlinks();

                if (targetWebsiteLinks == null) continue; //Skip if no links
            } catch (RuntimeException e) {
                Logger.error("Failed to fetch hrefs for \"" + href + "\"");
                Logger.warn("skipping iteration...");
                continue; //We skip the rest of this iteration
            }

            int numOfBacklinksBefore = numOfBacklinks;

            //Go through all the hrefs on the Link's target Website
            for (Hyperlink twh : targetWebsiteLinks) {
                if (AbstractCollector.pointsToSameWebpage(this.url, twh.target))
                    numOfBacklinks++;
            }

            Logger.info("The URL \"" + h + "\" has " + (numOfBacklinks-numOfBacklinksBefore) + " backlinks");

        }

        /*** WRONG !!! PAGE OF BACKLINK DISTINCTION NEEDED !!! ***/
        /*** ONE ORE MORE BACKLINKS PER LINKED-TO PAGE NEEDED !!!! */

        float backlinkRatio = AbstractEvaluator.percentualDivergence(this.linkAmount, numOfBacklinks);

        EvaluationValueContainer backlinkRatioResults = new EvaluationValueContainer();

        backlinkRatioResults.add(WebsiteHtmlEvaluatorKey.ACTUAL,
                                    new EvaluationValueFigure(backlinkRatio));
        backlinkRatioResults.add(WebsiteHtmlEvaluatorKey.IDEAL,
                                    new EvaluationValueFigure(BACKLINK_RATIO_IDEAL));
        backlinkRatioResults.add(WebsiteHtmlEvaluatorKey.DIV,
                                    new EvaluationValueFigure(Math.abs(BACKLINK_RATIO_IDEAL-backlinkRatio)));

        Rating backlinkRatioRating;

        if (backlinkRatio < BACKLINK_RATIO_IDEAL*0.1) backlinkRatioRating = Rating.POOR;
        else if (backlinkRatio < BACKLINK_RATIO_IDEAL*0.3) backlinkRatioRating = Rating.TENUOUS;
        else if (backlinkRatio < BACKLINK_RATIO_IDEAL*0.6) backlinkRatioRating = Rating.OK;
        else if (backlinkRatio < BACKLINK_RATIO_IDEAL*0.9) backlinkRatioRating = Rating.GOOD;
        else backlinkRatioRating = Rating.EXCELLENT;

        backlinkRatioResults.add(WebsiteHtmlEvaluatorKey.RATING,
                                     new EvaluationValueFigure(backlinkRatioRating));

        return backlinkRatioResults;

    }

}
