
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
import evaluators.EvaluatorFactory;

public class WebsiteHtmlController extends Controller {

    private final WebsiteHtmlCollectorFactory COLLECTORFACTORY =
                                WebsiteHtmlCollectorFactory.getInstance();

    private final EvaluatorFactory EVALUATORFACTORY =
                                EvaluatorFactory.getInstance();

    /**
      * Triggers a FULL WEBSITE EVALUATION
      *
      * Instantiates Collecor and obtains Map with all relevant data from it
      *
      * Passes it to the Evaluator and again obtains a Map, this time with the
      * Evaluation Results
      *
      * @returns a Json Object with all infos that are being shown to the client
      */

    /**
     * Full Website Evaluation regarding its HTML content
     * Triggers Collector and passes collected Data to Evaluator
     * Also triggers the persistor to record the evaluation result in the DB
     * @param  String URL of Webpage to be Evaluated
     * @return        JSONObject with the full evaluation Result
     */
    public static JSONObject evaluate(final String URL) {

        // Create Collector and obtain extracted data
        Logger.debug("Invoke Collector for URL :: \"" + URL + "\"...");
        collectors.Collector collector = this.COLLECTORFACTORY.create();
        Map<? extends CollectorKey, CollectorValue> collectedData = collector.url(URL).get();
        Logger.debug("Collected data is :: " + collectedData.toString());

        //Create Evaluator, pass data from Collector and obtain eval. results
        Logger.debug("Create Evaluator and Pass Collected data...");
        evaluators.Evaluator evaluator = this.EVALUATORFACTORY.create();
        EvaluationValue evalresult = evaluator.pass(collectedData).get();

        //TODO: Temporarily I directly return the result from the evaluator
        //instead of a combines list/map/jsonobject with data from the
        //collector directly (for showing the user additional info e.g. linktext)
        Logger.debug("json object is :: " + evalresult.toJson());

        JSONObject evalresultJson = evalresult.toJson();
        WebsiteHtmlController.persistEvaluation(evalresultJson, URL);

        return evalresultJson

    }

    /**
     * Records the Evaluation Result in the DB
     * @param  EVALVAL  The result of the evaluation process as JsonObject
     * @param  URL      url of the page that has been evaluated
     */
    private static void persistEvaluation(final JSONObject EVALVAL, final String URL) {

        WebPageDAO webpageDAO = new WebPageDAO();
        EvaluationResult evaluationResult = new EvaluationResult();
        evaluationResult.result = EVALVAL;

        webpageDAO.save(new WebPage(URL, evaluationResult));

    }

    private static void persistEvaluation(final EvaluationValue EVALVAL, final String URL) {
        WebsiteHtmlController.persistEvaluation(EVALVAL.toJson(), URL);
    }

}
