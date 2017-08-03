import java.util.List;
import java.util.Scanner;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.Lane;
import org.activiti.engine.FormService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

/*
 * FormProperties
 * 
 * enum validacaooDocumento (devolver, reprovar, encaminhar)
 *  
 * 
 * 
 */
public class Main {
    
    static ProcessEngine processEngine;
    static RepositoryService repositoryService;
    static RuntimeService runtimeService;
    static TaskService taskService;
    static FormService formService;
    static Scanner scanner;
    
    @SuppressWarnings("deprecation")
    public static void main(String[] args) {

        processEngine = ProcessEngines.getDefaultProcessEngine();
        
        repositoryService = processEngine.getRepositoryService();
        for (Deployment deployment : repositoryService.createDeploymentQuery().list()) {
            repositoryService.deleteDeploymentCascade(deployment.getId());
        }        
        repositoryService.createDeployment().addClasspathResource("diagrams/inContract_-_Souza_Cruz_sem_pools.bpmn20.xml").deploy();
        
        runtimeService = processEngine.getRuntimeService();        
        taskService = processEngine.getTaskService();
        formService = processEngine.getFormService();
        
        scanner = new Scanner(System.in);
        
        while (true) {
            
            System.out.println("\n");
            System.out.println("inContract - Bem vindo escolha uma opção:");
            System.out.println("1 - Iniciar processo");
            System.out.println("2 - Listar processos");
            
            
            String option = scanner.nextLine();
            
            switch (option.trim()) {            
                case "1":
                    iniciarProcesso();
                    break;
                case "2":
                    listarProcessos();
                    break;
                default:
                    System.out.println("Opção inválida");                                   
            }             
        }        
    }
    
    public static void iniciarProcesso() {
        runtimeService.startProcessInstanceByKey("process");
    }
    
    public static void listarProcessos() {
        System.out.println("\n");
        System.out.println("Processos ativos:");        
        for (ProcessInstance processInstance : runtimeService.createProcessInstanceQuery().list()) {            
            Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();            
            System.out.println("Id do processo: " + processInstance.getId() + "   Task atual: " + task.getName());            
            getCurrentLaneForProcessInstance("", processInstance.getProcessDefinitionId());
        }
    }
    
    public static void executarProcesso() {
        
    }
    
    public static Lane getCurrentLaneForProcessInstance(String processInstanceId, String processDefinitionId) {
        
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        System.out.println("Encontramos " + bpmnModel.getProcesses().size() + " Processes");
        
        List<Lane> lanes = bpmnModel.getProcess(processInstanceId).getLanes();
            
        System.out.println("Achou " + lanes.size() + " lanes");
        
        return null;
    }
    
}
