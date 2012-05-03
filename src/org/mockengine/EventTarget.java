package org.mockengine;



public interface EventTarget {
	public void addEventListener(String type, Handler handler);
}
