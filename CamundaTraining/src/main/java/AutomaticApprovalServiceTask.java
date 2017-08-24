//package delegates;
//
//import org.camunda.bpm.engine.delegate.DelegateExecution;
//import org.camunda.bpm.engine.delegate.JavaDelegate;
//
//import Variable;
//
//public class AutomaticApprovalServiceTask implements JavaDelegate {
//
//    @Override
//    public void execute(DelegateExecution execution) {
//        execution.setVariable("aprovacaoDocumento_v", new Variable("manual", "Manual"));
//        execution.setVariableLocal("aprovacaoDocumento_v", new Variable("manual", "Manual"));
//    }
//
//}
