
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class ResponsaveisJavaDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        if (Main.print)
            System.out.println("Notificando");
    }

}
