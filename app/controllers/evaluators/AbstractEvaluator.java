package evaluators;

public abstract class AbstractEvaluator implements Evaluator {

    private Map<? extends CollectorKey, List<String>> collected;
    
    public abstract Map<? extends EvaluatorKey, Map<? extends EvaluatorKey, String>> get();

    Evaluator pass(Map<? extends CollectorKey, List<String>> collected) {
        this.collected = collected
    }
    
    Map<? extends CollectorKey, List<String>> collected() {
        (collected == null) 
                    ? throw new RuntimeException("No Collected Data found!") 
                    : return this.collected;
    }

}
