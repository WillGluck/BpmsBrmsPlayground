package org.activiti;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

public class Main {

	public static void main(String[] args) {

		//Configura engine de processos
		ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
				.setJdbcUrl("jdbc:h2:mem:activiti;DB_CLOSE_DELAY=1000")
				.setJdbcUsername("sa")
				.setJdbcPassword("")
				.setJdbcDriver("org.h2.Driver")
				.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
		
		//Faz deploy da definição do processo no repositório
		ProcessEngine processEngine = cfg.buildProcessEngine();		
		RepositoryService repositoryService = processEngine.getRepositoryService();
		repositoryService.createDeployment().addClasspathResource("org/activiti/VacationRequest.bpmn20.xml").deploy();
		
		System.out.println("Number of process definitions: " + repositoryService.createProcessDefinitionQuery().count());
		
		//Variáveis do processo
		Map<String, Object> variables = new HashMap<>();
		variables.put("employeeName", "Kermit");
		variables.put("numberOfDays", new Integer(4));
		variables.put("vacationMotivation", "I'm really tired!");
		
		//Inicializa o processo
		RuntimeService runtimeService = processEngine.getRuntimeService();
		ManagementService managementService = processEngine.getManagementService();
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("vacationRequest", variables);
		
		System.out.println("Number of process instances: " + runtimeService.createProcessInstanceQuery().count());
		
		//Recupera lista de tasks
		TaskService taskService = processEngine.getTaskService();
		List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("management").list();
		for (Task task : tasks) {
			System.out.println("Task variable: " + task.getName());
		}
		
		//Completando uma task do usuário
		Task task = tasks.get(0);
		Map<String, Object> taskVariables = new HashMap<>();
		taskVariables.put("vacationApproved", "false");
		taskVariables.put("managerMotivation", "We have a tight deadline!");
		taskService.complete(task.getId(), taskVariables);
		
		//suspendendo e ativando um processo
		repositoryService.suspendProcessDefinitionByKey("vacationRequest");
		//repositoryService.activateProcessDefinitionByKey("vacationRequest");
		try {
			runtimeService.startProcessInstanceByKey("vacationRequest");
		} catch (ActivitiException e) {
			e.printStackTrace();
		}
		
		//Query API
		tasks = taskService.createTaskQuery()
				.taskAssignee("kermit")
				.processVariableValueEquals("orderId", "0815")
				.orderByDueDateNullsFirst().asc().list();
		
		tasks = taskService.createNativeTaskQuery()
				.sql("select count(*) from " + managementService.getTableName(Task.class) + " T where T.NAME_ = #{taskName}")
				.parameter("taskName", "gonzoTask")
				.list();
				//.count();
		
		//Variables oiden ser passadas ao iniciar o processo ou durante execução.
		//Podem ser recuperadas também.
		//Transient variables
		
		//JUEL -> sintaxe
				
		
	}

}
