package models.evaluation;

import java.util.Map;
import java.util.Map.*;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
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
            else
                json.put(e.getKey().toString(), ((EvaluationValueFigure) e.getValue()).getValue());
        }
        return json;
    }

    @Override
    public EvaluationValue getLightweightValue(List<EvaluatorKey> blankOutKeys) {
        EvaluationValueContainer slim = new EvaluationValueContainer();
        for (Map.Entry<EvaluatorKey, EvaluationValue> e : this.results.entrySet()) {
            if (blankOutKeys.contains(e.getKey())) continue;
            slim.add(e.getKey(), e.getValue().getLightweightValue(blankOutKeys));
        }
        return slim;
    }

    @Override
    public EvaluationValue getLightweightValue(EvaluatorKey blankOutKey) {
        List<EvaluatorKey> keys = new ArrayList<>();
        keys.add(blankOutKey);
        return getLightweightValue(keys);
    }

    @Override
    public String toString() {
        return results.toString();
    }

}
