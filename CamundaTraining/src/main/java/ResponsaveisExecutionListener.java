
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;

public class ResponsaveisExecutionListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution execution) {
        if (Main.print)
            System.out.println("ExecutionListener executado");
    }

}
