/* Project Arignee ** autor: David Rieger ** 2014 */

/* HTML Evaluation Result Renderer */

/* This JS renders and displays the results from the HTML Evaluation
 * See WebsiteHtmlController
 * The render function is triggered from http_req.js (corresponding section)
 */
 
function html_eval_render(RESULTS) {

    var json = JSON.parse(RESULTS)
    $("#html_eval_res").css("display", "block")
    console.log(json)
    $("#html_eval_res > #her_title").html(json.TITLE)
    $("#html_eval_res > #pan_links").addClass("panel-success")
    
    //Links
    
    //Link Amount
    var link_amount_label_class
    var link_amount_panel_class
    switch(json.LINKS_EVAL_RESULTS.LINK_AMOUNT_RATING) {
        case "EXCELLENT" : link_amount_label_class = "label-success";
                            link_amount_panel_class = "panel-success"; break;
        case "OK" : link_amount_label_class = "label-danger";
                    link_amount_panel_class = "panel-danger"; break;
        default : link_amount_label_class = "label-default";
                    link_amount_panel_class = "panel-default"; break;
    }
    
    
    $("#html_eval_res > #pan_links > .panel-body > #lab_links_amount").addClass(link_amount_label_class)
    $("#html_eval_res > #pan_links > .panel-body > #lab_links_amount").html(
                                    json.LINKS_EVAL_RESULTS.LINK_AMOUNT_RATING)
                                    
    $("#html_eval_res > #pan_links > .panel-body > .eval_infotext").html(
                "You have " + json.LINKS_EVAL_RESULTS.LINK_AMOUNT +
                " links on your website. Ideal would be " + json.LINKS_EVAL_RESULTS.LINK_AMOUNT_IDEAL +
                ". That's a divergence of " + json.LINKS_EVAL_RESULTS.LINK_AMOUNT_DIV + 
                "%, which leads to a(n) <b>" + json.LINKS_EVAL_RESULTS.LINK_AMOUNT_RATING + 
                "</b> rating.")

}
