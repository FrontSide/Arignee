package models.evaluation;

import java.util.Map;
import java.util.Map.*;
import java.util.HashMap;
import org.json.JSONObject;
import evaluators.enums.EvaluatorKey;

public class EvaluationValueContainer implements EvaluationValue {

    private Map<EvaluatorKey, EvaluationValue> results = new HashMap<>();

    public void add(EvaluatorKey k, EvaluationValue v) {
        this.results.put(k, v);
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        for (Map.Entry<EvaluatorKey, EvaluationValue> e : this.results.entrySet()) {
            if (e.getValue() instanceof EvaluationValueContainer)
                json.put(e.getKey().toString(), e.getValue().toJson());
            else json.put(e.getKey().toString(), e.getValue().toString());
        }
        return json;
    }

    @Override
    public String toString() {
        return results.toString();
    }

}
