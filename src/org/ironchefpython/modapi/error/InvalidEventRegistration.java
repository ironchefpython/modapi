package org.ironchefpython.modapi.error;

public class InvalidEventRegistration extends GeneralModdingException {
	private static final long serialVersionUID = 1L;

	public InvalidEventRegistration(String type) {
		super("Event already registered with type '" + type + "'");
	}
}
