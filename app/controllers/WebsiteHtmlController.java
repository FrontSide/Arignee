
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
    public static JSONObject evaluate(final String URL, long ticketNumber) {

        WebsiteHtmlController.TICKETHANDLER.updateStatus(ticketNumber, TicketStatus.STARTING);

        // Create Collector and obtain extracted data
        logger.debug("invoking collector for URL :: " + URL + " ...");
        collectors.Collector collector =
                    (collectors.Collector) WebsiteHtmlController.COLLECTORFACTORY.create();

        ((TicketProcessor) collector).setTicketNumber(ticketNumber);

        Map<? extends CollectorKey, CollectorValue> collectedData = collector.url(URL).get();
        logger.debug("collected data :: " + collectedData);

        //Create Evaluator, pass data from Collector and obtain eval. results
        logger.debug("creating evaluator and passing collected data...");
        evaluators.Evaluator evaluator =
                        WebsiteHtmlController.EVALUATORFACTORY.create();

        ((TicketProcessor) evaluator).setTicketNumber(ticketNumber);

        EvaluationValue evalresult = evaluator.pass(collectedData).get();

        //TODO: Temporarily I directly return the result from the evaluator
        //instead of a combines list/map/jsonobject with data from the
        //collector directly (for showing the user additional info e.g. linktext)
        logger.info("json object returned is :: " + evalresult.toJson());

        JSONObject evalresultJson = evalresult.toJson();
        WebsiteHtmlController.persistEvaluation(evalresultJson, URL);

        WebsiteHtmlController.TICKETHANDLER.updateStatus(ticketNumber, TicketStatus.EVALUATION_FINISHED);

        return evalresultJson;

    }

    /**
     * Records the Evaluation Result in the DB
     * @param  EVALVAL  The result of the evaluation process as JsonObject
     * @param  URL      url of the page that has been evaluated
     */
    private static void persistEvaluation(final JSONObject EVALVAL, final String URL) {

        WebPageDAO webpageDAO = new WebPageDAO();
        EvaluationResult evaluationResult = new EvaluationResult();
        evaluationResult.setResult(EVALVAL);

        webpageDAO.save(new WebPage(URL, evaluationResult));

    }

    private static void persistEvaluation(final EvaluationValue EVALVAL, final String URL) {
        WebsiteHtmlController.persistEvaluation(EVALVAL.toJson(), URL);
    }

}
