
package controllers;

/**
 * This class parses and renders the responses from the WebsiteHtmlCollector
 * which fetches the HTML content from Websites
 * The html is parsed with the Jsoup library http://jsoup.org/
 */

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import play.mvc.*;
import play.Logger;
import play.Logger.ALogger;

import org.json.JSONObject;

import collectors.WebsiteHtmlCollectorFactory;
import collectors.WebsiteHtmlCollector;
import collectors.enums.CollectorKey;
import collectors.enums.WebsiteHtmlCollectorKey;
import evaluators.enums.EvaluatorKey;
import evaluators.enums.WebsiteHtmlEvaluatorKey;
import models.collection.CollectorValue;
import models.evaluation.EvaluationValueFigure;
import models.evaluation.EvaluationValue;
import models.evaluation.EvaluationValueContainer;
import models.evaluation.EvaluationValueContainer.*;
import models.persistency.EvaluationResult;
import models.persistency.WebPage;
import daos.WebPageDAO;
import daos.EvaluationResultDAO;
import evaluators.EvaluatorFactory;
import ticketing.TicketHandler;
import ticketing.TicketStatus;
import ticketing.TicketProcessor;

public class WebsiteHtmlController extends Controller {

    private static final ALogger logger = Logger.of(WebsiteHtmlController.class);

    private static final WebsiteHtmlCollectorFactory COLLECTORFACTORY =
                                WebsiteHtmlCollectorFactory.getInstance();

    private static final EvaluatorFactory EVALUATORFACTORY =
                                EvaluatorFactory.getInstance();

    private static final TicketHandler TICKETHANDLER =
                                TicketHandler.getInstance();

    /**
     * Full Website Evaluation regarding its HTML content
     * Triggers Collector and passes collected Data to Evaluator
     * Also triggers the persistor to record the evaluation result in the DB
     * @param  String URL of Webpage to be Evaluated
     * @return        JSONObject with the full evaluation Result
     */
    public static JSONObject evaluate(final String URL, final String TICKETNUMBER) {

        logger.debug("invoking collector for URL :: " + URL + " ...");
        WebsiteHtmlController.TICKETHANDLER.updateStatus(TICKETNUMBER, TicketStatus.STARTING);

        /* Create Collector and pass ticket-number */
        collectors.Collector collector =
                    (collectors.Collector) WebsiteHtmlController.COLLECTORFACTORY.create();
        ((TicketProcessor) collector).setTicketNumber(TICKETNUMBER);

        /* Fetch extracted data */
        Map<? extends CollectorKey, CollectorValue> collectedData = collector.url(URL).get();

        /* Assembling WebPage object from Collected data */
        WebPage webPage = (WebPage) collectedData.get(WebsiteHtmlCollectorKey.WEBPAGE).getValue();

        /* Create Evaluator, pass data from Collector and obtain eval. results */
        logger.debug("creating evaluator and passing collected data...");
        evaluators.Evaluator evaluator =
                        WebsiteHtmlController.EVALUATORFACTORY.create();
        ((TicketProcessor) evaluator).setTicketNumber(TICKETNUMBER);

        /* FetchEvaluation Result  */
        EvaluationValue evalResult = evaluator.pass(collectedData).get();

        /* Persist Evaluation Result associated to WebPage */
        WebsiteHtmlController.persistEvaluation(evalResult.getLightweightValue(WebsiteHtmlEvaluatorKey.ADDITIONAL), webPage, TICKETNUMBER);

        JSONObject evalResultJson = evalResult.toJson();


        WebsiteHtmlController.TICKETHANDLER.updateStatus(TICKETNUMBER, TicketStatus.EVALUATION_FINISHED);

        return evalResultJson;

    }

    /**
     * Records the Evaluation Result in the DB and connects it to the URL
     * Also persists the URL as WebPage in the DB if not already existing
     * @param  EVALVAL      The result of the evaluation process as JsonObject
     * @param  WEBPAGE      page that has been evaluated
     * @param  TICKETNUMBER The tiketnumber this Evaluation has been requested with
     */
    private static void persistEvaluation(final JSONObject EVALVAL, final WebPage WEBPAGE, final String TICKETNUMBER) {

        EvaluationResult evaluationResult = new EvaluationResult(TICKETNUMBER);
        evaluationResult.setResult(EVALVAL);

        new WebPageDAO().addEvaluationResult(WEBPAGE, evaluationResult);

    }

    private static void persistEvaluation(final EvaluationValue EVALVAL, final WebPage WEBPAGE, final String TICKETNUMBER) {
        WebsiteHtmlController.persistEvaluation(EVALVAL.toJson(), WEBPAGE, TICKETNUMBER);
    }

}
