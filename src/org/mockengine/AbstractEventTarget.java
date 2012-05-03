package org.mockengine;


import java.util.Collection;

import org.mockengine.*;



import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public abstract class AbstractEventTarget implements EventTarget {
	private Multimap<String, Handler> bubbleEvents;
	
	public AbstractEventTarget() {
		bubbleEvents = ArrayListMultimap.create();
	}
	
	public Collection<Handler> getEventHandlers(String name) {
		return bubbleEvents.get(name);
	}
	
	public void addEventListener(String type, Handler listener, boolean useCapture) {
		if (useCapture) {
			throw new UnsupportedOperationException("Capture not implemented yet");
		}
		bubbleEvents.put(type, listener);
	}

	public void addEventListener(String type, Handler listener) {
		addEventListener(type, listener, false);
	}

	public void removeEventListener(String type, Handler listener) {
		throw new UnsupportedOperationException("Not implemented yet");
	}	

}
