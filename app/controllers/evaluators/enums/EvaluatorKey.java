package evaluators.enums;

/**
 * An interface for Supertyping enums that are used in Evaluator-Maps as Keys
 * However, when using it with wildcards, the keyword "extends has to be used"
 * --> <? extends EvaluatorKey> 
 * (not "implements")
 *
 * That is just insane!
 */
public interface EvaluatorKey {}
