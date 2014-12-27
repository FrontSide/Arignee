/* Project Arignee ** autor: David Rieger ** 2014 */

/* HTML Evaluation Result Renderer */

/* This JS renders and displays the results from the HTML Evaluation
 * See WebsiteHtmlController
 * The render function is triggered from http_req.js (corresponding section)
 */

var container_wrapper = $("#eval_res_wrapper")

/* Takes results from HTTP response and initiates the rendering */
function html_eval_render(RESULTS) {

    var json = JSON.parse(RESULTS)    
    build_html_containers(json)
}

function build_html_containers(json) {

    setProgressbarLabel("Render results...")

    container_wrapper.css("display", "block")
    
    //HTML-Builder
    var content = "";
    
    for (var top in json) { // LEVEL 1 - categories
    
        console.log(" - " + top)
        
        //Check if the overall rating value is missing
        if (json[top].RATING == null) categoryrating = "NONE"
        else categoryrating = json[top].RATING
        
        panel_type = "panel-" + getColourType(categoryrating)
        
        //Panel Heading
        content +=  "<div id='pan_title' class='panel " + panel_type 
                    + "'>" + "<div class='panel-heading'>" + top;
        
        //Panel Heading Overall-Category-Rating Label
        content +=  " <span id='lab_links_amount' class='badge'>" + 
                    categoryrating + "</span>" + "<br />" 
        
        //Close Panel Heading
        content +=  " </div> " 
                            
        for (var med in json[top] ) { //LEVEL 2 - sub-categories
            console.log(" -- " + med)
            
            //Panel Body with Sub-Category-/Name
            content +=  "<div class='panel-body'>" + "<span>" + med + "</span>"
            
            //Check if the rating value is missing
            if (json[top][med].RATING == null) rating = "NONE"
            else rating = json[top][med].RATING
            
            label_type = "label-" + getColourType(rating)
            
            //Category Rating Label
            content +=  " <span id='lab_links_amount' class='label " + 
                        label_type + "'>" + rating + "</span>" + "<br />"
            
            
            //Evaluation Infotext
            content +=  "<br /><div class='eval_infotext'>" + 
                        "Ideal would be " + json[top][med].IDEAL + 
                        ". Your website has " + json[top][med].ACTUAL + 
                        ". This is a divergence of " + json[top][med].DIV + 
                        "%." + "</div>"
            
            //Close Panel Body
            content += "</div>"
            
        }
        
        //Close Complete Panel for this Category
        content += "</div>" 
        
    }
    
    //Display generated Containers in wrapper
    container_wrapper.html(content);
    
    hideProgressbar()
    

}

/* returns a Bootstrap colour type (success, danger, warning...) 
 * or a propritary one (defined in css)
 */
function getColourType(RATING) {

    switch(RATING) {
        case "EXCELLENT" :  return "success";
        case "GOOD" :       return "primary";
        case "OK" :         return "info";
        case "TENUOUS" :    return "warning";
        case "POOR" :       return "danger"
        default :           return "default"
    }

}
    /*
    $("#html_eval_res").css("display", "block")
    console.log(json)
    $("#html_eval_res > #her_title").html(json.TITLE)
    $("#html_eval_res > #pan_links").addClass("panel-success")
    
    //Links
    
    //Link Amount
    var link_amount_label_class
    var link_amount_panel_class
    
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
                */

