import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class ResponsaveisJavaDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        System.out.println("JavaDelegate executado");
    }

}
