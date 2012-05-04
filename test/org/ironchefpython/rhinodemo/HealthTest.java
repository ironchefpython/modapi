package org.ironchefpython.rhinodemo;

import org.ironchefpython.modapi.*;

import org.junit.Test;



public class HealthTest {

	
	public static void main(String[] args) throws Exception {
		new HealthTest().mockTest();
	}
	
	@Test
	public void mockTest() throws Exception {
		

		JsModManager mm = new JsModManager("health");
		mm.registerEvent(new CustomEventFactory("full_health", null));
		mm.registerEvent(new CustomEventFactory("no_health", null));
		mm.runScript(HealthTest.class.getResourceAsStream("health.js"), "health.js");
		
		// Create a factory that turns prototypes into Terasology Classes
//		DynamicEntitySystemFactory componentFactory = new DynamicEntitySystemFactory(mm);
//
//		// Create the HealthComponent class
//		Class<? extends DynamicComponent> HealthComponent = componentFactory.getComponent("Health");
//
//		// Create a Constructor object that makes a new HealthComponent
//		Constructor ctr = new Constructor(HealthComponent, Number.class, Number.class, Number.class);
//		
//		// Create a new HealthComponent instance
//		DynamicComponent o = ctr.newInstance(1,2,3);
//
//		// Create a getter for a HealthComponent property
//		Getter getRatio = o.new Getter(HealthComponent, "ratio");
//		
//		// Get the value for the property "ratio" on the instance o
//		Number ratio = (Number) getRatio.get(o);
//		
//		System.out.println(ratio);
//		
//		
//		DynamicSystem healthSystem = componentFactory.getSystem("Health");
//		
	}
	
}
