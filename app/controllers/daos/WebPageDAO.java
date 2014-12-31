package daos;

/**
 *
 */

import models.persistency.WebPage;
import com.avaje.ebean.Ebean; 
import play.db.ebean.*;
import play.db.ebean.Model.Finder;
import play.Logger;
import play.Logger.ALogger;


public class WebPageDAO implements DAO<WebPage> {

    private static final ALogger logger = Logger.of(WebPageDAO.class);

    private static Finder<Long,WebPage> find =
        new Finder<>(Long.class, WebPage.class);

    public void save(WebPage model) {
        logger.info("persisting model :: " + model);
        Ebean.save(model);
    }

    public WebPage getById(long id) {
        return null;
    }

    public void remove(WebPage model) {}

}
