import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class ValidacaoAlcadaJavaDelegate2 implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        execution.setVariable("ServiceVariable_ValidacaoAlcada", "Escalar Alçada 3");
        execution.setVariableLocal("ServiceVariable_ValidacaoAlcada", "Escalar Alçada 3");
    }

}
