//package delegates;
//
//import org.camunda.bpm.engine.delegate.DelegateExecution;
//import org.camunda.bpm.engine.delegate.JavaDelegate;
//
//import Variable;
//
//public class SuppliesApprovalServiceTask implements JavaDelegate {
//
//    @Override
//    public void execute(DelegateExecution execution) {
//        execution.setVariable("alcadaDocumento_v", new Variable("finalizar", "Finalizar"));
//        execution.setVariableLocal("alcadaDocumento_v", new Variable("finalizar", "Finalizar"));
//    }
//
//}
