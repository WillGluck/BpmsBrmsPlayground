package com.bpmn.wrapper.camunda.classes;

import java.io.File;
import java.util.Map;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;

import com.bpmn.wrapper.common.classes.WorkflowTask;
import com.bpmn.wrapper.common.interfaces.IWorkflowEngine;
import com.bpmn.wrapper.common.interfaces.IWorkflowTask;

public abstract class AbstractCamundaEngine implements IWorkflowEngine {
    
    protected ProcessEngine processEngine;
    protected RepositoryService repositoryService;
    protected HistoryService historyService;
    protected RuntimeService runtimeService;
    protected IdentityService identityService;
    protected TaskService taskService;
    protected FormService formService;
    
    public AbstractCamundaEngine() {

        repositoryService = processEngine.getRepositoryService();
        runtimeService = processEngine.getRuntimeService();       
        taskService = processEngine.getTaskService();
        historyService = processEngine.getHistoryService();
        formService = processEngine.getFormService();
        identityService = processEngine.getIdentityService();                
    }
    
    public void deployDefinitionFromFile(File file) {
        repositoryService.createDeployment().addClasspathResource(file.getAbsolutePath()).deploy();
    }
            
    public String startProcessWithDefinitionId(String processDefinitionId) {
        return runtimeService.startProcessInstanceByKey("process").getId();
    }
    
    public IWorkflowTask getCurrentTaskFromProcessWithInstanceId(String processInstanceId) {;
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).active().singleResult();
        return new WorkflowTask(task.getTaskDefinitionKey(), task.getId());
    }
    
    public void completeTaskWithInstanceId(String taskInstanceId, Map<String, Object> variables) {
        taskService.complete(taskInstanceId, variables);
    }
    
    public void completeTaskWithInstanceId(String taskInstanceId) {
        taskService.complete(taskInstanceId);
    }
    
    public Map<String, Object> getVariablesOfTaskWithInstanceId(String taskInstanceId) {
        return taskService.getVariables(taskInstanceId);
    }
    
//    public void validateModel(String id) throws Exception {
//        
//        Model model = repositoryService.getBpmnModelInstance("");
//        Collection<UserTask> x = .getModelElementsByType(UserTask.class);
//        for (UserTask y : x) {
//            y.get
//        }
//        
//    }
    
//    public Map<String, Object> getPropertiesOfTaskFromProcessWithDefinitionId(String processDefinitionId, String taskDefinitionId) {
//        BpmnModelInstance model = repositoryService.getBpmnModelInstance(processDefinitionId);
//        Collection<CamundaProperties> properties = model.getModelElementsByType(CamundaProperties.class);
//        return null;
//    }
//    
//    public String getLaneFromDefinitionsId(String processDefinitionId, String taskDefinitionId) {
//        BpmnModelInstance model = repositoryService.getBpmnModelInstance(processDefinitionId);
//        Collection<Lane> lanes = model.getModelElementsByType(Lane.class); 
//        if (0 < lanes.size()) {
//            return lanes.stream()
//                        .filter(x -> 0 < x.getFlowNodeRefs().stream().filter(y -> y.getId().equals(taskDefinitionId)).count())
//                        .map(i -> i.getName())
//                        .collect(Collectors.toList()).get(0);    
//        }
//        return "";
//    }
    
}
