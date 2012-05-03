package org.ironchefpython.rhinodemo;

import org.ironchefpython.modapi.*;

import org.junit.Test;
import org.mockengine.DynamicComponent;
import org.mockengine.DynamicComponent.Getter;
import org.mockengine.DynamicComponent.Constructor;
import org.mockengine.DynamicComponentFactory;
import org.mockengine.Engine;
import org.mockengine.MockEngine;


public class HealthTest {

	
	public static void main(String[] args) throws Exception {
		new HealthTest().mockTest();
	}
	
	@Test
	public void mockTest() throws Exception {
		
		Engine game = new MockEngine();
		JsModManager mm = new JsModManager(game, "health");
		mm.registerEvent(new CustomEventFactory("full_health", null));
		mm.registerEvent(new CustomEventFactory("no_health", null));
		mm.runScript(HealthTest.class.getResourceAsStream("health.js"), "health.js");
		
		// Create a factory that turns prototypes into Terasology Classes
		DynamicComponentFactory componentFactory = new DynamicComponentFactory(mm);

		// Create the HealthComponent class
		Class<? extends DynamicComponent> HealthComponent = componentFactory.makeComponent("Health");

		// Create a Constructor object that makes a new HealthComponent
		Constructor ctr = new Constructor(HealthComponent, Number.class, Number.class, Number.class);
		
		// Create a new HealthComponent instance
		DynamicComponent o = ctr.newInstance(1,2,3);

		// Create a getter for a HealthComponent property
		Getter getRatio = new Getter(HealthComponent, "ratio");
		
		// Get the value for the property "ratio" on the instance o
		Number ratio = (Number) getRatio.get(o);
		
		System.out.println(ratio);
	}
	
}
