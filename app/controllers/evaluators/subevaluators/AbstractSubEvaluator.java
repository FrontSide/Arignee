package evaluators.subevaluators;

/**
  * SubEvaluators are Evaluators that are used by other evaluators only,
  * and never directly accessed/instantiated by the Controller and thus
  * have a simpler structure.
  */

import java.util.Map;
import java.util.List;
import java.lang.NoSuchMethodException;

import play.Logger;
import play.Logger.ALogger;

import evaluators.Evaluator;
import collectors.enums.CollectorKey;
import models.evaluation.EvaluationValue;
import models.collection.CollectorValue;
import ticketing.TicketStatus;
import ticketing.TicketHandler;
import ticketing.TicketProcessor;

public abstract class AbstractSubEvaluator implements Evaluator, TicketProcessor {

    private static final ALogger logger = Logger.of(AbstractSubEvaluator.class);

    protected EvaluationValue result;

    /* ------------ Impl of TicketProcessor ------------ */
    private final TicketHandler TICKETHANDLER = TicketHandler.getInstance();
    protected long ticketNumber;

    @Override
    public void setTicketNumber(long number) {
        this.ticketNumber = number;
    }

    @Override
    public void updateTicketStatus(TicketStatus status) {
        if (this.ticketNumber < TICKETHANDLER.MIN) {
            logger.error("No Ticket-Number found!");
            return;
        }
        this.TICKETHANDLER.updateStatus(this.ticketNumber, status);
    }
    /* ------------ ----------------------- ------------ */

    public abstract EvaluationValue get();

   /** This method has to be overriden OR BETTER overloaded by the concrete
     * SubEvaluator it is not intended to be used
     * for the data coming from the collector but merely for sub-collections
     */
    public Evaluator pass(Map<? extends CollectorKey, CollectorValue> collected) {
        Logger.error("The pass(Map<? extends CollectorKey, List<String>>)" +
                        " method is not available for SubEvaluators");
        return this;
    }


}
