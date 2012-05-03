package org.ironchefpython.modapi;

import java.util.Map;


import org.mockengine.Entity;
import org.mockengine.Event;
import org.mockengine.EventTarget;


public class CustomEvent implements Event {
//	public CustomEvent(EventManager )
	
	private EventTarget target;
	private String type;
	private Map<String, Object> params;
	
	public CustomEvent(String type, Map<String, DynamicProperty> properties, Map<String, Object> params) {
		this.type = type;
		this.params = params;
	}

	public EventTarget getCurrentTarget() {
		return target;
	}
	
	public String getType() {
		return type;
	}
	
	public Object get(String name)  {
		return params.get(name); 
	}

	public Entity getTarget() {
		return null;
	}
}
