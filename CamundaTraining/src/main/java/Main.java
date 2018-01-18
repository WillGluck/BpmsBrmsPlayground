import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.IdentityService;
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
import org.camunda.bpm.engine.repository.DeploymentWithDefinitions;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.Lane;


/**
 * Main genérico para testes de fluxos bpmn criados utilizando a biblioteca Camunda.
 * 
 */
public class Main {
    
    /**
     * Flag Global que verifica se os "syout" devem ser executados.
     */
    static Boolean print = true;
    
    /*
     * Engine e services diversos disponibilizados pela API Java do Camunda. 
     */
    static ProcessEngine processEngine;
    static RepositoryService repositoryService;
    static HistoryService historyService;
    static RuntimeService runtimeService;
    static IdentityService identityService;
    static TaskService taskService;
    static FormService formService;
    
    
    /**
     * Scanner global iniciado com inputStream apontando para o System.in. Utilizar para ler inputs do usuário com {@link Scanner#nextLine()}
     */
    static Scanner scanner;
    /**
     * Lista mantendo todos as instâncias de processos iniciados durante a execução corrente (sem utilidade nenhuma, criado apenas para testes de uso de memória)
     */
    static List<ProcessInstance> startedProcesses = new ArrayList<>();
    /**
     * Intância do processo. Utilizado apenas no método {@link #listarProcessosEExecutar(Runnable)}, onde é setado para uso posterior no Runnable passado como argumento.
     */
    static ProcessInstance processInstance;
    
    /*
     * Frescuras
     */
    static String mainSeparator =      "####################################################################################################################";
    static String secondarySeparator = "--------------------------------------------------------------------------------------------------------------------";
    static String tableFileName = "tables/inContract_Modelo_Aprovação_Padrão.dmn";
    static String bpmnFileName = "diagrams/inContract_Modelo_Aprovação_Padrão.bpmn";    
    //static String bpmnFileName = "diagrams/teste_template.bpmn";
    //static String bpmnFileName = "diagrams/novos_testes.bpmn";
    
    static String ID;
    
    /** 
     * Aqui a magia acontece
     * 
     *  @throws Exception
     */
    public static void main(String[] args) throws Exception {     
        
        initCamundaProcessEngine();        
        scanner = new Scanner(System.in);        
        doIt();
        
    }
    
    /**
     * Método que inicializa a engine e todos os services Camunda. Sempre remove todos os processos já persistidos e faz deploy novamente. Apenas garantindo que estamos usando sempre a versão mais nova do xml.
     *
     * @throws Exception
     */
    static void initCamundaProcessEngine() throws Exception {

        //Iniciando a engine (nesse momento o arquivo camunda.cfg.xml é lido)
        Benchmark.getDefaultInstance().start("Inicializando a engine");
        processEngine = ProcessEngines.getDefaultProcessEngine();
        Benchmark.getDefaultInstance().end("Inicializando a engine");
        
        //Recupera o service responsável por tudo que é estático (definições, basicamente)
        repositoryService = processEngine.getRepositoryService();           
        
        //Limpa a base de dados de definições anteriores.
        Benchmark.getDefaultInstance().start("Limpando a base de dados");
        for (Deployment deployment : repositoryService.createDeploymentQuery().list()) {
            //repositoryService.deleteDeployment(deployment.getId(), true);
        }
        Benchmark.getDefaultInstance().end("Limpando a base de dados");
        
        //Faz deploy da nova versão para a empresa CND Brazil
        
        File file = new File("D:/teclogica/BpmsBrmsPlayground/CamundaTraining/src/main/resources/diagrams/inContract_Modelo_Aprovação_Padrão.bpmn");
        InputStream inputStream = new FileInputStream(file);
        DeploymentWithDefinitions definitionsBpmn = null;
        try {
            definitionsBpmn =  repositoryService.createDeployment().tenantId("CDN Brazil").addInputStream("xyz.bpmn", inputStream).deployWithResult();
        } finally {
            inputStream.close();
        }
        
        //DeploymentWithDefinitions definitionsBpmn =  repositoryService.createDeployment().tenantId("CDN Brazil").addClasspathResource(bpmnFileName).deployWithResult();
        DeploymentWithDefinitions definitionsDmn = repositoryService.createDeployment().tenantId("CDN Brazil").addClasspathResource(tableFileName).deployWithResult(); 
        
        testingFileRecovering(definitionsBpmn, definitionsDmn);
        
        //Recupera todos os services que iremos utilizar
        runtimeService = processEngine.getRuntimeService();        
        taskService = processEngine.getTaskService();
        historyService = processEngine.getHistoryService();
        formService = processEngine.getFormService();
        identityService = processEngine.getIdentityService();
        
        //Autentica o usuário Glück, do grupo Manager, da empresa CDN Brazil.
        identityService.setAuthentication("WillGluck", Arrays.asList("manager"), Arrays.asList("CDN Brazil")); // Todas as chamadas tem tenantId = CDN Brazil
        //identityService.clearAuthentication();
    }
    
