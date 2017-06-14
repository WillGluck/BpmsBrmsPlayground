package droolscours;

import org.junit.Test;
import org.kie.api.event.rule.ObjectDeletedEvent;
import org.kie.api.event.rule.ObjectInsertedEvent;
import org.kie.api.event.rule.ObjectUpdatedEvent;
import org.kie.api.event.rule.RuleRuntimeEventListener;
import org.kie.api.runtime.rule.FactHandle;

import util.KnowledgeSessionHelper;
import util.OutputDisplay;

@SuppressWarnings("restriction")
public class TestLesson1 extends AbstractTestLesson {
	
	static String sessionName = "ksession-rules"; 
	
	private RuleRuntimeEventListener getRuleRuntimeEventListener() {
		return new RuleRuntimeEventListener() {			
			@Override
			public void objectUpdated(ObjectUpdatedEvent event) {
				System.out.println("Object was updated\nnew Content\n" + event.getObject().toString());
			}			
			@Override
			public void objectInserted(ObjectInsertedEvent event) {
				System.out.println("Object inserted \n" + event.getObject().toString());
			}			
			@Override
			public void objectDeleted(ObjectDeletedEvent event) {
				System.out.println("Object retracted \n" + event.getOldObject().toString());				
			}
		};
	}
	
	@Test
	public void testFirstOne() {
		sessionStateful = KnowledgeSessionHelper.getStatefulKnowledgeSession(kieContainer, sessionName);
		OutputDisplay outputDisplay = new OutputDisplay();
		sessionStateful.setGlobal("showResults", outputDisplay);
		Account a = new Account();
		sessionStateful.insert(a);
		sessionStateful.fireAllRules();
	}
	
	@Test
	public void testRuleOneFactWithFactAndUsageOfGlobalAndCallBack() {
		sessionStateful = KnowledgeSessionHelper.getStatefulKnowledgeSession(kieContainer, sessionName);
		sessionStateful.addEventListener(getRuleRuntimeEventListener());
		Account a = new Account();
		a.setAccountNo(0);
		FactHandle handlea = sessionStateful.insert(a);
		a.setBalance(12.0);
		sessionStateful.update(handlea,  a);
		sessionStateful.delete(handlea);
		sessionStateful.fireAllRules();
		System.out.println("So you saw something ;)");
	}
	
	@Test
	public void testFirstOneTwoFireAllRules() {
		sessionStateful = KnowledgeSessionHelper.getStatefulKnowledgeSession(kieContainer, sessionName);
		OutputDisplay outputDisplay = new OutputDisplay();
		sessionStateful.setGlobal("showResults", outputDisplay);
		Account a = new Account();
		sessionStateful.insert(a);
		System.out.println("First fire all rules");
		sessionStateful.fireAllRules();
		System.out.println("Second fire all rules");
		sessionStateful.fireAllRules();
		//Não executa a segunda vez
	}
	
	@Test
	public void testFirstOneTwoFireAllRulesAndSetter() {
		sessionStateful = KnowledgeSessionHelper.getStatefulKnowledgeSession(kieContainer, sessionName);
		OutputDisplay outputDisplay = new OutputDisplay();
		sessionStateful.setGlobal("showResults", outputDisplay);
		Account a = new Account();
		sessionStateful.insert(a);
		System.out.println("First fire all rules");
		sessionStateful.fireAllRules();
		a.setAccountNo(1);
		System.out.println("Second fire all rules");
		sessionStateful.fireAllRules();
		//Não executa a segunda vez
	}
	
	@Test
	public void testFirstOneTwoFireAllRulesWithUpdateInBetween() {
		sessionStateful = KnowledgeSessionHelper.getStatefulKnowledgeSession(kieContainer, sessionName);
		OutputDisplay outputDisplay = new OutputDisplay();
		sessionStateful.setGlobal("showResults", outputDisplay);
		Account a = new Account();
		FactHandle handle = sessionStateful.insert(a);
		System.out.println("First fire all rules");
		sessionStateful.fireAllRules();
		sessionStateful.update(handle, a);
		System.out.println("Second fire all rules");
		sessionStateful.fireAllRules();
		//Executa a segunda vez, mesmo sem alterações.
		//Drools desativa a regra após executada. O comando update faz ele reconsiderar todas as regras novamente.
	}
	
	@Test
	public void testRuleOneFactThatInsertObject() {
		sessionStateful = KnowledgeSessionHelper.getStatefulKnowledgeSession(kieContainer, sessionName);		
		OutputDisplay outputDisplay = new OutputDisplay();
		sessionStateful.setGlobal("showResults", outputDisplay);
		sessionStateful.addEventListener(getRuleRuntimeEventListener());
		CashFlow a = new CashFlow();
		/*FactHandle handlea =*/ sessionStateful.insert(a);
		sessionStateful.fireAllRules();
	}

}
