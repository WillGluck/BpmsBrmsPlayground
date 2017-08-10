import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

public class ResponsaveisExecutionListener implements ExecutionListener {

    private static final long serialVersionUID = -4377680826352664930L;

    @Override
    public void notify(DelegateExecution execution) {
        System.out.println("ExecutionListener executado");
    }

}
