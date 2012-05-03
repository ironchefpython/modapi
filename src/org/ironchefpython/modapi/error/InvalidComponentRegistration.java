package org.ironchefpython.modapi.error;

public class InvalidComponentRegistration extends GeneralModdingException {
	private static final long serialVersionUID = 1L;

	public InvalidComponentRegistration(String message) {
		super(message);
	}

	public static InvalidComponentRegistration duplicateRegistration(String id) {
		return new InvalidComponentRegistration(
				"Component already registered with name '" + id + "'");
	}

	public static InvalidComponentRegistration missingId() {
		return new InvalidComponentRegistration(
				"Component requires id property");
	}
	
	public static InvalidComponentRegistration unregisteredComponent(String id) {
		return new InvalidComponentRegistration(
				"No Component registered with id '" + id + "'");
	}

	public static InvalidComponentRegistration unregisteredPrototype(String id) {
		return new InvalidComponentRegistration(
				"No Prototype registered with id '" + id + "'");
	}

	
}
