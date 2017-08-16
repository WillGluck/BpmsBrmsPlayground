import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class AprovacaoAutomaticaJavaDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        execution.setVariable("ServiceVariable_AprovacaoAutomatica", false);
        execution.setVariableLocal("ServiceVariable_AprovacaoAutomatica", false);
    }

}
