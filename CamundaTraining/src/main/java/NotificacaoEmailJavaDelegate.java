import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class NotificacaoEmailJavaDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {        
        System.out.println("Email enviado do service " + execution.getCurrentActivityName());
    }

}
