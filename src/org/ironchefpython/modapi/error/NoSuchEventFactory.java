package org.ironchefpython.modapi.error;


public class NoSuchEventFactory extends GeneralModdingException {
	private static final long serialVersionUID = 1L;

	public NoSuchEventFactory(String name) {
		super("No Event Factory registered with name '" + name + "'");
	}
}
