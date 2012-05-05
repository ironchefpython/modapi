package org.ironchefpython.rhinodemo;

import java.io.InputStreamReader;

import org.ironchefpython.modapi.*;
import org.ironchefpython.modapi.JsModManager.Console;
import org.ironchefpython.modapi.JsModManager.Facade;

import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeJavaClass;
import org.mozilla.javascript.ScriptableObject;



public class EventSendTest {

	
	public static void main(String[] args) throws Exception {
		new EventSendTest().mockTest();
	}

	private Context cx;
	private ScriptableObject scope;
	
	@Test
	public void mockTest() throws Exception {
		
        cx = Context.enter();
        scope = cx.initStandardObjects();

        ScriptableObject.putProperty(scope, "console", Context.javaToJS(new Console(), scope));
        ScriptableObject.putProperty(scope, "event1", new NativeJavaClass(scope, ZeroArgEvent.class));
        ScriptableObject.putProperty(scope, "event3", Context.javaToJS(OneArgEvent.class, scope));
        ScriptableObject.putProperty(scope, "event4", Context.javaToJS(TwoArgEvent.class, scope));

        cx.compileReader(new InputStreamReader(HealthTest.class.getResourceAsStream("event_test.js")), "event_test.js", 1, null).exec(cx, scope);
        
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
	
	interface Event {
		
	}
	
	public static class ZeroArgEvent implements Event {
		public ZeroArgEvent() {
			
		}
	}

	public static class OneArgEvent implements Event {
		
	}
	
	public static class TwoArgEvent implements Event {
		
	}
	
	public static class SendTarget {
		void send(Event e) {
			
		}
	}
}
