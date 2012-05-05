package org.ironchefpython.modapi;

import java.util.Map;

public class EventDefinition {
	private final String type;
	private final Map<String, DynamicProperty> properties;

	public EventDefinition(String type, Map<String, DynamicProperty> properties) {
		this.type = type;
		this.properties = properties;
	}

	public String getType() {
		return type;
	}
	
	
	
}
