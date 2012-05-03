package org.ironchefpython.modapi.error;

public class UnregisteredEventException extends Exception {
	private static final long serialVersionUID = 1L;

	public UnregisteredEventException(String id) {
		super("No Event registered with id '" + id + "'");
	}
}
