import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class ResponsaveisTaskListener implements TaskListener {

    private static final long serialVersionUID = 5766481614597558083L;

    @Override
    public void notify(DelegateTask delegateTask) {
        System.out.println("TaskListener executado");        
    }

}
