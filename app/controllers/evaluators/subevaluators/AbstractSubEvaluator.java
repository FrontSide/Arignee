package evaluators.subevaluators;

/**
  * SubEvaluators are Evaluators that are used by other evaluators only,
  * and never directly accessed/instantiated by the Controller and thus
  * have a simpler structure.
  */

import java.util.Map;
import java.util.List;
import java.lang.NoSuchMethodException;
import play.Logger;
import evaluators.Evaluator;
import evaluators.EvaluationResult;
import collectors.enums.CollectorKey;

public abstract class AbstractSubEvaluator implements Evaluator {

    protected EvaluationResult result;

    public abstract EvaluationResult get();

   /** This method has to be overriden OR BETTER overloaded by the concrete 
     * SubEvaluator it is not intended to be used
     * for the data coming from the collector but merely for sub-collections
     */
    public Evaluator pass(Map<? extends CollectorKey, List<String>> collected) {
        Logger.error("The pass(Map<? extends CollectorKey, List<String>>)" + 
                        " method is not available for SubEvaluators");
        return this;
    }
    

}
