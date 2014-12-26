package evaluators;

import java.util.Map;
import java.util.Map.*;
import java.util.HashMap;
import org.json.JSONObject;
import evaluators.enums.EvaluatorKey;

public class EvaluationResultContainer implements EvaluationResult {

    private Map<EvaluatorKey, EvaluationResult> results = new HashMap<>();

    public void add(EvaluatorKey k, EvaluationResult v) {
        this.results.put(k, v);
    }
            
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        for (Map.Entry<EvaluatorKey, EvaluationResult> e : this.results.entrySet()) {
            if (e.getValue() instanceof EvaluationResultContainer)
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
