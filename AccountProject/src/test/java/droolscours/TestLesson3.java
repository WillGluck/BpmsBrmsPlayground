package droolscours;

import org.junit.Test;
import org.kie.api.runtime.rule.FactHandle;

import util.DateHelper;
import util.KnowledgeSessionHelper;
import util.OutputDisplay;

@SuppressWarnings("restriction")
public class TestLesson3 extends AbstractTestLesson {

	static final String sessionName = "ksession-lesson3";
	
//	@Test
//	public void testInConstraint() throws Exception {
//		
//		sessionStateful = KnowledgeSessionHelper.getStatefulKnowledgeSessionWithCallback(kieContainer, sessionName);
//		OutputDisplay display = new OutputDisplay();
//		sessionStateful.setGlobal("showResults", display);
//		
//		CashFlow cashFlow = new CashFlow();
//		cashFlow.setType(CashFlow.CREDIT);
//		sessionStateful.insert(cashFlow);
//		
//		sessionStateful.fireAllRules();
//		
//	}
//	
//	@Test
//	public void testNestedAccessor() throws Exception {
//		
//		sessionStateful = KnowledgeSessionHelper.getStatefulKnowledgeSessionWithCallback(kieContainer, sessionName);
//		OutputDisplay display = new OutputDisplay();
//		sessionStateful.setGlobal("showResults", display);
//		
//		Customer customer = new Customer();
//		customer.setName("Héron");
//		customer.setSurname("Nicolas");
//		PrivateAccount pAccount = new PrivateAccount();
//		pAccount.setOwner(customer);		
//		sessionStateful.insert(pAccount);
//		
//		sessionStateful.fireAllRules();
//		
//	}
//	
//	@Test
//	public void testInOrFact() throws Exception {
//
//		sessionStateful = KnowledgeSessionHelper.getStatefulKnowledgeSessionWithCallback(kieContainer, sessionName);
//		OutputDisplay display = new OutputDisplay();
//		sessionStateful.setGlobal("showResults", display);
//		
//		Customer customer = new Customer();
//		customer.setCountry("GB");		
//		sessionStateful.insert(customer);
//		PrivateAccount account = new PrivateAccount();
//		account.setOwner(customer);
//		sessionStateful.insert(account);
//		
//		sessionStateful.fireAllRules();
//	}
//	
//	@Test
//	public void testNotCondition() throws Exception {
//		
//		sessionStateful = KnowledgeSessionHelper.getStatefulKnowledgeSessionWithCallback(kieContainer, sessionName);
//		OutputDisplay display = new OutputDisplay();
//		sessionStateful.setGlobal("showResults", display);
//		sessionStateful.fireAllRules();		
//	}
//	
//	@Test
//	public void testExistsCondition() throws Exception {
//		
//		sessionStateful = KnowledgeSessionHelper.getStatefulKnowledgeSessionWithCallback(kieContainer, sessionName);
//		OutputDisplay display = new OutputDisplay();
//		sessionStateful.setGlobal("showResults", display);
//		
//		Account pAccount = new Account();
//		sessionStateful.insert(pAccount);
//		Customer c = new Customer();
//		sessionStateful.insert(c);
//		sessionStateful.fireAllRules();
//	}
//	
//	@Test
//	public void testForAll() throws Exception {
//
//		sessionStateful = KnowledgeSessionHelper.getStatefulKnowledgeSessionWithCallback(kieContainer, sessionName);
//		OutputDisplay display = new OutputDisplay();
//		sessionStateful.setGlobal("showResults", display);
//		
//		Account a = new Account();
//		a.setAccountNo(1);
//		a.setBalance(0);
//		sessionStateful.insert(a);
//		CashFlow cash1 = new CashFlow();
//		cash1.setAccountNo(1);		
//		sessionStateful.insert(cash1);
//		CashFlow cash2 = new CashFlow();
//		cash2.setAccountNo(1);
//		sessionStateful.insert(cash2);
//		
//		//Ok
//		Account a2 = new Account();
//		a2.setAccountNo(2);
//		a2.setBalance(0);
//		sessionStateful.insert(a2);
//		CashFlow cash3 = new CashFlow();
//		cash3.setAccountNo(2);
//		sessionStateful.insert(cash3);
//
//		//Nok
//		/*
//        Account a2 = new Account();
//        a2.setAccountNo(2);
//        a2.setBalance(0);
//        sessionStateful.insert(a2);
//        CashFlow cash3 = new CashFlow();
//        cash3.setAccountNo(1);
//        sessionStateful.insert(cash3);
//		*/
//		
//		sessionStateful.fireAllRules();
//		
//	}
//	
//	@Test
//	public void testFromHLS() throws Exception {
//
//		sessionStateful = KnowledgeSessionHelper.getStatefulKnowledgeSessionWithCallback(kieContainer, sessionName);
//		OutputDisplay display = new OutputDisplay();
//		sessionStateful.setGlobal("showResults", display);
//		sessionStateful.setGlobal("serviceCustomer", new CustomerService());
//		
//		Customer c = new Customer("Héron", "Nicolas", "A");
//		sessionStateful.insert(c);
//		
//		sessionStateful.fireAllRules();
//	}
//	
//	@Test
//	public void testCollecting() throws Exception {
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
//		cash1.setMvtDate(DateHelper.getDate("2010-01-15"));
//		cash1.setAmount(1000);
//		cash1.setType(CashFlow.CREDIT);
//		cash1.setAccountNo(1);
//		sessionStateful.insert(cash1);
//		
//		CashFlow cash2 = new CashFlow();
//		cash2.setMvtDate(DateHelper.getDate("2010-02-15"));
//		cash2.setAmount(500);
//		cash2.setType(CashFlow.DEBIT);
//		cash2.setAccountNo(1);
//		sessionStateful.insert(cash2);
//		
//		CashFlow cash3 = new CashFlow();
//		cash3.setMvtDate(DateHelper.getDate("2010-04-15"));
//		cash3.setAmount(1000);
//		cash3.setType(CashFlow.CREDIT);
//		cash3.setAccountNo(1);
//		sessionStateful.insert(cash3);
//				
//		AccountingPeriod ap = new AccountingPeriod();
//		ap.setStartDate(DateHelper.getDate("2010-01-01"));
//		ap.setEndDate(DateHelper.getDate("2010-31-31"));
//		sessionStateful.insert(ap);
//		
//		sessionStateful.fireAllRules();
//	}
	
