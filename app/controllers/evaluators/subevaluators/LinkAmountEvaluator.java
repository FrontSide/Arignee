package evaluators.subevaluators;

/**
  * Used by WebsiteHtmlEvaluator
  */

import java.net.URL;
import java.util.List;
import java.util.ArrayList;

import ticketing.TicketStatus;
import evaluators.Evaluator;
import evaluators.AbstractEvaluator;
import evaluators.enums.EvaluatorKey;
import evaluators.enums.Rating;
import url.URLHandler;
import collectors.WebsiteHtmlCollector;
import collectors.WebsiteHtmlCollectorFactory;
import models.evaluation.EvaluationValue;
import models.evaluation.EvaluationValueFigure;
import models.evaluation.EvaluationValueContainer;
import models.evaluation.EvaluationValueContainer.*;
import models.persistency.Hyperlink;
import models.persistency.WebPage;
import models.collection.CollectorValue;

import play.Logger;
import play.Logger.ALogger;

public class LinkAmountEvaluator extends AbstractSubEvaluator<Integer> {

    private static final ALogger logger = Logger.of(LinkAmountEvaluator.class);

    private final int LINKS_AMOUNT_IDEAL = 50; //assumption

    private int linkAmount = -1;

    public LinkAmountEvaluator(){}
    public LinkAmountEvaluator(Integer linkAmount) {
        this.pass(linkAmount);
    }

    @Override
    public Evaluator pass(Integer linkAmount) {
        this.linkAmount = linkAmount.intValue();
        return this;
    }

    /**
     * Calculates the rating for the Links
     * @returns a map with all the evaluation results for the Links
     *      which is later integrated in the full evaluation list
     */
    @Override
    public EvaluationValue get() {

        if (this.linkAmount == -1)
            throw new IllegalStateException("Link Amount missing, try pass(...)");

        logger.debug("LinkAmountEvaluator get()");
        this.result = rateLinkAmount();

        return this.result;

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
                                    Math.abs(LINKS_AMOUNT_IDEAL-this.linkAmount));

        linkAmountResults.add(EvaluatorKey.ACTUAL,
                                    new EvaluationValueFigure(this.linkAmount));
        linkAmountResults.add(EvaluatorKey.IDEAL,
                                    new EvaluationValueFigure(LINKS_AMOUNT_IDEAL));
        linkAmountResults.add(EvaluatorKey.DIV,
                                    new EvaluationValueFigure(linkAmountDiv));

        Rating linkAmountRating;

        if (linkAmountDiv > 200 || linkAmount < 4) linkAmountRating = Rating.POOR;
        else if (linkAmountDiv > 150) linkAmountRating = Rating.TENUOUS;
        else if (linkAmountDiv > 100) linkAmountRating = Rating.OK;
        else if (linkAmountDiv > 50) linkAmountRating = Rating.GOOD;
        else linkAmountRating = Rating.EXCELLENT;

        linkAmountResults.add(EvaluatorKey.RATING,
                                    new EvaluationValueFigure(linkAmountRating));

        return linkAmountResults;

    }




}
