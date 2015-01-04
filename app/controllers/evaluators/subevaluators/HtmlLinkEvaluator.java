package evaluators.subevaluators;

/**
  * Used by WebsiteHtmlEvaluator
  */

import java.util.List;
import java.util.ArrayList;

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
import models.persistency.WebPage;
import models.collection.CollectorValue;

import play.Logger;
import play.Logger.ALogger;

public class HtmlLinkEvaluator extends AbstractSubEvaluator {

    private static final ALogger logger = Logger.of(HtmlLinkEvaluator.class);

    private final int LINKS_AMOUNT_IDEAL = 50; //assumption

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
        this.result.add(WebsiteHtmlEvaluatorKey.INTERNAL_BACKLINK_RATIO,
                                    rateBackLinkRatio(getAllInternalLinks()));
        this.result.add(WebsiteHtmlEvaluatorKey.EXTERNAL_BACKLINK_RATIO,
                                    rateBackLinkRatio(getAllExternalLinks()));

        return this.result;

    }

    private List<Hyperlink> internalLinks;
    private List<Hyperlink> externalLinks;

    /**
     * Filters all the Hyperlinks from the full Hyperlink-List
     * of the source page that can be identified as Domain-Internal-Links and
     * puts those in the global "internalLinks"-List and the rest in the
     * "externalLinks"-List
     */
    private void seperateInternalFromExternalLinks() {
        this.internalLinks = new ArrayList<>();
        this.externalLinks = new ArrayList<>();
        for (Hyperlink h : this.hyperlinks) {
            if (AbstractCollector.isInternalUrl(this.url, h.target))
                    this.internalLinks.add(h);
            else    this.externalLinks.add(h);
        }
    }

    private List<Hyperlink> getAllInternalLinks() {
        if (this.internalLinks == null) seperateInternalFromExternalLinks();
        return this.internalLinks;
    }

    private List<Hyperlink> getAllExternalLinks() {
        if (this.externalLinks == null) seperateInternalFromExternalLinks();
        return this.externalLinks;
    }

    /**
      * Rate the amount of links by percentual divergence
      * @param linkAmount : amount of Links on the website
      * @returns : An EvaluationResult for the LinkAmount
      */
    private EvaluationValue rateLinkAmount() {

        logger.info("starting linkAmount-Rating...");

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

        EvaluationValueContainer additionals = new EvaluationValueContainer();
        additionals.add(WebsiteHtmlEvaluatorKey.FOUND_URLS,
                                    new EvaluationValueFigure(Hyperlink.getUrls(this.hyperlinks)));
        linkAmountResults.add(WebsiteHtmlEvaluatorKey.ADDITIONAL, additionals);

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
     * Measures how many of the links in a given list point to a page
     * which points back to the source-page/url and rates this value
     *
     * Also crates (and adds to the Map) a List with all the WebPages
     * that the source page links to and that have a Link pointing back
     * (i.e. have a backlink)
     *
     * @param  links List of links on the source page that are to be evaluated
     * @return       Evaluation result as EvaluationValue
     */
     /*** WRONG !!! PAGE OF BACKLINK DISTINCTION NEEDED !!! ***/
     /*** ONE ORE MORE BACKLINKS PER LINKED-TO PAGE NEEDED !!!! */
    private EvaluationValue rateBackLinkRatio(List<Hyperlink> links) {

        logger.info("starting backLinkRatio-Rating...");

        int numOfBacklinks = 0;

        //List with all WebPages that are linked to from the source-Page
        List<WebPage> pagesLinkedTo = new ArrayList<>();

        //List with all Pages that have a backlink to the source-page
        List<WebPage> pagesWithBacklinks = new ArrayList<>();

        final WebsiteHtmlCollectorFactory COLLECTORFACTORY =
                                WebsiteHtmlCollectorFactory.getInstance();

        //Go through all HREFS on the website to evaluate
        for (Hyperlink h : links) {

            String href = h.target;

            logger.info("browsing URL for backlinks :: " + href);

            //First check if this link just refers to its own page
            if (AbstractCollector.pointsToSameWebpage(this.url, href)) {
                logger.info("recursive URL encountered :: " + href);
                logger.warn("skipping iteration...");
                continue;
            }

            collectors.Collector collector = COLLECTORFACTORY.create();

            /* Check if href is an url-appendix (parameter, path, #)
             * If so, build a complete url with the url of the eval-WebPage */
            if (AbstractCollector.isUrlAppendix(href))
                href = AbstractCollector.trimToBaseUrl(this.url) + href;

            WebPage targetWebsite = new WebPage(href);
            pagesLinkedTo.add(targetWebsite);

            List<Hyperlink> targetWebsiteLinks = null;

            try {
                //Get all the hrefs of the Links from the linked-to page
                targetWebsiteLinks =
                        ((WebsiteHtmlCollector) collector.url(href).fetch())
                        .getHyperlinks();

                if (targetWebsiteLinks == null) continue; //Skip if no links
            } catch (RuntimeException e) {
                logger.error("Failed to fetch hrefs for \"" + href + "\"");
                logger.warn("skipping iteration...");
                continue;
            }

            targetWebsite.hyperlinks = targetWebsiteLinks;
            int numOfBacklinksBefore = numOfBacklinks;

            //Go through all the hrefs on the Link's target Website
            for (Hyperlink twh : targetWebsiteLinks) {
                if (AbstractCollector.pointsToSameWebpage(this.url, twh.target)) {
                    logger.info("URL was identified as backlink :: " + twh.target);
                    numOfBacklinks++;
                }
            }

            int currentPageBacklinks = numOfBacklinks - numOfBacklinksBefore;

            //Add webPage to list of webpage with backlinks if has backlinks
            if (currentPageBacklinks > 0) {
                pagesWithBacklinks.add(targetWebsite);
            }


            logger.info("The URL \"" + h + "\" has :: "
                                + currentPageBacklinks + " backlinks");

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

        /* Add additional info */
        EvaluationValueContainer additionals = new EvaluationValueContainer();

        additionals.add(WebsiteHtmlEvaluatorKey.FOUND_URLS,
                                    new EvaluationValueFigure(WebPage.getUrls(pagesWithBacklinks)));

        backlinkRatioResults.add(WebsiteHtmlEvaluatorKey.ADDITIONAL, additionals);

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
