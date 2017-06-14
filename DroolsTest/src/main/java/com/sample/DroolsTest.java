package com.sample;

import java.math.BigDecimal;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import com.sample.ItemCity.City;
import com.sample.ItemCity.Type;

/**
 * This is a sample class to launch a rule.
 */
public class DroolsTest {

    public static final void main(String[] args) {
        try {
        	test1();
        	
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
    
    public static void test1() {
    	
        // load up the knowledge base
    	KieSession kSession = getKieSession();

        // go !
        Message message = new Message();
        message.setMessage("Hello World");
        message.setStatus(Message.HELLO);
        kSession.insert(message);
        kSession.fireAllRules();
        
    }
    
    public static void test2() {
    	
    	KieSession kSession = getKieSession();
    	
    	ItemCity item1 = new ItemCity();
    	item1.setPurchaseCity(City.PUNE);
    	item1.setTypeofItem(Type.MEDICINES);
    	item1.setSellPrice(new BigDecimal(10));
    	kSession.insert(item1);
    	
    	ItemCity item2 = new ItemCity();
    	item2.setPurchaseCity(City.PUNE);
    	item2.setTypeofItem(Type.GROCERIES);
    	item2.setSellPrice(new BigDecimal(10));
    	kSession.insert(item2);
    	
    	ItemCity item3 = new ItemCity();
    	item3.setPurchaseCity(City.NAGPUR);
    	item3.setTypeofItem(Type.MEDICINES);
    	item3.setSellPrice(new BigDecimal(10));
    	kSession.insert(item3);
    	
    	ItemCity item4 = new ItemCity();
    	item4.setPurchaseCity(City.NAGPUR);
    	item4.setTypeofItem(Type.GROCERIES);
    	item4.setSellPrice(new BigDecimal(10));
    	kSession.insert(item4);
    	
    	kSession.fireAllRules();
    	
    	System.out.println(item1.getPurchaseCity().toString() + " " + item1.getLocalTax().intValue());
    	System.out.println(item2.getPurchaseCity().toString() + " " + item2.getLocalTax().intValue());
    	System.out.println(item3.getPurchaseCity().toString() + " " + item3.getLocalTax().intValue());
    	System.out.println(item4.getPurchaseCity().toString() + " " + item4.getLocalTax().intValue());
    	
    	
    }
    
    private static KieSession getKieSession() {
        KieServices ks = KieServices.Factory.get();
	    KieContainer kContainer = ks.getKieClasspathContainer();
    	KieSession kSession = kContainer.newKieSession("ksession-rules");
    	return kSession;
    }

    

}
