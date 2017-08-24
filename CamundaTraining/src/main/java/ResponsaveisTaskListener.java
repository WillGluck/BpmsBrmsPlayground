
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;

public class ResponsaveisTaskListener implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        if (Main.print)
            System.out.println("TaskListener executado");        
    }

}
