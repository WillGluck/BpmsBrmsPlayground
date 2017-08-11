import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;

public class UserTaskListener implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        System.out.println("Oi");
    }

    
}
