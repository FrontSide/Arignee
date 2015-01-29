package daos;

/**
 *
 */

import models.persistency.WebPage;
import models.persistency.EvaluationResult;
import url.URLHandler;

import java.net.URL;
import com.avaje.ebean.Ebean;

import play.db.ebean.*;
import play.db.ebean.Model.Finder;
import play.Logger;
import play.Logger.ALogger;


public class WebPageDAO implements DAO<WebPage> {

    private static final ALogger logger = Logger.of(WebPageDAO.class);

    private static Finder<Long,WebPage> find =
        new Finder<>(Long.class, WebPage.class);

    /**
     * Adds an EvaluationResult to a WebPage.
     * If WebPage not already in DB create new WebPage
     * @param  webPage  WebPage this EvaluationResult is for
     * @param  evalres  EvaluationResult to add to the WebPage
     */
    public void addEvaluationResult(WebPage webPage, EvaluationResult evalres) {
        logger.info("connection evalres to webpage");
        logger.info("webpage id is :: " + webPage.id);
        WebPage existing = this.getById(webPage.id);
        if (existing == null) {
            logger.info("webpage does not seem to exist yet :: " + webPage);
            webPage.addEvaluationResult(evalres);
            this.save(webPage);
        } else {
            logger.info("updating webpage :: " + webPage);
            existing.addEvaluationResult(evalres);
            this.update(existing);
        }
    }

    @Override
    public void save(WebPage model) {
        logger.info("persisting model :: " + model);
        Ebean.save(model);
    }

    @Override
    public WebPage getById(long id) {
        return this.find.where().eq("id", id).findUnique();
    }

    public WebPage getByURL(URL url) {
        return this.find.where().eq("url", url.toString()).findUnique();
    }

    public WebPage getByURL(String url) {
        try {
            return this.find.where().eq("url", URLHandler.getInstance().create(url)).findUnique();
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public void remove(WebPage model) {

    }

    @Override
    public void update(WebPage model) {
        Ebean.update(model);
    }

}
