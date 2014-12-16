/* Project Arignee ** autor: David Rieger ** 2014 */

/* HTML Evaluation Result Renderer */

/* This JS renders and displays the results from the HTML Evaluation
 * See WebsiteHtmlController
 * The render function is triggered from http_req.js (corresponding section)
 */
 
function html_eval_render(RESULTS) {

    var json = JSON.parse(RESULTS)
    $("#html_eval_res").css("display", "block")    
    $("#html_eval_res > #her_title").html(json.title)

}
