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

    private BacklinkEvaluationStrategy strategy;

    public BacklinkEvaluator(EBacklinkEvaluationStrategy strategy) {
        setStrategy(strategy);
    }

    public BacklinkEvaluator(WebPage webPage, List<Hyperlink> links, EBacklinkEvaluationStrategy strategy) {
        pass(webPage, links);
        setStrategy(strategy);
    }

    private void setStrategy(EBacklinkEvaluationStrategy strategy) {

        if (strategy == EBacklinkEvaluationStrategy.EXTERNAL)
            this.strategy = new ExternalBacklinkEvaluation();
        else
            this.strategy = new InternalBacklinkEvaluation();
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
        return getRating(getNumberOfPagesWithBacklink(hyperlinks));

    }

  /**
    * Measures how many of the links in a given list point to a page
    * which points back to the source-page/url
    *
    * Also crates (and adds to the Map) a List with all the WebPages
    * that the source page links to and that have a Link pointing back
    * (i.e. have a backlink)
    *
    * @param  links List of links on the source page that are to be evaluated
    * @return       Evaluation result as EvaluationValue
    */
    private int getNumberOfPagesWithBacklink(List<Hyperlink> links) {

        logger.info("starting backLinkRatio-Rating...");

        final WebsiteHtmlCollectorFactory COLLECTORFACTORY =
                        WebsiteHtmlCollectorFactory.getInstance();

        return strategy.getNumberOfPagesWithBacklink(this.webPage, links, COLLECTORFACTORY);

    }

    /**
     * Rates the number of backlinks
     * @param  numberOfPagesWithBacklink
     * @return  EvaluationValue with Rating and add. info
     */
    private EvaluationValue getRating(int numberOfPagesWithBacklink) {

        float backlinkRatio = AbstractEvaluator.percentualDivergence(this.linkAmount, numberOfPagesWithBacklink);
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
