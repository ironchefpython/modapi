package org.ironchefpython.modapi;

import java.util.*;

import org.ironchefpython.modapi.error.InvalidComponentRegistration;
import org.mockengine.*;
import org.mozilla.javascript.Callable;

public class Prototype implements EventTarget {


	private String id;
	private Map<String, DynamicProperty> properties;
	private DynamicProperty type;
	private ConstructorParams constructor;

	public Prototype(String id, Map<String, DynamicProperty> properties,
			Map<String, Handler> listeners, Collection<String> includes)
			throws InvalidComponentRegistration {
		this(id);
		if (properties != null) {
			this.properties.putAll(properties);
		}
	}

	public Prototype(String id) throws InvalidComponentRegistration {
		if (id == null) {
			throw InvalidComponentRegistration.missingId();
		}
		this.id = id;
		this.properties = new HashMap<String, DynamicProperty>();
		type = new DynamicValueProperty(Prototype.class, this);
	}

	public String getId() {
		return id;
	}

	public void addProperty(String propName, DynamicProperty prop) {
		properties.put(propName, prop);
	}
	
	public void addEventListener(String type, Handler handler) {
		// TODO Auto-generated method stub
	}

	public Collection<DynamicProperty> getProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getPropertyValue(String propertyName) {
		return properties.get(propertyName).getValue();
	}

	public Map<String, DynamicProperty> getPropertyMap() {
		// TODO make sure this is immutable.
		return properties;
	}

	public DynamicProperty getType() {

		return this.type;
	}
	
    public interface EventReceiver<T extends Event> {
        public void onEvent(T event, Entity entity);
    }


	public void addConstructor(ConstructorParams params) {
		this.constructor = params;
	}

	public ConstructorParams getConstructor() {
		return constructor;
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

}
