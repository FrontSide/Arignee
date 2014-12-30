package evaluators.enums;

/**
  * Hold the Keys that are used by the WebsiteHTMLEvaluator
  * for the Resulting map which is then returned to the Controller
  * The Data-structure that is created with all its levels can be seen
  * in the Actual Evaluator Class.
  *
  * Important! :: Keep in mind that a Map can only hold one of a Key on
  * the same level. Therefore NESTING is important.
  * (again, see Map-Data-Structure (EvaluationResult) in Evaluator Impl)
  */

public enum WebsiteHtmlEvaluatorKey implements EvaluatorKey {

    /* LEVEL 1 */
    /* Keys for sub-Maps i.e. Categories */
    LINKS,

    /* LEVEL 2 */
    /* Keys for sub-sub-Maps i.e. Sub-Categories */
    AMOUNT,
    BACKLINK_RATIO,

    /* LEVEL 3 */
    /* Keys for values that show actual and ideal figures and the divergence
     * between those two */
    ACTUAL,
    IDEAL,
    DIV,

    /* Key for the actual rating-outcome value */
    RATING

}
