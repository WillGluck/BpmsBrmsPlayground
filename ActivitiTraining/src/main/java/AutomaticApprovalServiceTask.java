import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class AutomaticApprovalServiceTask implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        execution.setVariable("aprovacaoDocumento_v", new Variable("manual", "Manual"));
        execution.setVariableLocal("aprovacaoDocumento_v", new Variable("manual", "Manual"));
    }

}