    static void testingFileRecovering(DeploymentWithDefinitions definitionsBpmn, DeploymentWithDefinitions definitionsDmn) throws Exception {
        
        InputStream inputStream = null;
        byte[] buffer = null;
        File file = null;
        OutputStream outputStream = null;
        
        //Teste recuperação de arquivo BPMN.
        inputStream = repositoryService.getProcessModel(definitionsBpmn.getDeployedProcessDefinitions().get(0).getId());
        buffer = new byte[inputStream.available()];        
        inputStream.read(buffer);
        inputStream.close();
        
        file = new File("temp.bpmn");
        outputStream = new FileOutputStream(file);
        outputStream.write(buffer);
        outputStream.flush();
        outputStream.close();
        
        //Teste recuperação de arquivo DMN
        inputStream = repositoryService.getDecisionModel(definitionsDmn.getDeployedDecisionDefinitions().get(0).getId());
        buffer = new byte[inputStream.available()];        
        inputStream.read(buffer);
        inputStream.close();
        
        file = new File("temp.dmn");
        outputStream = new FileOutputStream(file);
        outputStream.write(buffer);
        outputStream.flush();
        outputStream.close();
    }
    
    /**
     * Exibe o menu com as opções e de acordo com a escolhida executa o que tem que executar. 
     *
     * @throws Exception
     */
    public static void doIt() throws Exception {
        
        while (true) {
            
            print("\n");
            print(mainSeparator);
            print("inContract - Bem vindo escolha uma opção:");
            print(secondarySeparator);
            print("1 - Iniciar processo");
            print("2 - Executar um processo");
            print("3 - Ver histórico de um processo");
            print("4 - Teste overkill");
            print(mainSeparator);
            
            String option = scanner.nextLine();
            
            switch (option.trim()) {
                case "1":
                    iniciarProcesso();
                    break;
                case "2":
                    listarProcessosEExecutar(() -> {try { executarProcesso(processInstance, null); } catch (Exception e) { e.printStackTrace(); }});                    
                    break;
                case "3":
                    listarProcessosEExecutar(() -> {try { historico(processInstance, false); } catch (Exception e) { e.printStackTrace(); }});
                    break;
                case "4":
                    ultraTestFullStackOverkill();
                    break;
                default:
                    print("Opção inválida");         
                    break;
            }             
        }   
    }
    
    /**
     * Incializa uma nova instância de um processo e inicia a execução.
     * 
     * @throws Exception
     */
    public static void iniciarProcesso()  throws Exception {
    
        print("Processo iniciado");
        processInstance = runtimeService.startProcessInstanceByKey("Process_1");
        startedProcesses.add(processInstance);
        
        executarProcesso(processInstance, null);

    }
    
    /**
     * Busca todos os processos instanciados atualmente e exibe ao usuário. Após o usuário selecionar um executa o Runnable passado.
     * 
     * @param execution
     */
    public static void listarProcessosEExecutar(Runnable execution) {
        
        print("\n");
        print(mainSeparator);
        print("Processos ativos:");        
        print(secondarySeparator);
        for (ProcessInstance processInstance : runtimeService.createProcessInstanceQuery().list()) {         
            Task task = getCurrentTaskForProcessInstanceId(processInstance.getId());                   
            print("Id do processo: " + processInstance.getId() + ", Responsável: " + task.getAssignee() + ", Atividade atual: " + task.getName());
        }
        print(secondarySeparator);
        print("Pressione enter para voltar ou passe o id de um processo para proceder.");
        print(mainSeparator);
        String value = scanner.nextLine();
        switch (value) {
        case "":
            return;
        default:
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(value).list().get(0);
            if (null == processInstance) {
                print("Id do processo passada é inválida.");
            } else {
                Main.processInstance = processInstance;
                execution.run();
            }            
        }           
    }
    
