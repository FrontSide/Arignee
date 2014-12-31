package models.evaluation;

/** Root of Composite Pattern for EvaluationValueFigure
  * and EvaluationValueContainer (Map)
  *
  * This composite pattern enables the possibility to nest Maps
  * of Evaluation-Results. Those can then be easily transported to
  * the View/Templates
  */

import org.json.JSONObject;
import evaluators.enums.EvaluatorKey;

public interface EvaluationValue {
    JSONObject toJson();
    void add(EvaluatorKey k, EvaluationValue v);
}
