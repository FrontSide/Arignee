package evaluators.enums;

/**
  * Hold the Keys that are used by the WebsiteHTMLEvaluator
  * for the Resulting map which is then returned to the Controller
  */

public enum WebsiteHtmlEvaluatorKey implements EvaluatorKey {

    //General Results and Figures
    //Link amount
    LINK_AMOUNT,    
    LINK_AMOUNT_IDEAL,
    LINK_AMOUNT_DIV,
    
    //Keys for indicating sub-Maps
    LINKS_EVAL_RESULTS,

    //Ratings
    TITLE_RATING,
    LINK_AMOUNT_RATING
    
}
