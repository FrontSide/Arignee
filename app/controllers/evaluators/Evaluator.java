package evaluators;

import java.util.Map;
import java.util.List;
import evaluators.enums.EvaluatorKey;
import collectors.enums.CollectorKey;

public interface Evaluator {

    /**
     * Defines the method that is accessed publicly.
     * Takes the data from the Collector stored in the Evaluator 
     * (handed through constructor)
     * @returns: a Map with Enum-Keys and List<String> Values
     *      that contains all evaluation results
     */
    Map<? extends EvaluatorKey, Map<? extends EvaluatorKey, EvaluationFigure>> get();
    
    /**
      * Passes the data collected by the Collector to the Evaluator
      */
    Evaluator pass(Map<? extends CollectorKey, List<String>> collected);
    
}
