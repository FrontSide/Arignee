package models.persistency;

/**
 * Entity Object
 * Stores a Hyperlink with all its attributes and
 * Relations
 */

import play.db.ebean.Model;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
public class Hyperlink extends Model {

    @Id
    public long id;

    /* The Webpage this Link is embedded in */
    public WebPage parentPage;

    /* The value of this Link's href Attribut */
    public String target;

    /* The Link's Text as it is seen on the Webpage */
    public String text;

    @Override
    public String toString(){
        return "<a " + "href=\"" + target + "\"> " + text + " </a>";
    }

    @Override
    public boolean equals(Object o){
        /*TODO*/
        return false;
    }

    /**
    * Returns a List with all the urls/hrefs from a list of hyperlinks
    * @param  hyperlinks    List of hyperlink Objects
    * @return               List of urls/hrefs from the passed hyperlink objects
    */
    public static List<String> getUrls(List<Hyperlink> hyperlinks) {
        List<String> urls = new ArrayList<>();
        for (Hyperlink h : hyperlinks) {
            urls.add(h.target);
        }
        return urls;
    }

}
