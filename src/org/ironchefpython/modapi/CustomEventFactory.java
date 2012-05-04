package org.ironchefpython.modapi;

import java.util.*;


public class CustomEventFactory implements EventFactory {
	private Map<String, DynamicProperty> properties;
	private String type;
	
	public CustomEventFactory(String type, Map<String, DynamicProperty> properties) {
		this.type = type;
		this.properties = properties;
	}
	
//	public Event makeEvent(Map<String, Object> params) {
//		return new CustomEvent(type, properties, params);
//	}

	@Override
	public String getType() {
		return type;
	}
	
}
