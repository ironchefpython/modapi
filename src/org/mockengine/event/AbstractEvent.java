package org.mockengine.event;

import org.mockengine.Entity;
import org.mockengine.Event;


public abstract class AbstractEvent implements Event {
	private final String type;
	
	public AbstractEvent(String type) {
		this.type = type;
	}
	
	@Override
	public String getType() {
		return type;
	}


}
