package daos;

/**
*
*/

import models.persistency.EvaluationResult;
import models.persistency.Hyperlink;
import com.avaje.ebean.Ebean;
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

    @Override
    public EvaluationResult getById(long id) {
        return null;
    }

    @Override
    public void remove(EvaluationResult model) {

    }

    @Override
    public void update(EvaluationResult model) {
        Ebean.update(model);
    }

}
