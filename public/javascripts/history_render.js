/* Project Arignee ** autor: David Rieger ** 2014 */

/* Evaluation Result History Renerer */

/*
 * Renders charts showing the history of evaluation results
 */

function getIntOfRating(RATING) {
    console.log("converting :: " + RATING + " to INT")
    switch(RATING) {
        case "EXCELLENT" :  return 5
        case "GOOD" :       return 4
        case "OK" :         return 3
        case "TENUOUS" :    return 2
        case "POOR" :       return 1
        default :           return 0
    }
}

function getRatingOfInt(RATING) {
    console.log("converting :: " + RATING + " to RATING")
    switch(parseInt(RATING)) {
        case 5 :  return "EXCELLENT"
        case 4 :  return "GOOD"
        case 3 :  return "OK"
        case 2 :  return "TENUOUS"
        case 1 :  return "POOR"
        default : return "NOT_AVAILABLE"
    }
}

function getColor(COUNTER, ELEMENT) {

    var r
    var g
    var b
    var a

    switch(COUNTER) {
        case 0 : r=220; g=100; b=100; break
        case 1 : r=100; g=220; b=100; break
        case 2 : r=100; g=100; b=220; break
        case 3 : r=220; g=220; b=100; break
        default : r=100; g=220; b=220
    }

    switch(ELEMENT) {
        case "FILL" : a=0.2; break
        default : a=1
    }

    return "rgba(" + r + "," + g + "," + b + "," + a + ")"

}

 /* Takes results from HTTP response and initiates the rendering */
 function websiteHtmlEvaluationHistoryRender(RESULTS) {
     var json = $.parseJSON(RESULTS)
     console.log("EvaluationHistoryRendering -- full json received by html_eval_render :: " + json)
     websiteHtmlEvaluationHistoryBuilder(json)
 }

/**
 * Renderd the Evaluation-Result-History chars
 * for a URL
 */
function websiteHtmlEvaluationHistoryBuilder(json) {

    resetEvalRender()
    showContainerWrapper()

    /* -------------------------------------------------- */
    /* COLLECTING DATA ---------------------------------- */
    /* -------------------------------------------------- */

    var dates = []
    var datasets = {}

    /* Stores the overall ratings for the WebPage */
    datasets["OVERALL"] = {}
    datasets["OVERALL"]["OVERALL"] = []

    for (var i=0; i<json.length; i++) {

        var kvPair = json[i]

        console.log("kvPair :: " + kvPair)

        var date = Object.keys(kvPair)[0]
        var eval = kvPair[date]

        dates.push(date)
        datasets["OVERALL"]["OVERALL"].push(getIntOfRating(eval.RATING))

        /* Each Category gets one chart */
        for (var cat in eval) {

            /* Create Dataset for Category if not Existing */
            if (datasets[cat] == null) {
                console.log("category found :: " + cat)
                datasets[cat] = {}
                datasets[cat]["OVERALL"] = []
            }

            datasets[cat]["OVERALL"].push(getIntOfRating(eval[cat].RATING))

            /* Each Sub-Category gets one Line in the chart */
            for (var subcat in eval[cat]) {

                /* Create Dataset for Subcategory if not Existing */
                if (datasets[cat][subcat] == null) {
                    console.log("sub-category found :: " + subcat)
                    datasets[cat][subcat] = []
                }

                datasets[cat][subcat].push(getIntOfRating(eval[cat][subcat].RATING))
            }
        }

    }

    /* -------------------------------------------------- */
    /* RENDERING GRAPH ---------------------------------- */
    /* -------------------------------------------------- */

    /* Overwriting the axis of ordinates' (y-axis) labels*/
    var options = {
        scaleLabel : "<%= Messages(getRatingOfInt(value)) %>",
        responsive : true,
        multiTooltipTemplate: "<%= Messages(getRatingOfInt(value)) %>",
        legendTemplate :
            "<% for (var i=0; i<datasets.length; i++){%>"
            +   "<div class=\"graphLegendElement\" style=\"border-color:<%=datasets[i].strokeColor%>\" >"
            +       "<%if(datasets[i].label){%>"
            +           "<%=datasets[i].label%>"
            +       "<%}%>"
            +   "</div>"
            + "<%}%>"
    };


    /* Iterate through datasets */
    for (graph in datasets) {

        var chartID = "chart" + graph
        var content = ""

        content += "<div id='pan_title' class='panel panel-default'>"
        + "<div class='panel-heading'>" + Messages(graph) + "</div>"
        + "<div class='panel-body'>"
        + "<div class='chartContainer' id='" + chartID + "container'>"
        + "<canvas class='historyGraph' id=" + chartID
        + " width='600px' height='300'></canvas>"
        + "</div>"
        + "</div></div>"

        appendContainerWrapperContent(content)

        var lines = []

        var i = 0
        for (line in datasets[graph]) {

            console.log("GRAPH :: " + graph + " :: LINE :: " + line + " :: DATA :: " + datasets[graph][line])

            var data = {
                label: Messages(line),
                fillColor: getColor(i, "FILL"),
                strokeColor: getColor(i, "STROKE"),
                pointColor: getColor(i, "POINT"),
                pointStrokeColor: "#fff",
                pointHighlightFill: "#fff",
                pointHighlightStroke: getColor(i, "STROKE"),
                data: datasets[graph][line]
            }

            lines.push(data)
            i++
        }

        var data = {
            labels: dates,
            datasets: lines
        }

        var ctx = document.getElementById(chartID).getContext("2d");
        var lineChart = new Chart(ctx).Line(data, options);

        var legend = lineChart.generateLegend();
        $("#" + chartID + "container").append(legend)

    }

    hideProgressbar()

}
