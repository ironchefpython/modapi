package org.ironchefpython.modapi;

import java.util.Map;

import org.mockengine.Event;

public interface EventFactory {
	Event makeEvent(Map<String, Object> params); 
	String getType();
}
