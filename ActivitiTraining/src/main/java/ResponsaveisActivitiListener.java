import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;

public class ResponsaveisActivitiListener implements ActivitiEventListener {

    @Override
    public void onEvent(ActivitiEvent event) {
        System.out.println("ActivitiEventListener executado");
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }

}
