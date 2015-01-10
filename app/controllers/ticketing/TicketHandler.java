package ticketing;

/**
* This Controller Handles Tickets for HTTPRequests/Clients.
*
* Example: A user wants to evaluate a Website and enters its URL
* 			it is first routed to this Ticket Generator where it gets a
* 			Number which represents a Ticket.
* 			The evaluation is then asynchronously executed in the background.
* 			The ticket however is linked to the evaluation-Process.
* 			Whenever the client now sends a request with the Ticket-Number
* 			it receives either a status update of the evaluation process
* 			or if the evaluation process has finished for this ticket
* 			the client receives the Results which can then be rendered by JS.
*
* 			Singleton
*/

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

import play.mvc.Result;
import play.mvc.Results;
import play.libs.F.*;
import play.Logger;
import play.Logger.ALogger;

import models.persistency.EvaluationResult;
import models.ticketing.Ticket;
import daos.EvaluationResultDAO;

public class TicketHandler {

    private static final ALogger logger = Logger.of(TicketHandler.class);

    private TicketHandler(){}
    private static TicketHandler instance;
    public static TicketHandler getInstance() {
        if (TicketHandler.instance == null)
        TicketHandler.instance = new TicketHandler();
        return TicketHandler.instance;
    }

    private List<Ticket> tickets = new ArrayList<>();

    public String getNextTicketNumber() {
        return UUID.randomUUID().toString();
    }

    public String getNewTicket() {
        Ticket t = new Ticket();
        this.tickets.add(t);
        return t.getNumber();
    }

    public void updateStatus(String number, TicketStatus status) {
        logger.info("updating status of ticket :: "
                    + number + " :: to :: " + status);
        this.getTicket(number).setStatus(status);
    }

    /**
     * Mark a Ticket as finished and ready to have result obtained
     * @param number number of ticket to mark as finished
     */
    public void markAsFinished(String number) {
        this.getTicket(number).setFinished();
    }

    /**
     * Passes a promise of a Result to a ticket
     * @param  number   number of the ticket to pass result to
     * @param  result   Promise of the result
     */
    public void passResponse(String number, Promise<Result> result) {
        this.getTicket(number).setResponse(result);
    }

    public TicketStatus getStatus(String number) {
        Ticket t = getTicket(number);
        if (t != null) return t.getStatus();
        boolean inDB = new EvaluationResultDAO().isResultForTicketAvailable(number);
        return inDB ? TicketStatus.RESPONSE_AVAILABLE : TicketStatus.TICKET_NOT_FOUND;
    }

    public Result getResponse(String number) {
        Ticket t = this.getTicket(number);
        Result r;

        if (t == null) {
            logger.info("ticket not in list. trying to get result from DB :: " + number);
            if (new EvaluationResultDAO().isResultForTicketAvailable(number))
                r = Results.ok(this.getResultFromDB(number).getResult());
            else r = null;
        }
        else if (!t.isFinished()) throw new TicketNotFinishedException();
        else r = t.getResponse().get(30000L);

        if (r == null) {
            logger.error("could not obtain response from ticket!");
            return null;
        }

        this.tickets.remove(t);
        return r;
    }

    private Ticket getTicket(String number) {

        for (Ticket t : this.tickets) {
            if (number.equals(t.getNumber())) return t;
        }

        return null;
    }

    private EvaluationResult getResultFromDB(String number) {
        return new EvaluationResultDAO().getByTicketNumber(number);
    }

}
