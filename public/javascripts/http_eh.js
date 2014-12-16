/* Project Arignee ** autor: David Rieger ** 2014 */

/* HTTP Request Event Handler */

/* This JS defines all the evant handlers that are attached to 
 * UI elements or actions that trigger a function with an HTTP request
 */
 
$("#trends_kw_send_btn").click(function(){ 
    console.log("trends_kw_send_btn TRIGGERED");
    /* from trends_kw.js */
    trends_kw_request($("#keyphrase").val()); 
});

$("#url_send_btn").click(function(){ 
    console.log("url_send_btn TRIGGERED");
    /* from html_content.js */
    html_content_request($("#url").val()); 
});
