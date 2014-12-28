package collectors;

/** Single-Class Composite Pattern for CollectorValues
  * that are stored in the Result-Map of a Collector next to the CollectorKey
  *
  * A CollectorValue can now either be a String or a List of Strings
  */
  
import java.util.List;
import java.util.ArrayList;
  
public class CollectorValue {
  
    private String value;
    private List<String> list;
    
    public CollectorValue() {}
    public CollectorValue(List<String> list) {
        this.list = list;
    }
    public CollectorValue(String value) {
        this.value = value;
    }
    
    /**
      * Adds a String to the Collector-Values
      * If no value is stored 
      */
    public CollectorValue add(String value) {
        
        //If there is no value so far, just add the String 
        //to the object's "value" attribute
        if (this.getValue() == null) {
            this.value = value;
            return this;
        }
        
        //If there is a single String "value" existing
        //create a list and add the existing String to it
        if (this.getValue() instanceof String) {
            this.list = new ArrayList<>();
            this.list.add(this.value);
            this.value = null;
        } 
        
        this.list.add(value);
        return this;
    }
    
    public CollectorValue add(List<String> list) {
        for (String s : list) {
            this.add(s);
        }
        return this;
    }
    
    public CollectorValue add(CollectorValue value) {
        if (value.getValue() instanceof List) add((List<String>) value.getValue());
        else if (value.getValue() instanceof String) add((String) value.getValue());
        return this;
    }
    
    public Object getValue() {
        return (this.list == null) ? this.value : this.list;
    }
    
    public List<String> getList() {
        if (this.list != null) return this.list;
        List<String> list = new ArrayList<String>();
        list.add((String) this.getValue());
        return list;
    }
        
    @Override
    public String toString() {
        return this.getValue().toString();
    }
    
    
  
}
