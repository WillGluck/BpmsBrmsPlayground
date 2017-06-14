package droolscours;

import org.junit.Test;

import util.KnowledgeSessionHelper;
import util.OutputDisplay;

@SuppressWarnings("restriction")
public class TestLesson4 extends AbstractTestLesson {

	public static final String sessionName4 = "ksession-lesson4";
	public static final String sessionName4a = "ksession-lesson4a";

//	@Test
//	public void testRuleFlow1() {
//		
//		sessionStateful = KnowledgeSessionHelper.getStatefulKnowledgeSessionForJBPM(kieContainer, sessionName4);
//		OutputDisplay display = new OutputDisplay();
//		sessionStateful.setGlobal("showResult", display);
//		
//		Account a = new Account();
//		sessionStateful.insert(a);
//		sessionStateful.startProcess("RF1");
//		sessionStateful.fireAllRules();		
//	}
//	
//	@Test
//	public void testRuleFlow2() {
//		
//		sessionStateful = KnowledgeSessionHelper.getStatefulKnowledgeSessionForJBPM(kieContainer, sessionName4);
//		OutputDisplay display = new OutputDisplay();
//		sessionStateful.setGlobal("showResult", display);
//		
//		Account a = new Account();
//		sessionStateful.insert(a);
//		sessionStateful.fireAllRules();
//		
//	}
	
	@Test
	public void testTuleFlow3() {
		
		sessionStateful = KnowledgeSessionHelper.getStatefulKnowledgeSessionForJBPM(kieContainer, sessionName4a);
		OutputDisplay display = new OutputDisplay();
		sessionStateful.setGlobal("showResult", display);
		
		Account a = new Account();
		a.setBalance(2500);
		sessionStateful.insert(a);
		
		AccountingPeriod period = new AccountingPeriod();
		sessionStateful.insert(period);
		
		sessionStateful.fireAllRules();		
	}
}
