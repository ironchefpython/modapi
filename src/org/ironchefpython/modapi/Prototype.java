package org.ironchefpython.modapi;

import java.util.*;

import org.ironchefpython.modapi.DynamicValueProperty.DynamicTypeProperty;
import org.ironchefpython.modapi.error.InvalidComponentRegistration;
import org.mozilla.javascript.Callable;

public class Prototype  {
	private String id;
	private Map<String, DynamicProperty> properties;
	private Map<String, Callable> handlers;
	private DynamicProperty type;
	private ConstructorParams constructor;
	private Callable updater;

	public Prototype(String id) throws InvalidComponentRegistration {
		if (id == null) {
			throw InvalidComponentRegistration.missingId();
		}
		this.id = id;
		this.properties = new HashMap<String, DynamicProperty>();
		this.handlers = new HashMap<String, Callable>();
		
		type = new PrototypeType(this);
	}

	public String getId() {
		return id;
	}

	public void addProperty(String propName, DynamicProperty prop) {
		properties.put(propName, prop);
	}
	
	public void addEventListener(String type, Callable callable) {
		this.handlers.put(type, callable);
	}

	public Map<String, DynamicProperty> getPropertyMap() {
		// TODO make sure this is immutable.
		return properties;
	}

	public DynamicProperty getType() {
		return this.type;
	}

	public void addConstructor(ConstructorParams params) {
		this.constructor = params;
	}

	public ConstructorParams getConstructor() {
		return constructor;
	}
	
	public void setUpdater(Callable updater) {
		this.updater = updater;
	}
	
	public Callable getUpdater() {
		return updater;
	}
	
	public static class ConstructorParams {
		protected String[] provided;
		protected Callable initializer;
		public ConstructorParams(String[] provided, Callable initializer) {
			this.provided = provided;
			this.initializer = initializer;
		}
		public Callable getInitializer() {
			return initializer;
		}
		public String[] getProvided() {
			return provided;
		}
	}

	public static class PrototypeType extends DynamicTypeProperty {
		private Prototype type;
		public PrototypeType(Prototype type) {
			this.type = type;
		}
		public DynamicProperty cloneWith(Object object) {
			// FIXME validate that type is an ancestor of object
			if (object == null) {
				return this;
			} else {
				return new DynamicValueProperty(Prototype.class, object);
			}
		}

		public Class<?> getJavaType() {
			return Prototype.class;
		}
	}

	public Map<String, Callable> getHandlers() {
		return handlers;
	}


}
