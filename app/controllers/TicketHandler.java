package controllers;

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

import play.mvc.*;
import play.Logger;
import play.Logger.ALogger;

import models.ticketing.Ticket;

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

    private long lastTicketNumber = -1;
    public long getNextTicketNumber() {
        return ++this.lastTicketNumber;
    }

    public Ticket getNewTicket() {
        Ticket t = new Ticket();
        this.tickets.add(t);
        return t;
    }

    public String getStatus(long number) {

        Ticket t = getTicket(number);

        if (t == null) {
            if (number <= this.lastTicketNumber) return "FINISHED OR ABORTED";
            return "NOT FOUND";
        }

        if (t.isFinished()) {
            this.tickets.remove(t);
            return this.getStatus(number);
        }

        return t.getStatusText();
    }

    public Result getResponse(long number) {
        logger.info("trying to obtain stored response from ticket :: " + number);
        return this.getTicket(number).getResponse().get(30000L);
    }

    private Ticket getTicket(long number) {
        for (Ticket t : this.tickets) {
            if (t.getNumber() == number) return t;
        }
        return null;
    }

}
