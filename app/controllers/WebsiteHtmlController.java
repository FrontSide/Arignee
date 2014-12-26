
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
import collectors.enums.CollectorKey;
import collectors.enums.WebsiteHtmlCollectorKey;
import evaluators.enums.EvaluatorKey;
import evaluators.enums.WebsiteHtmlEvaluatorKey;
import evaluators.EvaluationFigure;
import evaluators.EvaluationResult;
import evaluators.EvaluationResultContainer;
import evaluators.EvaluationResultContainer.*;
import evaluators.EvaluatorFactory;

public class WebsiteHtmlController extends Controller {

    private final WebsiteHtmlCollectorFactory COLLECTORFACTORY = 
                                WebsiteHtmlCollectorFactory.getInstance();
                                
    private final EvaluatorFactory EVALUATORFACTORY =
                                EvaluatorFactory.getInstance();

    /**
      * Receives a Map with all relevent data from the Controller
      * passes it to the Evaluator and returns
      * a Map that is ready to be rendered by the template
      */
    public JSONObject evaluate(final String URL) {
    
        // Create Collector and obtain extracted data
        Logger.debug("Invoke Collector for URL :: \"" + URL + "\"...");
        collectors.Collector collector = this.COLLECTORFACTORY.create();
        Map<? extends CollectorKey, List<String>> collectedData = collector.url(URL).get();        
        Logger.debug("Collected data is :: " + collectedData.toString());
        
        //Create Evaluator, pass data from Collector and obtain eval. results
        Logger.debug("Create Evaluator and Pass Collected data...");
        evaluators.Evaluator evaluator = this.EVALUATORFACTORY.create();
        EvaluationResult evalresult = evaluator.pass(collectedData).get();
        
        Logger.debug("Evaluator returned :: " + evalresult);
        
        //TODO: Temporarily I directly return the result from the evaluator
        //instead of a combines list/map/jsonobject with data from the
        //collector directly (for showing the user additional info e.g. linktext)
        Logger.debug("json object is :: " + evalresult.toJson());
        
        return evalresult.toJson();
        
    }

}
