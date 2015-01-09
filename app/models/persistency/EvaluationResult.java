package models.persistency;

/**
* Entity Object
* Stores a whole EvaluationResult for a Webpage
*/

import play.db.ebean.Model;
import play.data.validation.Constraints.*;
import javax.persistence.*;
import java.util.List;
import org.json.JSONObject;
import models.evaluation.EvaluationValue;

@Entity
public class EvaluationResult extends Model {

    public EvaluationResult(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    @Id
    public long id;

    /**
     * Stores the number of the ticket with which this EvaluationResult has
     * been requested
     */
    @Column(unique=true)
    @Required
    public String ticketNumber;

    /**
    * Stores the complete Evaluation-Result (K,V-Map)
    * as String (overwritten setters and getters due to type conversion)
    */
    @Lob
    public String result;

    public void setResult (JSONObject result) {
        this.result = result.toString();
    }

    public EvaluationValue getResult() {
        return null;
    }

    @Override
    public String toString() {
        return "[" + this.id + " : " + this.ticketNumber + "]";
    }

}
