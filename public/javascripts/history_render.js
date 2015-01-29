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


function websiteHtmlEvaluationHistoryBuilder(json) {

    resetEvalRender()
    showContainerWrapper()



    //Build new object where
    var graphs = []
    var names = []

    /* -------------------------------------------------- */
    /* GATHERING CATEGORIES AND SUB-CATEGORIES ---------- */
    /* -------------------------------------------------- */

    //store categories with their sub-categories in objects
    //with keys (categories) and arrays (list of sub-categories) as values
    var categories = {}

    console.log("gathering categories...")

    for (var i=0; i<json.length; i++) {

        var curDateEvalPair = json[i]
        var curDate = Object.keys(curDateEvalPair)[0]
        var curEval = curDateEvalPair[curDate]

        console.log("CURRENT DATE :: " + curDate)
        console.log("CURRENT EVAL :: %obj ", curEval)

        //add not yet registered categories to the "categories" object
        for (curCategory in curEval) {

            console.log("CURRENT CATEGORY :: " + curCategory)

            //If category not yet existing
            //Add it to the categories object as key
            if (categories[curCategory] == null) {
                console.log("NEW CATEGORY :: " + curCategory)
                categories[curCategory] = []
            }

            //add not yet registered SUB-categories to the array
            //assigned to each category(key) in the "categories" object
            for (curSubCategory in curEval[curCategory]) {

                console.log("CURRENT SUB-CATEGORY :: " + curSubCategory)

                //If current Subcategory NOT in
                //sub-categories array in categories object
                //push sub-category into array
                if ($.inArray(curSubCategory, categories[curCategory]) < 0) {
                    console.log("NEW SUB-CATEGORY :: " + curSubCategory)
                    categories[curCategory].push(curSubCategory)
                }
            }
        }

    }

    console.log("finished with gathering categories.")
    console.log("category Object- Dump: %o", categories)


    /* -------------------------------------------------- */
    /* COLLECTING DATA (RATINGS)  ----------------------- */
    /* -------------------------------------------------- */

    //Store all graphs in "graphs" object whereas categories are keys
    //which store objects that have sub-categories as keys which store
    //key-value-pairs that are made from DATES(keys) and RATINGS(values)
    //
    // -CATEGORY
    // --SUBCATEGORY
    // ---DATE
    // ----VALUE
    // ---DATE
    // ----VALUE
    // --SUBCATEGORY
    // .... etc ....
    var graphs = {}

    console.log("collecting rating data...")

    for (var i=0; i<json.length; i++) {

        var curDateEvalPair = json[i]
        var curDate = Object.keys(curDateEvalPair)[0]
        var curEval = curDateEvalPair[curDate]

        console.log("CURRENT EVAL :: %o", curEval)

        for (category in categories) {

            console.log("CURRENT CATEGORY :: " + category)
            console.log("VALUE FOR CURRENT CATEGORY :: %o", curEval[category])

            //Create object for this category if not yet existing
            if (graphs[category] == null) {
                graphs[category] = {}
                graphs[category].OVERALL = {}
            }

            if (curEval[category] == null || curEval[category].RATING == null) {
                console.log("NO OVERALL RATING FOR CATEGORY FOUND :: " + category)
                graphs[category].OVERALL[curDate] = 0
            } else {
                graphs[category].OVERALL[curDate] = getIntOfRating(curEval[category].RATING)
            }

            //Iterate through sub-categories array for this category
            for (var j=0; j<categories[category].length; j++) {

                var subcategory = categories[category][j]

                console.log("CURRENT SUB-CATEGORY :: " + subcategory)

                //Create object for this category if not yet existing
                if (graphs[category][subcategory] == null) {
                    graphs[category][subcategory] = {}
                }

                if (    curEval[category] == null
                     || curEval[category][subcategory] == null
                     || curEval[category][subcategory].RATING == null) {

                    console.log("NO RATING FOR SUB-CATEGORY FOUND :: " + subcategory)
                    graphs[category][subcategory][curDate] = 0

                } else {
                    graphs[category][subcategory][curDate] = getIntOfRating(curEval[category][subcategory].RATING)
                }

            }

        }

    }

    console.log("finished with collecting data.")
    console.log("graphs Object- Dump: %o", graphs)

    /* -------------------------------------------------- */
    /* DRAW GRAPHS -------------------------------------- */
    /* -------------------------------------------------- */

    console.log("rendering graphs...")

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

    for (graph in graphs) {

        console.log("graph is :: " + graph)

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

        var lineData = []

        var i = 0
        for (line in graphs[graph]) {

            console.log("line is :: " + line)

            dates = []
            ratings = []
            for (date in graphs[graph][line]) {
                dates.push(date)
                ratings.push(graphs[graph][line][date])
                console.log("date is :: " + date)
                console.log("rating is :: " + graphs[graph][line][date])
            }

            var curLineData = {
                label: Messages(line),
                fillColor: getColor(i, "FILL"),
                strokeColor: getColor(i, "STROKE"),
                pointColor: getColor(i, "POINT"),
                pointStrokeColor: "#fff",
                pointHighlightFill: "#fff",
                pointHighlightStroke: getColor(i, "STROKE"),
                data: ratings
            }

            lineData.push(curLineData)
            i++

        }

        var curGraphData = {
            labels: dates,
            datasets: lineData
        }

        var ctx = document.getElementById(chartID).getContext("2d");
        var lineChart = new Chart(ctx).Line(curGraphData, options);

        var legend = lineChart.generateLegend();
        $("#" + chartID + "container").append(legend)
    }

    hideProgressbar()

}
