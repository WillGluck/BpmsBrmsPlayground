import java.io.Serializable;
import java.util.HashMap;

import org.camunda.bpm.engine.impl.form.type.AbstractFormFieldType;
import org.camunda.bpm.engine.impl.form.type.EnumFormType;
import org.camunda.bpm.engine.variable.value.TypedValue;

public class Variable extends AbstractFormFieldType implements Serializable {

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
    
    @Override
    public TypedValue convertToFormValue(TypedValue propertyValue) {
        return null;
    }

    @Override
    public TypedValue convertToModelValue(TypedValue propertyValue) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object convertFormValueToModelValue(Object propertyValue) {
        
        return null;
    }

    @Override
    public String convertModelValueToFormValue(Object modelValue) {
        // TODO Auto-generated method stub
        return null;
    }    
    
}
