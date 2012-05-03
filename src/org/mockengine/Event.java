package org.mockengine;

public interface Event {
	public String getType();
	public EventTarget getTarget();
}
