package com.bpmn.wrapper.common.interfaces;

import java.io.File;
import java.util.Map;

public interface IWorkflowEngine {

    public void deployDefinitionFromFile(File file);
            
    public String startProcessWithDefinitionId(String processDefinitionId);
   
    public IWorkflowTask getCurrentTaskFromProcessWithInstanceId(String processInstanceId);
    
    public void completeTaskWithInstanceId(String taskInstanceId, Map<String, Object> variables);
    
    public void completeTaskWithInstanceId(String taskInstanceId);
    
    public Map<String, Object> getVariablesOfTaskWithInstanceId(String taskInstanceId);
    
    public void validateModel(String processDefinitionId);
    
}
