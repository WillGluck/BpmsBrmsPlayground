import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class UserTaskListener implements TaskListener {

    private static final long serialVersionUID = 6591746500748046492L;

    @Override
    public void notify(DelegateTask delegateTask) {
        System.out.println("Oi");
    }

    
}
