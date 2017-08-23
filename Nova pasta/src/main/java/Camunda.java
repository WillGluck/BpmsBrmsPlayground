import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;

public class Camunda {
    
    public static String BPMN_FILE_NAME = "resources/diagrams/teste.bpmn";
    public static ProcessEngine processEngine;
    public static RepositoryService repositoryService;
    public static HistoryService historyService;
    public static RuntimeService runtimeService;
    public static TaskService taskService;
    public static FormService formService;
    
}
