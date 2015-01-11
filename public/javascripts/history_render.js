/* Project Arignee ** autor: David Rieger ** 2014 */

/* Evaluation Result History Renerer */

/*
 *Renders charts showing the history of evaluation results
 */


 /* Takes results from HTTP response and initiates the rendering */
 function websiteHtmlEvaluationHistoryRender(RESULTS) {
     console.log("EvaluationHistoryRendering -- full json received by html_eval_render :: " + RESULTS)
     websiteHtmlEvaluationHistoryBuilder(RESULTS)
 }

/**
 * Renderd the Evaluation-Result-History chars
 * for a URL
 */
function websiteHtmlEvaluationHistoryBuilder(json) {

    resetEvalRender()
    showContainerWrapper()

    var content = ""

    var data = {
        labels: ["January", "February", "March", "April", "May", "June", "July"],
        datasets: [
            {
                label: "My First dataset",
                fillColor: "rgba(220,220,220,0.2)",
                strokeColor: "rgba(220,220,220,1)",
                pointColor: "rgba(220,220,220,1)",
                pointStrokeColor: "#fff",
                pointHighlightFill: "#fff",
                pointHighlightStroke: "rgba(220,220,220,1)",
                data: [65, 59, 80, 81, 56, 55, 40]
            },
            {
                label: "My Second dataset",
                fillColor: "rgba(151,187,205,0.2)",
                strokeColor: "rgba(151,187,205,1)",
                pointColor: "rgba(151,187,205,1)",
                pointStrokeColor: "#fff",
                pointHighlightFill: "#fff",
                pointHighlightStroke: "rgba(151,187,205,1)",
                data: [28, 48, 40, 19, 86, 27, 90]
            }
        ]
    };

    content += "<canvas id='myChart' width='600' height='300'></canvas>"

    /* render canvas before drawing charts */
    appendContainerWrapperContent(content)

    var ctx = document.getElementById('myChart').getContext("2d");
    var myLineChart = new Chart(ctx).Line(data);

    hideProgressbar()

}
