package evaluators;

public class EvaluatorFactory {

    private EvaluatorFactory() {}

    private static final EvaluatorFactory INSTANCE = 
                                    new EvaluatorFactory();
    public static final EvaluatorFactory getInstance() {
        return EvaluatorFactory.INSTANCE;
    }

    public Evaluator create() {
        return new WebsiteHtmlEvaluator();
    }

}
