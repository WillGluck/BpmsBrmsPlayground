import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;

public class ResponsaveisExecutionListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution execution) {
        System.out.println("ExecutionListener executado");
    }

}
