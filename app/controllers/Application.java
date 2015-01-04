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
        final Ticket TICKET = TICKETHANDLER.getNewTicket();
        logger.info("triggering evaluation for ticket :: " + TICKET.getNumber() + ":: asynchronously");

        //Trigger the evaluation process asynchronously and store the
        //Promise in the ticket
        Promise<JSONObject> promiseOfEvaluationResult = Promise.promise(
            new Function0<JSONObject>() {
                public JSONObject apply() {
                    return WebsiteHtmlController.evaluate(URL);
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

        TICKET.setResponse(promiseOfResult);

        logger.info("returning ticketstatus...");
        return ticketStatus(TICKET.getNumber());
    }


    @BodyParser.Of(Json.class)
    public static Result ticketStatus(long ticketNumber) {
        JSONObject job = new JSONObject();
        job.put("STATUS", TICKETHANDLER.getStatus(ticketNumber));
        return ok(job.toString());
    }

}
