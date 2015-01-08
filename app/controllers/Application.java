package controllers;

import play.*;
import play.mvc.*;
import play.Logger;
import play.Logger.ALogger;
import play.mvc.BodyParser;
import play.mvc.BodyParser.*;
import play.libs.F.*;
import static play.libs.F.Promise.promise;

import java.util.concurrent.Callable;
import java.util.Map;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.json.JSONObject;

import views.html.*;
import ticketing.TicketHandler;
import ticketing.TicketStatus;
import ticketing.TicketNotFinishedException;
import models.ticketing.Ticket;

public class Application extends Controller {

    private static final ALogger logger = Logger.of(Application.class);

    private static final TicketHandler TICKETHANDLER = TicketHandler.getInstance();

    public static Result index() {
        return ok(index.render(null));
    }

    public static Result kwpot(String kp) {
        //return ok(index.render(GoogleTrendsController.getKeywordTimePopularity(kp)));
        new GoogleTrendsController().getKeywordTimePopularity(kp);
        return index();
    }


    @BodyParser.Of(Json.class)
    public static Result requestHtmlEvaluation(String url) {
        final String URL = url;
        logger.info("html website evaluation for url requested :: " + URL);
        logger.info("generating ticket...");
        final String TICKETNUMBER = Application.TICKETHANDLER.getNewTicket();
        logger.info("triggering evaluation for ticket :: " + TICKETNUMBER + " :: asynchronously");

        //Trigger the evaluation process asynchronously and store the
        //Promise in the ticket
        Promise<JSONObject> promiseOfEvaluationResult = Promise.promise(
            new Function0<JSONObject>() {
                public JSONObject apply() {
                    JSONObject result = WebsiteHtmlController.evaluate(URL, TICKETNUMBER);
                    Application.TICKETHANDLER.updateStatus(TICKETNUMBER, TicketStatus.RESPONSE_AVAILABLE);
                    Application.TICKETHANDLER.markAsFinished(TICKETNUMBER);
                    return result;
                }
            }
        );

        Promise<Result> promiseOfResult = promiseOfEvaluationResult.map(
            new Function<JSONObject,Result>() {
                public Result apply(JSONObject evaluationResult) {
                    return ok(evaluationResult.toString());
                }
            }
        );

        Application.TICKETHANDLER.passResponse(TICKETNUMBER, promiseOfResult);

        logger.info("returning ticketstatus...");
        return ticketStatus(TICKETNUMBER);
    }


    /**
     * Requests the Status of a ticket according to its number.
     * Returns either Status of Ticket or Response Ticket is holding if Ticket
     * has finished.
     * @param  ticketNumber Number of the Ticket
     *                      for which the status is requested
     *
     * @return              Either status of the ticket
     *                      or the actual response if available
     */
    @BodyParser.Of(Json.class)
    public static Result ticketStatus(String ticketNumber) {
        JSONObject job = new JSONObject();
        TicketStatus status = TICKETHANDLER.getStatus(ticketNumber);

        //Get Response if available
        if (status.equals(TicketStatus.RESPONSE_AVAILABLE)) {
            try {
                return TICKETHANDLER.getResponse(ticketNumber);
            } catch (TicketNotFinishedException e) {
                logger.error("ticketsStatus() failed to get response from ticket :: " + ticketNumber);
            }
        }

        //Return Ticket-Status if response not available
        job.put("TICKET", ticketNumber);
        job.put("STATUS", status);
        return ok(job.toString());
    }

}
