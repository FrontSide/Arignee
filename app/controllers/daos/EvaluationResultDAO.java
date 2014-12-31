package daos;

/**
*
*/

import models.persistency.EvaluationResult;
import com.avaje.ebean.Ebean;
import play.db.ebean.*;
import play.db.ebean.Model.Finder;
import play.Logger;
import play.Logger.ALogger;

public class EvaluationResultDAO implements DAO<Hyperlink> {

    private static final ALogger logger = Logger.of(EvaluationResultDAO.class);

    private static Finder<Long,EvaluationResult> find =
    new Finder<>(Long.class, EvaluationResult.class);

    public void save(EvaluationResult model) {
        logger.info("persisting model :: " + model);
        Ebean.save(model);
    }

    public Hyperlink getById(long id) {
        return null;
    }

    public void remove(HyperEvaluationResultlink model) {}

    }
