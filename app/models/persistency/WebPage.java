package models.persistency;

/**
 * Entity Object
 * Stores information about a WebPage
 */

import java.net.URL;
import java.net.MalformedURLException;
import java.util.List;
import java.util.ArrayList;
import javax.persistence.*;

import play.db.ebean.Model;
import play.data.validation.Constraints.*;

import daos.EvaluationResultDAO;
import url.URLHandler;

import play.Logger;
import play.Logger.ALogger;

@Entity
public class WebPage extends Model {

    private static final ALogger logger = Logger.of(WebPage.class);

    public WebPage(String url) {
        this.setUrl(url);
    }

    public WebPage(URL url) {
        this.url = url;
        this.setId();
    }

    @Id
    public int id;
    public void setId() {
        this.id = this.hashCode();
    }

    /* This WebPage's url as fetched */
    @Column(unique=true)
    @Required
    public URL url;
    public void setUrl(String url) {
        this.url = URLHandler.getInstance().create(url);
    }

    /* All links of this page */
    @OneToMany(cascade=CascadeType.PERSIST, mappedBy="parentPage")
    public List<Hyperlink> hyperlinks;

    /* All evaluation results of this Page */
    @OneToMany(cascade=CascadeType.PERSIST)
    public List<EvaluationResult> evaluations = new ArrayList<>();

    public void addEvaluationResult(EvaluationResult e) {
        this.evaluations.add(e);
    }

    /**
     * Loads all the Evaluation results for this webpage
     * using the EvaluationResultDAO (not automatically loadad due to LazyLoad)
     * @return EvaluationResults for this WebPage
     */
    public List <EvaluationResult> getEvaluations() {
        return new EvaluationResultDAO().getByWebPageId(this.id);
    }

    @Override
    public String toString(){
        return this.url.toString();
    }

    @Override
    public boolean equals(Object o){
        return this.url.equals(((WebPage)o).url);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = hash * 31 + this.url.getHost().hashCode();
        return hash;
    }

    /**
     * Returns a List with all the urls from a list of webPages
     * @param  webPages List of webpage Objects
     * @return          List of urls from the passed webPage objects
     */
    public static List<String> getUrls(List<WebPage> webPages) {
        List<String> urls = new ArrayList<>();
        for (WebPage wp : webPages) {
            urls.add(wp.url.toString());
        }
        return urls;
    }

}
