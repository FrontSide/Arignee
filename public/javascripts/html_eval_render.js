/* Project Arignee ** autor: David Rieger ** 2014 */

/* HTML Evaluation Result Renderer */

/* This JS renders and displays the results from the HTML Evaluation
 * See WebsiteHtmlController
 * The render function is triggered from http_req.js (corresponding section)
 */

var container_wrapper = $("#eval_res_wrapper")

/* Takes results from HTTP response and initiates the rendering */
function html_eval_render(RESULTS) {

    //var json = $.parseJSON(RESULTS)
    console.log("full json received by html_eval_render :: " + RESULTS)
    build_html_containers(RESULTS)
}

function build_html_containers(json) {

    container_wrapper.css("display", "block")

    //HTML-Builder
    var content = "";

    //Calculate how much to increment the Progressbar per Category
    var NUM_CATEGORIES = Object.keys(json).length
    console.log("outerSteps :: " + NUM_CATEGORIES)
    var PROG_CAT_INCREMENT = (100-currentProgress())/NUM_CATEGORIES
    console.log("outerIncrementSize :: " + PROG_CAT_INCREMENT)

    for (var top in json) { //loop LEVEL 1 - categories

        console.log(" - " + top)

        /* Further Calculate how much to increment the Progressbar
         * per Sub-Category
         */
        var NUM_SUB_CATEGORIES = Object.keys(json[top]).length
        console.log("innerSteps :: " + NUM_SUB_CATEGORIES)
        var PROG_SUBCAT_INCREMENT = PROG_CAT_INCREMENT/NUM_SUB_CATEGORIES
        console.log("innerIncrementSize :: " + PROG_SUBCAT_INCREMENT)

        //Check if the overall rating value is missing
        if (json[top].RATING == null) categoryrating = "NONE"
        else categoryrating = json[top].RATING

        panel_type = "panel-" + getColourType(categoryrating)

        //Panel Heading
        content +=  "<div id='pan_title' class='panel " + panel_type
                    + "'>" + "<div class='panel-heading'>" + Messages(top);

        //Panel Heading Overall-Category-Rating Label
        content +=  " <span id='lab_links_amount' class='badge'>" +
                    categoryrating + "</span>" + "<br />"

        //Close Panel Heading
        content +=  " </div> "

        for (var med in json[top] ) { //loop LEVEL 2 - sub-categories
            console.log(" -- " + med)

            //Increment Progressbar
            incrementProgressbar(PROG_SUBCAT_INCREMENT)

            //Panel Body with Sub-Category-/Name
            content +=  "<div class='panel-body'>"
                        + "<span class='sub-title'>" + Messages(med) + "</span>"

            //Check if the rating value is missing
            if (json[top][med].RATING == null) rating = "NONE"
            else rating = json[top][med].RATING //LEVEL 3

            label_type = "label-" + getColourType(rating)

            //Category Rating Label
            content +=  " <span id='lab_links_amount' class='label " +
                        label_type + "'>" + rating + "</span>" + "<br />"


            //Evaluation Infotext on LEVEL 3
            content +=  "<br /><div class='eval_infotext'>" +
                        "Ideal would be " + json[top][med].IDEAL +
                        ". Your website has " + json[top][med].ACTUAL +
                        ". This is a divergence of " + json[top][med].DIV +
                        "%." + "</div>"


            //Check if additional Information is available and render it in
            //well if so
            var additionals = json[top][med].ADDITIONAL
            if (additionals != null) {
                content += "<br /><div class='well additional'>"
                for (var add in additionals) { //loop LEVEL 3 - additionals
                    content += "<br /><b>" + add + "</b><br />"
                    //the value found here could be a json-array
                    for (var i=0; i< json[top][med].ADDITIONAL[add].length; i++) {
                        content += json[top][med].ADDITIONAL[add][i] + "<br />"
                    }
                }
                content += "</div>"
            }

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
