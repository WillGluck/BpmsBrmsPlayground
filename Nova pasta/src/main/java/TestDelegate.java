import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class TestDelegate implements JavaDelegate {

    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("Olá eu sou o JavaDelegate maroto do fluxo simplificado vlw flw");
    }

}
