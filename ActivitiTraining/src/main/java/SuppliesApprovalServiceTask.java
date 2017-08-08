import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class SuppliesApprovalServiceTask implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        execution.setVariable("alcadaDocumento_v", new Variable("finalizar", "Finalizar"));
        execution.setVariableLocal("alcadaDocumento_v", new Variable("finalizar", "Finalizar"));
    }

}
