import java.io.Serializable;

public class Variable implements Serializable {

    private static final long serialVersionUID = 6698152451311959378L;

    public Variable(String value, String name) {
        this.value = value;
        this.name = name;
    }    

    private String value;
    private String name;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }    
    
}