	@Test
	public void testAccumulate() throws Exception {
		
		sessionStateful = KnowledgeSessionHelper.getStatefulKnowledgeSessionWithCallback(kieContainer, sessionName);
		OutputDisplay display = new OutputDisplay();
		sessionStateful.setGlobal("showResults", display);
		
		Account a = new Account();
		a.setAccountNo(1);
		a.setBalance(0);
		sessionStateful.insert(a);
		
		CashFlow cash1 = new CashFlow();
		cash1.setMvtDate(DateHelper.getDate("2010-01-15"));
		cash1.setAmount(1000);
		cash1.setType(CashFlow.CREDIT);
		cash1.setAccountNo(1);
		FactHandle fa = sessionStateful.insert(cash1);
		
		CashFlow cash2 = new CashFlow();
		cash2.setMvtDate(DateHelper.getDate("2010-02-15"));
		cash2.setAmount(500);
		cash2.setType(CashFlow.DEBIT);
		cash2.setAccountNo(1);
		sessionStateful.insert(cash2);
		
		CashFlow cash3 = new CashFlow();
		cash3.setMvtDate(DateHelper.getDate("2010-04-15"));
		cash3.setAmount(1000);
		cash3.setType(CashFlow.CREDIT);
		cash3.setAccountNo(1);
		sessionStateful.insert(cash3);
		
		AccountingPeriod ap = new AccountingPeriod();
		ap.setStartDate(DateHelper.getDate("2010-01-01"));
		ap.setEndDate(DateHelper.getDate("2010-12-31"));
		sessionStateful.insert(ap);
		
		sessionStateful.fireAllRules();
		sessionStateful.delete(fa);
		sessionStateful.fireAllRules();
	}
	
}
