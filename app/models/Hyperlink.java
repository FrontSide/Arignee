package models;

/**
 * Entity Object
 * Stores a Hyperlink with all its attributes and
 * Relations
 */

import play.db.ebean.Model;
import javax.persistence.Entity;

@Entity
public class Hyperlink extends Model {

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

}
