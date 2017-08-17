import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormData;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.engine.history.HistoricVariableInstance;
import org.camunda.bpm.engine.impl.form.type.EnumFormType;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.Lane;

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
      
    //static String bpmnFileName = "diagrams/diagrama.bpmn20.xml";
    //static String bpmnFileName = "diagrams/diagrama.bpmn";
    //static String bpmnFileName = "diagrams/diagrama_testes.bpmn";
    static String bpmnFileName = "diagrams/xyz.bpmn";
    //static String bpmnFileName = "diagrams/teste.bpmn";
    
    public static void main(String[] args) {
        
        processEngine = ProcessEngines.getDefaultProcessEngine();
                
        repositoryService = processEngine.getRepositoryService();
        for (Deployment deployment : repositoryService.createDeploymentQuery().list()) {
            repositoryService.deleteDeployment(deployment.getId(), true);
        }        
        repositoryService.createDeployment().addClasspathResource(bpmnFileName).deploy();
        
        runtimeService = processEngine.getRuntimeService();        
        taskService = processEngine.getTaskService();
        historyService = processEngine.getHistoryService();
        formService = processEngine.getFormService();
        
        scanner = new Scanner(System.in);
        
        doIt();
        //doIt2();
    }
    
    public static void doIt2() {
        
        try {
            
            
            List<String> responsaveis = Arrays.asList("","","");
            Map<String, Object> variables = new HashMap<>();
            variables.put("emailResponsaveis", responsaveis);
            processInstance = runtimeService.startProcessInstanceByKey("process", variables);
            
//            BpmnModelInstance bpmnModel = repositoryService.getBpmnModelInstance(processInstance.getProcessDefinitionId());
//            List<Lane> lanes = (List<Lane>) bpmnModel.getDefinitions().getChildElementsByType(Lane.class);
            
            Task task = getCurrentTask();
            while(null != task) {
                
                test();
                                                        
                //Object loopCounter = task.getTaskLocalVariables().get("loopCounter");
                
                FormData formData = formService.getTaskFormData(task.getId());
                
                List<FormField> formProperties = formData.getFormFields();
                formProperties.forEach(i -> {
                    //System.out.println("Informe o valor da variável " + i.getName() + ":");
                    //String emailResponsavel = scanner.nextLine();
                    System.out.println(i.getLabel());
                });
                
                taskService.complete(task.getId());
                task = getCurrentTask();            
            } 
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    protected static void test() throws IOException {
        
//        String processInstanceId = processInstance.getId();
//        
//        BpmnModel pde = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
//        
//        ProcessDiagramGenerator diagramGenerator = processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator();
//        
//        InputStream inputStream = diagramGenerator.generateDiagram(pde, "png", runtimeService.getActiveActivityIds(processInstanceId));        
//        byte[] buffer = new byte[inputStream.available()];
//        
//        File file = new File("D:/" + UUID.randomUUID().toString() + ".png");
//        OutputStream outputStream = new FileOutputStream(file); 
//        outputStream.write(buffer);
//        outputStream.flush();
//        outputStream.close();
//        inputStream.close();
    }
    
    public static void doIt() {
        
        while (true) {
            
            System.out.println("\n");
            System.out.println(mainSeparator);
            System.out.println("inContract - Bem vindo escolha uma opção:");
            System.out.println(secondarySeparator);
            System.out.println("1 - Iniciar processo");
            System.out.println("2 - Executar um processo");
            System.out.println("3 - Ver histórico de um processo");
            //System.out.println("4 - Verificar prazo de um processo");
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
//                case "4":
//                    listarProcessosEExecutar(() -> {prazos();});
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
        //taskService.createTaskQuery().processInstanceId(processInstance.getId()).orderByDueDate().asc().dueBefore(new Date()).dueAfter(new Date()).active().singleResult();

        List<HistoricActivityInstance> activities = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstance.getId())
                .orderByHistoricActivityInstanceStartTime().asc()
                .orderByHistoricActivityInstanceEndTime().asc()
                .list()                
                .stream().filter(i -> ("serviceTask".equals(i.getActivityType()) || "userTask".equals(i.getActivityType()))).collect(Collectors.toList());
        
        BpmnModelInstance model = repositoryService.getBpmnModelInstance(processInstance.getProcessDefinitionId());
       
        for (HistoricActivityInstance activity : activities) {

            String variable = "indefinida";
            if (null != activity.getTaskId() && !"".equals(activity.getTaskId())) {
                HistoricVariableInstance variableHistory = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstance.getId()).taskIdIn(activity.getTaskId()).singleResult();
                if (null != variableHistory)                 
                    variable = (String) variableHistory.getValue();
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
        
        BpmnModelInstance model = repositoryService.getBpmnModelInstance(processInstance.getProcessDefinitionId());                
        Task task = getCurrentTask();
        
        while (null != task) {
                        
            String laneName = getLaneNameForTaskDefinitionIdFromModel(model, task.getTaskDefinitionKey());            
            
            System.out.println(mainSeparator);                
            System.out.println("Task atual: " + task.getName() + ", Categoria: " + laneName);
            System.out.println(secondarySeparator);
            
            FormData formData = formService.getTaskFormData(task.getId());
            List<FormField> formProperties = formData.getFormFields();
            
            Map<String, Object> variables = new HashMap<>();
            
            for (FormField formProperty : formProperties) {
                
                if (formProperty.getType() instanceof EnumFormType) {
                    
                    
                    if ("envioDeEmail".equals(formProperty.getId())) {
                        
                        @SuppressWarnings("unchecked")                    
                        Map<String, String> values = (Map<String, String>) formProperty.getType().getInformation("values");
                        for (Entry<String, String> entry : values.entrySet()) {
                            runtimeService.setVariable(processInstance.getId(), "enviarEmail", entry.getValue());
                            runtimeService.signalEventReceived("enviarEmail");
                        }                            
                        
                    } else {
                        //Resto
                    }
                    
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
                            //variables.put(formProperty.getId() + "_v", new Variable(key, values.get(key)));
                            variables.put(formProperty.getId(), key);
                        } else {
                            System.out.println("Opção inválida");
                        }
                    } while (!values.containsKey(key));
                    
                } else {
                    System.out.println("Informe o valor para o campo " + formProperty.getLabel() + ":");
                    System.out.println(mainSeparator);
                    String valor = scanner.nextLine();
                    //variables.put(formProperty.getId() + "_v", valor);
                    variables.put(formProperty.getId(), valor);
                }
            }
            
            taskService.setVariablesLocal(task.getId(), variables);
            taskService.complete(task.getId(), variables);
                                        
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
        
        System.out.println("Fluxo finalizado");
    }
    
    static void prazos() {        
        System.out.println("Não implementado");        
    }
    
    //TODO
    public static String getLaneNameForTaskDefinitionIdFromModel(BpmnModelInstance model, String taskDefinitionId) {
//        for (Lane lane : model.getModelElementsByType(Lane.class)) {
//            for (FlowNode flowNode : lane.getFlowNodeRefs()) {
//                if (taskDefinitionId.equals(flowNode.getId())) {
//                    return lane.getName();
//                }
//            }
//        }
        return model.getModelElementsByType(Lane.class)
            .stream()
            .filter(x -> 0 < x.getFlowNodeRefs().stream().filter(y -> y.getId().equals(taskDefinitionId)).count())
            .map(i -> i.getName()).collect(Collectors.toList()).get(0);
        //return "";
    }
    
    public static Task getCurrentTask() {
        return taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
    }
    
    public static Task getCurrentTaskForProcessInstanceId(String processInstanceId) {
        return taskService.createTaskQuery().processInstanceId(processInstanceId).active().singleResult();
    }
    
    
}
