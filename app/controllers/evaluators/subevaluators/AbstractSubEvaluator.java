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

public abstract class AbstractSubEvaluator<PassedData> implements Evaluator<PassedData>, TicketProcessor {

    private static final ALogger logger = Logger.of(AbstractSubEvaluator.class);

    protected EvaluationValue result;

    /* ------------ Impl of TicketProcessor ------------ */
    private final TicketHandler TICKETHANDLER = TicketHandler.getInstance();
    protected String ticketNumber;

    @Override
    public void setTicketNumber(String number) {
        this.ticketNumber = number;
    }

    @Override
    public void updateTicketStatus(TicketStatus status) {
        if (this.ticketNumber == null) return;
        this.TICKETHANDLER.updateStatus(this.ticketNumber, status);
    }
    /* ------------ ----------------------- ------------ */

    public abstract EvaluationValue get();

    public abstract Evaluator pass(PassedData collected);


}
