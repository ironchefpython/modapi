package org.ironchefpython.modapi;

import java.util.HashMap;
import java.util.Map;

public class EventDefinition {
	private final String type;
	private final Map<String, DynamicProperty> properties;

	public EventDefinition(String type) {
		this.type = type;
		this.properties = new HashMap<String, DynamicProperty>();
	}

	public String getType() {
		return type;
	}
	
	
	
}
