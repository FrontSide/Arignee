package evaluators;

/** Root of Composite Pattern for EvaluationFigures 
  * and EvaluationResultContainer (Map)
  *
  * This composite pattern enables the possibility to nest Maps
  * of Evaluation-Results. Those can then be easily transported to 
  * the View/Templates
  */

import org.json.JSONObject;
import evaluators.enums.EvaluatorKey;

public interface EvaluationResult {
    JSONObject toJson();
    void add(EvaluatorKey k, EvaluationResult v);
}
