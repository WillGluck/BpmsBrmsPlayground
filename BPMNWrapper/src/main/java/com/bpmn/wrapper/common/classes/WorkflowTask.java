package com.bpmn.wrapper.common.classes;

import com.bpmn.wrapper.common.interfaces.IWorkflowTask;

public class WorkflowTask implements IWorkflowTask {
    
    private String definitionId;
    private String instanceId;
    
    public WorkflowTask(String definitionId, String instanceId) {
        this.definitionId = definitionId;
        this.instanceId = instanceId;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getDefinitionId() {
        return definitionId;
    }

    public void setDefinitionId(String definitionId) {
        this.definitionId = definitionId;
    }
    
}
