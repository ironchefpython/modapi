package org.ironchefpython.modapi.error;

public class PropertyError extends GeneralModdingException {
	private static final long serialVersionUID = 1L;

	public PropertyError(String message) {
		super(message);
	}

	public static PropertyError IllegalAccessException(String cls) {
		return new PropertyError(
				"Cannot change the value of a '" + cls + "' instance");
	}
}
