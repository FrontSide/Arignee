package daos;

/**
*
*/

import models.persistency.EvaluationResult;
import models.persistency.Hyperlink;
import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow;
import play.db.ebean.*;
import play.db.ebean.Model.Finder;
import play.Logger;
import play.Logger.ALogger;

public class EvaluationResultDAO implements DAO<EvaluationResult> {

    private static final ALogger logger = Logger.of(EvaluationResultDAO.class);

    private static Finder<Long,EvaluationResult> find =
    new Finder<>(Long.class, EvaluationResult.class);

    @Override
    public void save(EvaluationResult model) {
        logger.info("persisting model :: " + model);
        Ebean.save(model);
    }

    public EvaluationResult getByTicketNumber(String ticketNumber) {
        return this.find.where().eq("ticketNumber", ticketNumber).findUnique();
    }

    public boolean isResultForTicketAvailable(String ticketNumber) {
        String sql = "select count(ticket_nmber) as count from evaluation_result";
        SqlRow row = Ebean.createSqlQuery(sql).findUnique();
        return row.getInteger("count") == 1;
    }

    @Override
    public EvaluationResult getById(long id) {
        return null;
    }

    @Override
    public void remove(EvaluationResult model) {

    }

    @Override
    public void update(EvaluationResult model) {
        throw new UnsupportedOperationException("Evaluation Results may not be changed once persisted!");
    }

}
