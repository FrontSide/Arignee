package models.evaluation;

/** Root of Composite Pattern for EvaluationValueFigure
  * and EvaluationValueContainer (Map)
  *
  * This composite pattern enables the possibility to nest Maps
  * of Evaluation-Results. Those can then be easily transported to
  * the View/Templates
  */

import org.json.JSONObject;
import java.util.List;
import evaluators.enums.EvaluatorKey;

public interface EvaluationValue {
    JSONObject toJson();
    void add(EvaluatorKey k, EvaluationValue v);

    /**
    * Blanks out/Removes the K-V-Pairs accodring to a given list of keys
    * to create a slim Result-Map
    * @param  blankOutKeys List of keys to remove from Map
    * @return              Filtered/Slim EvaluationValue
    */
    EvaluationValue getLightweightValue(List<EvaluatorKey> blankOutKeys);
    EvaluationValue getLightweightValue(EvaluatorKey blankOutKey);
}
