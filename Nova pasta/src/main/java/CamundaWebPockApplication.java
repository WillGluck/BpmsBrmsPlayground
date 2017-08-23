import org.camunda.bpm.application.PostDeploy;
import org.camunda.bpm.application.ProcessApplication;
import org.camunda.bpm.application.impl.ServletProcessApplication;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.repository.Deployment;

@ProcessApplication("CamundaWebApplication")
public class CamundaWebPockApplication extends ServletProcessApplication {

    
    @PostDeploy
    public void startProcessInstance(ProcessEngine processEngine) {

        processEngine = ProcessEngines.getDefaultProcessEngine();
        
        Camunda.repositoryService = processEngine.getRepositoryService();
        for (Deployment deployment : Camunda.repositoryService.createDeploymentQuery().list()) {
            Camunda.repositoryService.deleteDeployment(deployment.getId(), true);
        }        
        Camunda.repositoryService.createDeployment().addClasspathResource(Camunda.BPMN_FILE_NAME).deploy();
        
        processEngine.getRuntimeService().startProcessInstanceByKey("process");
    }

}
