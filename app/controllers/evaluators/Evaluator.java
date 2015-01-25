package evaluators;

import java.util.Map;
import java.util.List;
import evaluators.enums.EvaluatorKey;
import models.collection.CollectorValue;
import models.evaluation.EvaluationValue;
import collectors.enums.CollectorKey;

public interface Evaluator<PassedData> {

    /**
     * Defines the method that is accessed publicly.
     * Takes the data from the Collector stored in the Evaluator
     * (handed through constructor)
     * @returns: a Map with Enum-Keys and CollectorValues
     *      which contains all evaluation results
     */
    EvaluationValue get();

    /**
      * Passes the data which is to be evaluated
      * to the Evaluator
      */
    Evaluator pass(PassedData data);

}
