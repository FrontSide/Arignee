package evaluators;

import java.util.Map;
import java.util.HashMap;
import evaluators.enums.EvaluatorKey;

public class EvaluationResultContainer implements EvaluationResult {

    private Map<EvaluatorKey, EvaluationResult> results = new HashMap<>();

    public void add(EvaluatorKey k, EvaluationResult v) {
        this.results.put(k, v);
    }
    
    @Override
    public String toString() {
        return results.toString();
    }
    
}
