package evaluators;

/**
* Conducts the Search-Engine-Optimization Evaluations
*/

import java.net.URL;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import collectors.enums.CollectorKey;
import collectors.enums.WebsiteHtmlCollectorKey;
import evaluators.enums.Rating;
import evaluators.enums.EvaluatorKey;
import evaluators.subevaluators.LinkAmountEvaluator;
import evaluators.subevaluators.seo.BacklinkEvaluator;
import evaluators.subevaluators.seo.EBacklinkEvaluationStrategy;
import models.persistency.Hyperlink;
import models.persistency.WebPage;
import models.collection.CollectorValue;
import models.collection.CollectorValue;
import models.evaluation.EvaluationValue;
import models.evaluation.EvaluationValueFigure;
import models.evaluation.EvaluationValueContainer;
import ticketing.TicketStatus;

import play.Logger;
import play.Logger.ALogger;

public class PerformanceEvaluator extends AbstractEvaluator {


    private static final ALogger logger = Logger.of(PerformanceEvaluator.class);

    private long IDEAL_LANDING_PAGE_RESPONSE_TIME = 500; //ms

    public EvaluationValue get() {

        this.result = new EvaluationValueContainer();

        long landingPageResponseTime = Long.parseLong(
                                        (String) collected()
                                        .get(WebsiteHtmlCollectorKey.RESPONSE_TIME)
                                        .getValue());

        this.updateTicketStatus(TicketStatus.LANDING_PAGE_RESPONSE_EVAL);
        this.result.add(EvaluatorKey.LANDING_PAGE_RESPONSE_TIME,
                        getResponseTimeRating(IDEAL_LANDING_PAGE_RESPONSE_TIME,
                        landingPageResponseTime));

        return this.result;
    }

    private EvaluationValue getResponseTimeRating(final long IDEAL_TIME, long actualTime) {

        logger.debug("getResponseTimeRating() actualTime :: " + actualTime);

        if (actualTime < 1L) actualTime = 1L;

        float divergence = AbstractEvaluator.percentualDivergence(IDEAL_TIME, actualTime);

        logger.debug("divergence is :: " + divergence);

        EvaluationValueContainer responseTimeResults = new EvaluationValueContainer();

        responseTimeResults.add(EvaluatorKey.ACTUAL,
                new EvaluationValueFigure((int) actualTime));
        responseTimeResults.add(EvaluatorKey.IDEAL,
                new EvaluationValueFigure((int) IDEAL_TIME));
        responseTimeResults.add(EvaluatorKey.DIV,
                new EvaluationValueFigure(divergence));


        Rating responseTimeRating;

        if (divergence > 900) responseTimeRating = Rating.POOR;
        else if (divergence > 400) responseTimeRating = Rating.TENUOUS;
        else if (divergence > 100) responseTimeRating = Rating.OK;
        else if (divergence > 50) responseTimeRating = Rating.GOOD;
        else responseTimeRating = Rating.EXCELLENT;

        logger.debug("getResponseTimeRating() responseTimeRating :: " + responseTimeRating);

        responseTimeResults.add(EvaluatorKey.RATING,
                new EvaluationValueFigure(responseTimeRating));

        return responseTimeResults;

    }


}
