package droolscours;

import org.junit.Assert;
import org.junit.Test;

import util.DateHelper;
import util.KnowledgeSessionHelper;
import util.OutputDisplay;

@SuppressWarnings("restriction")
public class TestLesson2 extends AbstractTestLesson {

	static final String sessionName = "ksession-lesson2";
	
//	@Test
//	public void testdeuxFait1() {
//		sessionStateful = KnowledgeSessionHelper.getStatefulKnowledgeSessionWithCallback(kieContainer, sessionName);
//		OutputDisplay display = new OutputDisplay();
//		sessionStateful.setGlobal("showResults", display);
//		Account a = new Account();
//		sessionStateful.insert(a);
//		AccountingPeriod period = new AccountingPeriod();
//		sessionStateful.insert(period);
//		sessionStateful.fireAllRules();
//	}
	
//	@Test
//	public void testTwoFacts() {
//		
//		sessionStateful = KnowledgeSessionHelper.getStatefulKnowledgeSessionWithCallback(kieContainer, sessionName);
//		OutputDisplay display = new OutputDisplay();
//		sessionStateful.setGlobal("showResults", display);
//		
//		Account a = new Account();
//		a.setAccountNo(1);
//		a.setBalance(0);
//		sessionStateful.insert(a);
//		
//		CashFlow cash1 = new CashFlow();
//		cash1.setAccountNo(1);
//		cash1.setAmount(1000);
//		cash1.setType(CashFlow.CREDIT);
//		sessionStateful.insert(cash1);
//		
//		sessionStateful.fireAllRules();
//		Assert.assertEquals(a.getBalance(), 1000, 0);
//	}
	
	
//	@Test
//	public void testTwoFactsTwoCashFlowMovement() throws Exception {
//		
//		sessionStateful = KnowledgeSessionHelper.getStatefulKnowledgeSessionWithCallback(kieContainer, sessionName);
//		OutputDisplay display = new OutputDisplay();
//		sessionStateful.setGlobal("showResults", display);
//		
//		Account a = new Account();
//		a.setAccountNo(1);
//		a.setBalance(0);
//		sessionStateful.insert(a);
//		
//		CashFlow cash1 = new CashFlow();
//		cash1.setAccountNo(1);
//		cash1.setAccountNo(1000);
//		cash1.setMvtDate(DateHelper.getDate("2010-01-15"));
//		cash1.setType(CashFlow.CREDIT);
//		sessionStateful.insert(cash1);
//		
//		CashFlow cash2 = new CashFlow();
//		cash2.setAccountNo(2);
//		cash2.setAmount(1000);
//		cash2.setMvtDate(DateHelper.getDate("2010-01-15"));
//		cash2.setType(CashFlow.CREDIT);
//		sessionStateful.insert(cash2);
//		
//		sessionStateful.fireAllRules();
//		Assert.assertEquals(a.getBalance(), 1000, 0);
//	}
	
	@Test
	public void testcalculateBalance() throws Exception {
		
		sessionStateful = KnowledgeSessionHelper.getStatefulKnowledgeSessionWithCallback(kieContainer, sessionName);
		OutputDisplay display = new OutputDisplay();
		sessionStateful.setGlobal("showResults", display);
		
		Account a = new Account();
		a.setAccountNo(1);
		a.setBalance(0);
		sessionStateful.insert(a);
		
		CashFlow cash1 = new CashFlow();
		cash1.setAccountNo(1);
		cash1.setAmount(1000);
		cash1.setMvtDate(DateHelper.getDate("2016-01-15"));
		cash1.setType(CashFlow.CREDIT);
		sessionStateful.insert(cash1);
		
		CashFlow cash2 = new CashFlow();
		cash2.setAccountNo(1);
		cash2.setAmount(500);
		cash2.setMvtDate(DateHelper.getDate("2016-02-15"));
		cash2.setType(CashFlow.DEBIT);
		sessionStateful.insert(cash2);
		
		CashFlow cash3 = new CashFlow();
		cash3.setAccountNo(1);
		cash3.setAmount(1000);
		cash3.setMvtDate(DateHelper.getDate("2016-04-15"));
		cash3.setType(CashFlow.CREDIT);
		sessionStateful.insert(cash3);
		
		AccountingPeriod period = new AccountingPeriod();
		period.setStartDate(DateHelper.getDate("2016-01-01"));
		period.setEndDate(DateHelper.getDate("2016-03-31"));
		sessionStateful.insert(period);
		
		sessionStateful.fireAllRules();
		Assert.assertTrue(a.getBalance()==500);
		
	}
}
