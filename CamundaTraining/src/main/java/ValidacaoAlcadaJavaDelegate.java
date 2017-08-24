
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class ValidacaoAlcadaJavaDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        execution.setVariable("ServiceVariable_ValidacaoAlcada", "Não Escalar Próxima Alçada");
        execution.setVariableLocal("ServiceVariable_ValidacaoAlcada", "Não Escalar Próxima Alçada");
    }

}
