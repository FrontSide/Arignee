package evaluators;

/** 
  * This class represents an object-type which is used for 
  * saving the figures that result from Evaluations
  * That could be a String, a Float, an Integer or a Rating (enum)
  * This way those resulting figures can easily be stored in a map or list.
  * Some custom methods help retaining the figures in a nice object oriented way
  * There are no direct setters or getters for the value. The figure has to be
  * passed in through the constructor and can be obtained with the get() method.
  * No primitive data-types are allowed
  */
  
import evaluators.enums.Rating;

public class EvaluationFigure implements EvaluationResult {

    public EvaluationFigure(Object o) {
        setValue(o);
    }

    private String valueString;
    private Integer valueInt;
    private Float valueFloat;
    private Rating valueRating;
    
    private void setValue(Object o) {
    
        if (o == null) throw new NullPointerException();
        if (o instanceof String) this.valueString = (String) o;
        else if (o instanceof Integer) this.valueInt = (Integer) o;
        else if (o instanceof Float) this.valueFloat = (Float) o;
        else if (o instanceof Rating) this.valueRating = (Rating) o;
        else throw new RuntimeException("Type not allowed");
        
    }
    
    public Object get() {
        if (this.valueString != null) return this.valueString;
        if (this.valueInt != null) return this.valueInt;
        if (this.valueFloat != null) return this.valueFloat;
        if (this.valueRating != null) return this.valueRating;
        throw new RuntimeException("No Value Found");
    }
    
    @Override
    public String toString() {
        return get().toString();
    }

}
