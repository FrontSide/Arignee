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

public enum EvaluatorKey {

    /* LEVEL 1 */
    /* Keys for sub-Maps i.e. Categories */
    PERFORMANCE,
    SEO,
    USABILITY,
    LEGAL,
    SOCIAL,


    /* LEVEL 2 */
    /* Keys for sub-sub-Maps i.e. Sub-Categories */

    /* SEO */
    LINK_AMOUNT,
    EXTERNAL_BACKLINK_RATIO,

    /* USABILITY */
    INTERNAL_BACKLINK_RATIO,

    /* PERFORMANCE */
    LANDING_PAGE_RESPONSE_TIME,

    /* LEVEL 3 */
    /* Keys for values that show actual and ideal figures and the divergence
    * between those two */
    ACTUAL,
    IDEAL,
    DIV,

    /* Key for the actual rating-outcome value */
    RATING,

    /* Key for additional information */
    ADDITIONAL,

    /* LEVEL 4 */
    /* Keys for additional information to an evaluation value */
    FOUND_URLS

}