    /**
     * Exibe o histórico do processo (tasks, activities, ações, responsáveis, lanes, datas...)
     * 
     * @param processInstance Processo que terá o histórico exibido
     * @param waitForUserInput Se for true o usuário deverá entrar um "enter" para sair do método. Caso false o método printa o que tiver que printar e finaliza.
     * @throws Exception
     */
    public static void historico(ProcessInstance processInstance, Boolean waitForUserInput) throws Exception {
             
        //sid-72656344-BF74-4A56-A35B-FFA0DA4ED609
        //model.getProcesses().get(0).getLanes().get(0).getFlowReferences()
        //model.getMainProcess().getLanes().get(0).getFlowReferences()
        //serviceTask && userTask
                        
        //historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstance.getId()).list();
                
        print("\n");
        print(mainSeparator);
        print("Histórico de tasks do processo " + processInstance.getId());
        print(secondarySeparator);
        
        //List<HistoricDetail> detail = historyService.createHistoricDetailQuery().list();
        //taskService.createTaskQuery().processInstanceId(processInstance.getId()).orderByDueDate().asc().dueBefore(new Date()).dueAfter(new Date()).active().singleResult();

        //Benchmark.getDefaultInstance().start("History");
        
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
                List<HistoricVariableInstance> variableHistoryList = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstance.getId()).taskIdIn(activity.getTaskId()).list();
                HistoricVariableInstance variableHistory = 0 < variableHistoryList.size() ? variableHistoryList.get(0) : null;
                if (null != variableHistory)                 
                    variable = (String) variableHistory.getValue();
            }
            
            String laneName = getLaneNameForTaskDefinitionIdFromModel(model, activity.getActivityId());
            
            DateFormat format = new SimpleDateFormat("dd/mm/yyyy HH:mm:ss");
            print("Responsável: " + activity.getAssignee()
                               + ", Atividade: " + activity.getActivityName()                              
                               + ", Ação: " + variable
                               + ", Categoria: " + laneName
                               + ", Datas: " + format.format(activity.getStartTime()) + " até " + (null != activity.getEndTime() ? format.format(activity.getEndTime()) : " indefinido"));   
        }
        
        //Benchmark.getDefaultInstance().end("History");
        
        print(secondarySeparator);
        print("Pressione enter para retornar");
        if (waitForUserInput)
            scanner.nextLine();
        print(mainSeparator);                    
    }
    
    public static Map<String, Object> executarProcessoCoreForm(Task task, Boolean hadPassedFormInputs, List<String> formInputs, Integer index) {
        
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
                
                print("Escolha uma opção de valor para o campo " + formProperty.getId() + ":");                    
                
                @SuppressWarnings("unchecked")                    
                Map<String, String> values = (Map<String, String>) formProperty.getType().getInformation("values");
                for (Entry<String, String> entry : values.entrySet()) {
                    print(entry.getKey() + " (" + entry.getValue() + ")");
                }
                print(mainSeparator);
                String key = null;    
                do  {
                    key = hadPassedFormInputs ? formInputs.get(index) : scanner.nextLine();
                      
                    if (values.containsKey(key)) {
                        //variables.put(formProperty.getId() + "_v", new Variable(key, values.get(key)));
                        variables.put(formProperty.getId(), key);
                    } else {
                        print("Opção inválida");
                    }
                } while (!values.containsKey(key));
                
            } else {
                print("Informe o valor para o campo " + formProperty.getId() + ":");
                print(mainSeparator);                    
                String valor = hadPassedFormInputs ? formInputs.get(index) : scanner.nextLine();
                //variables.put(formProperty.getId() + "_v", valor);
                variables.put(formProperty.getId(), valor);
            }
        }
        
        return variables;
        
    }
    
    public static Map<String, Object>  executarProcessoCoreInputOutput(Task task, Boolean hadPassedFormInputs, List<String> formInputs, Integer index) {

        Map<String, Object> variables = new HashMap<>();        
        Map<String, Object> taskVariables = taskService.getVariables(task.getId());
        
        print("Escolha uma opção de valor para o campo :");
        
        for (Entry<String, Object> entry : taskVariables.entrySet())                
            print(entry.getKey() + " (" + entry.getValue() + ")");
        
        print(mainSeparator);
        String key = null;

        do  {                
            key = hadPassedFormInputs ? formInputs.get(index) : scanner.nextLine();                  
            
            if (taskVariables.containsKey(key)) {         
                variables.put("resultadoAtividadeUsuario", taskVariables.get(key));
            } else {
                print("Opção inválida");
            }
        } while (!taskVariables.containsKey(key));
        
        return variables;
    }

    /**
     * Executa o processo passo a passo.
     * 
     * @param processInstance Processo que será executado.
     * @param formInputs Lista para "automatizar" input de dados. Caso seja passada o scanner será ignorado e o método finalizará ao acabarem os formInputs.
     * @throws Exception
     */
    public static void executarProcesso(ProcessInstance processInstance, List<String> inputs) throws Exception {

        Boolean hadPassedInputs = !(null == inputs || inputs.isEmpty());
        
        print("\n");        
        print(mainSeparator);
        print("Executando instância " + processInstance.getId() + " da definição " + processInstance.getProcessDefinitionId());
        print(mainSeparator);
        print("\n");
        
        BpmnModelInstance model = repositoryService.getBpmnModelInstance(processInstance.getProcessDefinitionId());      
                
        Task task = getCurrentTaskForProcessInstanceId(processInstance.getId());
        
        Integer index = 0;
        while (null != task) {
            
//            Collection<CamundaProperties> properties = model.getModelElementsByType(CamundaProperties.class);
//            for (CamundaProperties camundaProperties : properties) {
//                for (CamundaProperty property: camundaProperties.getCamundaProperties()) {
//                    System.out.println("Value: " + property.getCamundaValue());
//                    System.out.println("Name: " + property.getCamundaName());
//                }
//                System.out.println("\n");                
//            }
                        
            String laneName = getLaneNameForTaskDefinitionIdFromModel(model, task.getTaskDefinitionKey()); //XXX aqui escolhe a implementação para exibir as opções para o usuário.            
            
            print(mainSeparator);                
            print("Task atual: " + task.getName() + ", Categoria: " + laneName);
            print(secondarySeparator);
            
            //Map<String, Object> variables = executarProcessoCoreForm(task, hadPassedInputs, inputs, index);
            Map<String, Object> variables = executarProcessoCoreInputOutput(task, hadPassedInputs, inputs, index);
            index++;
                        
            //print("\n" + String.join("\n", taskService.getVariables(task.getId()).entrySet().stream().map(i -> i.getKey() + ":" + i.getValue()).collect(Collectors.toList())) + "\n");
            
            Exception e;
            do {
                try {                    
                    e = null;
                    taskService.setVariablesLocal(task.getId(), variables);
                    taskService.complete(task.getId(), variables);                
                } catch (Exception exception) {
                    e = exception;
                }
            } while (null != e);                   
                                                    
            print("Pressione enter para continuar (n para sair)");   
            String continuar = null;
            if (hadPassedInputs) {
                continuar = inputs.size() == index ? "n" : "";
            } else {
                continuar = scanner.nextLine();
            }
                         
            switch (continuar) {
                case "":                
                    task = getCurrentTaskForProcessInstanceId(processInstance.getId());
                    break;
                default:
                    return;                
            }
        }
        
        print("Fluxo finalizado");
    }
    
    /**
     * Teste XIGANTE sqn
     * Altere o valor das seguintes variáveis:
     *      ${print} flag para printar as coisas no console ou não (recomendo false senão vira uma bagunça)
     *      ${amount} para quantos processos executar.
     *      ${threadAmount} para quantas threads utilizar (valores acima de 5 podem causar problema no Pool de conexões, mas ele fica "tentando" até conseguir num loop de try catch.... sai 
     *      ${formInputs} lista de ações para simular execução na definição que está sendo utilizada. "Customizar" de acordo com o diagrama que for ser utilizado. *          
     * 1. O método irá inicializar ${amount} processos
     * 2. O método irá executar ${amount} processos, de acordo com a lista de formInputs passada.
     * 3. O método irá buscar o histórico de cada um dos ${amount} processos.
     * 
     * No fim ele irá printar o Benchmark de cada ação.
     * NOTA: caso problemas ocorram no pool de conexões do banco erros vão ser printados. É só ignorar.
     * 
     * 
     * @throws Exception
     */
    public static void ultraTestFullStackOverkill() throws Exception {
        
        //Configure aqui
        print = false;
        Integer amount = 1000;
        Integer threadAmount = 5;
        List<String> formInputs = Arrays.asList("Encaminhar", "9000", "Assumir", "Encaminhar", "Aprovar", "Aprovar", "Aprovar", "Aprovar", "Aprovar"); //Aprovar);
        
        ExecutorService taskExecutor = null;
        List<ProcessInstance> currentInstances = new ArrayList<>();
        
        //Inicializando os processos
        taskExecutor = Executors.newFixedThreadPool(threadAmount);
        System.out.println(String.format("Iniciando %d processos", amount));        
        Benchmark.getDefaultInstance().start(String.format("Iniciando %d processos", amount));
        for (int i = 0; i < amount; i++) {
            taskExecutor.execute(() -> {
                currentInstances.add(runtimeService.startProcessInstanceByKey("Process_1"));
            });            
        }        
        startedProcesses.addAll(currentInstances); //Mantém na lista para teste de memória
        taskExecutor.shutdown();
        taskExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);        
        Benchmark.getDefaultInstance().end(String.format("Iniciando %d processos", amount));
        
        //Executando os processos
        taskExecutor = Executors.newFixedThreadPool(threadAmount);
        System.out.println(String.format("Executando %d processos", amount));
        Benchmark.getDefaultInstance().start(String.format("Executando %d processos", amount));
        for (ProcessInstance pi : currentInstances) {
            taskExecutor.execute(() -> {                
                try {
                    executarProcesso(pi, formInputs);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });            
        }
        taskExecutor.shutdown();
        taskExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        Benchmark.getDefaultInstance().end(String.format("Executando %d processos", amount));
        
        //Pesquisa o histórico dos processos.
        taskExecutor = Executors.newFixedThreadPool(threadAmount);
        System.out.println(String.format("Buscando histórico de %d processos", amount));
        Benchmark.getDefaultInstance().start(String.format("Buscando histórico %d processos", amount));
        for (ProcessInstance pi : currentInstances) {
            taskExecutor.execute(() -> {
                try {
                    historico(pi, false);    
                } catch (Exception e) {
                    e.printStackTrace();
                }                
            });
        }
        taskExecutor.shutdown();
        taskExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        Benchmark.getDefaultInstance().end(String.format("Buscando histórico %d processos", amount));

        //Exibe o log de execução
        System.out.println(Benchmark.getDefaultInstance().log());
        print = true;        

    }
    
    /**
     * Retorna o nome da lane da task.
     * 
     * @param model Definição do modelo de alguma definição xml de diagrama bpmn. 
     * @param taskDefinitionId Task específica que se deseja saber a Lane
     * @return
     */
    public static String getLaneNameForTaskDefinitionIdFromModel(BpmnModelInstance model, String taskDefinitionId) {
        if (0 < model.getModelElementsByType(Lane.class).size()) {
            return model.getModelElementsByType(Lane.class)
                    .stream()
                    .filter(x -> 0 < x.getFlowNodeRefs().stream().filter(y -> y.getId().equals(taskDefinitionId)).count())
                    .map(i -> i.getName()).collect(Collectors.toList()).get(0);    
        } 
        return "";        
    }
    
//    public static Task getCurrentTask() {
//        return taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
//    }
    
    /**
     * Retorna a task atual da instância
     * 
     * @param processInstanceId Instância que se deseja recuperar a task atual.
     * @return 
     */
    public static Task getCurrentTaskForProcessInstanceId(String processInstanceId) {
        return taskService.createTaskQuery().processInstanceId(processInstanceId).active().singleResult();
    }
    
    /**
     * Util para printAR
     * 
     * @param text
     */
    public static void print(String text) {
        if (print)
            System.out.println(text);
    }
    
}
