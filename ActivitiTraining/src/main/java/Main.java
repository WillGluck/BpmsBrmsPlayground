import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormData;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.form.EnumFormType;
import org.activiti.engine.repository.Deployment;
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
    static HistoryService historyService;
    static RuntimeService runtimeService;
    static TaskService taskService;
    static FormService formService;
    static Scanner scanner;
    static ProcessInstance processInstance;
    static String mainSeparator =      "####################################################################################################################";
    static String secondarySeparator = "--------------------------------------------------------------------------------------------------------------------";
    
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
        historyService = processEngine.getHistoryService();
        formService = processEngine.getFormService();
        
        scanner = new Scanner(System.in);
        
        doIt();
     
//        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey("process").singleResult(); 
//        BpmnModel model = processEngine.getRepositoryService().getBpmnModel(processDefinition.getId());
//        List<org.activiti.bpmn.model.Process> processes = model.getProcesses();
//        List<UserTask> userTasks = new ArrayList<>();
//        for(org.activiti.bpmn.model.Process p : processes) {
//             userTasks.addAll( p.findFlowElementsOfType(UserTask.class));    
//        }        
//        System.out.println("O loco");
        
    }
    
    public static void testeLane() {
        
    }
    
    public static void doIt() {
        
        while (true) {
            
            System.out.println("\n");
            System.out.println(mainSeparator);
            System.out.println("inContract - Bem vindo escolha uma opção:");
            System.out.println(secondarySeparator);
            System.out.println("1 - Iniciar processo");
            System.out.println("2 - Executar processo");
            System.out.println("3 - Ver histórico de processos");
            System.out.println(mainSeparator);
//            System.out.println("4 - Ver Histórico de log");
//            System.out.println("5 - Ver histórico de variáveis");
            
            String option = scanner.nextLine();
            
            switch (option.trim()) {            
                case "1":
                    iniciarProcesso();
                    break;
                case "2":
                    listarProcessosEExecutar(() -> {executarProcesso();});
                    break;
                case "3":
                    listarProcessosEExecutar(() -> {historico();});
                    break;
                default:
                    System.out.println("Opção inválida");         
                    break;
            }             
        }   
    }
    
    public static void iniciarProcesso() {
        System.out.println("Processo iniciado");
        processInstance = runtimeService.startProcessInstanceByKey("process");
        executarProcesso();
    }
    
    public static void listarProcessosEExecutar(Runnable execution) {
        System.out.println("\n");
        System.out.println(mainSeparator);
        System.out.println("Processos ativos:");        
        System.out.println(secondarySeparator);
        for (ProcessInstance processInstance : runtimeService.createProcessInstanceQuery().list()) {         
            Task task = getCurrentTaskForProcessInstanceId(processInstance.getId());                   
            System.out.println("Id do processo: " + processInstance.getId() + ", Responsável: " + task.getAssignee() + ", Atividade atual: " + task.getName());
        }
        System.out.println(secondarySeparator);
        System.out.println("Pressione enter para voltar ou passe o id de um processo para proceder.");
        System.out.println(mainSeparator);
        String value = scanner.nextLine();
        switch (value) {
        case "":
            return;
        default:
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(value).singleResult();
            if (null == processInstance) {
                System.out.println("Id do processo passada é inválida.");
            } else {
                Main.processInstance = processInstance;
                execution.run();
            }            
        }
    }
    
    public static void historico() {
        
        //sid-72656344-BF74-4A56-A35B-FFA0DA4ED609
        //model.getProcesses().get(0).getLanes().get(0).getFlowReferences()
        //model.getMainProcess().getLanes().get(0).getFlowReferences()
        //serviceTask && userTask
                        
        //historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstance.getId()).list();
                
        System.out.println("\n");
        System.out.println(mainSeparator);
        System.out.println("Histórico de tasks do processo " + processInstance.getId());
        System.out.println(secondarySeparator);
        //TODO .includeProcessVariables() .includeTaskLocalVariables
        
        //List<HistoricDetail> detail = historyService.createHistoricDetailQuery().list();
        taskService.createTaskQuery().processInstanceId(processInstance.getId()).orderByDueDateNullsLast().asc().taskDueBefore(new Date()).taskDueAfter(new Date()).active().singleResult();

        List<HistoricActivityInstance> activities = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstance.getId()).orderByHistoricActivityInstanceStartTime().asc().list()
                .stream().filter(i -> ("serviceTask".equals(i.getActivityType()) || "userTask".equals(i.getActivityType()))).collect(Collectors.toList());
        
        BpmnModel model = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
        
        for (HistoricActivityInstance activity : activities) {

            String variable = "indefinida";
            if (null != activity.getTaskId() && !"".equals(activity.getTaskId())) {
                HistoricVariableInstance variableHistory = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstance.getId()).taskId(activity.getTaskId()).singleResult();
                if (null != variableHistory)                 
                    variable = ((Variable) variableHistory.getValue()).getValue();
            }
            
            String laneName = getLaneNameForTaskDefinitionIdFromModel(model, activity.getActivityId());
            
            DateFormat format = new SimpleDateFormat("dd/mm/yyyy HH:mm:ss");
            System.out.println("Responsável: " + activity.getAssignee()
                               + ", Atividade: " + activity.getActivityName()                              
                               + ", Ação: " + variable
                               + ", Categoria: " + laneName
                               + ", Datas: " + format.format(activity.getStartTime()) + " até " + (null != activity.getEndTime() ? format.format(activity.getEndTime()) : " indefinido"));   
        }
        
        System.out.println(secondarySeparator);
        System.out.println("Pressione enter para retornar");
        scanner.nextLine();
        System.out.println(mainSeparator);
        
    }

    public static void executarProcesso() {
        
        System.out.println("\n");        
        System.out.println(mainSeparator);
        System.out.println("Executando instância " + processInstance.getId() + " da definição " + processInstance.getProcessDefinitionId());
        System.out.println(mainSeparator);
        System.out.println("\n");
        
        BpmnModel model = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
                
        Task task = getCurrentTask();
        while (null != task) {
            
            if (null == task.getAssignee() || "".equals(task.getAssignee())) {
                                
                System.out.println("Atenção. Você precisa informar o responsável pela atividade " + task.getName() + ":");
                String responsavel = "";
                while ("".equals(responsavel)) {                    
                    responsavel = scanner.nextLine();
                    task.setAssignee(responsavel);
                    taskService.    claim(task.getId(), responsavel);
                }
                
            } else {
                
                String laneName = getLaneNameForTaskDefinitionIdFromModel(model, task.getTaskDefinitionKey());
                
                System.out.println(mainSeparator);                
                System.out.println("Task atual: " + task.getName() + ", Categoria: " + laneName);
                System.out.println(secondarySeparator);
                
                FormData formData = formService.getTaskFormData(task.getId());            
                List<FormProperty> formProperties = formData.getFormProperties();
                
                Map<String, Object> variables = new HashMap<>();
                
                for (FormProperty formProperty : formProperties) {
                    
                    if (formProperty.getType() instanceof EnumFormType) {
                        
                        System.out.println("Escolha uma opção de valor para o campo " + formProperty.getId() + ":");                    
                        
                        @SuppressWarnings("unchecked")                    
                        Map<String, String> values = (Map<String, String>) formProperty.getType().getInformation("values");
                        for (Entry<String, String> entry : values.entrySet()) {
                            System.out.println(entry.getKey() + " (" + entry.getValue() + ")");
                        }
                        System.out.println(mainSeparator);
                        String key = null;    
                        do  {
                            key = scanner.nextLine();  
                            if (values.containsKey(key)) {
                                variables.put(formProperty.getId() + "_v", new Variable(key, values.get(key)));
                            } else {
                                System.out.println("Opção inválida");
                            }
                        } while (!values.containsKey(key));
                        
                    } else {
                        System.out.println("Informe o valor para o campo " + formProperty.getName() + ":");
                        System.out.println(mainSeparator);
                        String valor = scanner.nextLine();
                        variables.put(formProperty.getId() + "_v", valor);
                    }
                }
                
                runtimeService.setVariables(processInstance.getId(), variables);
                taskService.complete(task.getId(), variables, true);
                                            
                System.out.println("Pressione enter para continuar (n para sair)");
                String continuar = scanner.nextLine();            
                switch (continuar) {
                    case "":                
                        task = getCurrentTask();
                        break;
                    default:
                        return;                
                }   
            }
        }    
        System.out.println("Fluxo finalizado");
    }
    
    public static String getLaneNameForTaskDefinitionIdFromModel(BpmnModel model, String taskDefinitionId) {
        return model.getProcesses().get(0).getLanes().stream()
                .filter(x -> 0 < x.getFlowReferences().stream().filter(y -> y.equals(taskDefinitionId)).count())
                .map(i -> i.getName()).collect(Collectors.toList()).get(0);
    }
    
    public static Task getCurrentTask() {
        return taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
    }
    
    public static Task getCurrentTaskForProcessInstanceId(String processInstanceId) {
        return taskService.createTaskQuery().processInstanceId(processInstanceId).active().singleResult();
    }
    
    
}
