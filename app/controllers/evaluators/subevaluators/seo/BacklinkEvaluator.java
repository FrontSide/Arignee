package evaluators.subevaluators.seo;

/**
 *
 */

import models.persistency.*;
import models.evaluation.*;
import evaluators.Evaluator;
import evaluators.enums.EvaluatorKey;
import evaluators.enums.Rating;
import evaluators.AbstractEvaluator;
import evaluators.subevaluators.AbstractSubEvaluator;
import collectors.WebsiteHtmlCollectorFactory;
import collectors.WebsiteHtmlCollector;
import url.URLHandler;

import java.util.List;
import java.util.ArrayList;

import play.Logger;
import play.Logger.ALogger;

public class BacklinkEvaluator extends AbstractSubEvaluator<WebPage> {

    private static final ALogger logger = Logger.of(BacklinkEvaluator.class);

    private WebPage webPage;
    private List<Hyperlink> hyperlinks;
    private int linkAmount = -1;

    public BacklinkEvaluator() {}

    public BacklinkEvaluator(WebPage webPage, List<Hyperlink> links) {
        pass(webPage, links);
    }

    public Evaluator pass(WebPage webPage) {
        this.webPage = webPage;
        this.hyperlinks = webPage.hyperlinks;
        this.linkAmount = webPage.hyperlinks.size();
        return this;
    }

    public Evaluator pass(WebPage webPage, List<Hyperlink> links) {
        this.webPage = webPage;
        this.hyperlinks = links;
        this.linkAmount = links.size();
        return this;
    }

    public EvaluationValue get() {

        if (this.webPage == null || this.hyperlinks == null || this.linkAmount == -1)
            throw new IllegalStateException("Parameters missing, try pass(...)");

        logger.debug("BackLinkEvaluator get()");
        this.result = rateBackLinkRatio(hyperlinks);

        return this.result;

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
    private EvaluationValue rateBackLinkRatio(List<Hyperlink> links) {

        logger.info("starting backLinkRatio-Rating...");

        //List with all WebPages that are linked to from the source-Page
        List<WebPage> pagesLinkedTo = new ArrayList<>();

        //List with all Pages that have a backlink to the source-page
        List<WebPage> pagesWithBacklinks = new ArrayList<>();

        final WebsiteHtmlCollectorFactory COLLECTORFACTORY =
                        WebsiteHtmlCollectorFactory.getInstance();

        //Go through all HREFS on the website to evaluate
        for (Hyperlink h : links) {

            String href = h.href;

            logger.info("browsing URL for backlinks :: " + href);

            //First check if this link just refers to its own page
            if (URLHandler.isARecursiveLink(this.webPage, href)) continue;

            collectors.Collector collector = COLLECTORFACTORY.create();

            /* Check if href is an url-appendix (parameter, path, #)
            * If so, build a complete url with the url of the eval-WebPage */
            if (URLHandler.isUrlAppendix(href)) {
                href = (href.startsWith("/")) ? href : "/" + href;
                href = this.webPage.url.getHost() + href;
            }

            WebPage targetWebsite = new WebPage(href);
            logger.debug("done creating.");
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

            //Go through all the hrefs on the Link's target Website
            for (Hyperlink twh : targetWebsiteLinks) {
                if (URLHandler.isARecursiveLink(this.webPage, twh.href)) {
                    logger.info("URL was identified as backlink :: " + twh.href);
                    pagesWithBacklinks.add(targetWebsite);
                    break; //break if backlink detected
                }
            }

            logger.info("The URL \"" + h + "\" has >= 1 backlinks");

        }

        float backlinkRatio = AbstractEvaluator.percentualDivergence(this.linkAmount, pagesWithBacklinks.size());
        final int BACKLINK_RATIO_IDEAL = 100; //%, 100 -> every page links back at least once

        EvaluationValueContainer backlinkRatioResults = new EvaluationValueContainer();

        backlinkRatioResults.add(EvaluatorKey.ACTUAL,
                    new EvaluationValueFigure(backlinkRatio));
        backlinkRatioResults.add(EvaluatorKey.IDEAL,
                    new EvaluationValueFigure(BACKLINK_RATIO_IDEAL));
        backlinkRatioResults.add(EvaluatorKey.DIV,
                    new EvaluationValueFigure(Math.abs(BACKLINK_RATIO_IDEAL-backlinkRatio)));


        Rating backlinkRatioRating;

        if (backlinkRatio < BACKLINK_RATIO_IDEAL*0.1) backlinkRatioRating = Rating.POOR;
        else if (backlinkRatio < BACKLINK_RATIO_IDEAL*0.3) backlinkRatioRating = Rating.TENUOUS;
        else if (backlinkRatio < BACKLINK_RATIO_IDEAL*0.6) backlinkRatioRating = Rating.OK;
        else if (backlinkRatio < BACKLINK_RATIO_IDEAL*0.9) backlinkRatioRating = Rating.GOOD;
        else backlinkRatioRating = Rating.EXCELLENT;

        backlinkRatioResults.add(EvaluatorKey.RATING,
        new EvaluationValueFigure(backlinkRatioRating));

        return backlinkRatioResults;

    }


}
