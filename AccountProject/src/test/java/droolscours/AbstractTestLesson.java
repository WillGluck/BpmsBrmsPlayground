package droolscours;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;

import util.KnowledgeSessionHelper;

@SuppressWarnings("restriction")
public class AbstractTestLesson {

	StatelessKieSession sessionStateless = null;
	KieSession sessionStateful = null;
	static KieContainer kieContainer;
	

	@BeforeClass
	public static void beforeClass() {
		kieContainer = KnowledgeSessionHelper.createRuleBase();
	}
	
	@Before
	public void setUp() throws Exception {
		System.out.println("\n-----------Before----------");
	}
	
	@After
	public void tearDown() throws Exception {
		System.out.println("------------After----------\n");
	}
	
}
