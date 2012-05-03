package org.ironchefpython.modapi;

import java.util.*;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

import org.ironchefpython.modapi.Prototype.ConstructorParams;
import org.ironchefpython.modapi.error.GeneralModdingException;
import org.ironchefpython.modapi.error.InvalidComponentRegistration;
import org.ironchefpython.modapi.primitives.PrototypeProperty;
import org.ironchefpython.modapi.primitives.CalculatedProperty.CallablePointer;
import org.mockengine.*;
import org.mozilla.javascript.Callable;
import org.mozilla.javascript.ScriptableObject;

public class Prototype implements EventTarget {


	private String id;
	private Map<String, DynamicProperty> properties;
	private DynamicProperty type;
	private Constructor constructor;

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
		type = new PrototypeProperty.PrototypeType(this);
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
//System.out.println(properties.keySet());
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
		this.constructor = new Constructor(params);
	}

	public Constructor getConstructor() {
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
	}

	public class Constructor extends ConstructorParams {
		public Constructor(ConstructorParams params) {
			super(params.provided, params.initializer);
		}

		public CtClass[] getParamClasses() throws NotFoundException {
			CtClass[] result = new CtClass[provided.length];
			for (int i = 0; i < provided.length; i++) {
				result[i] = ClassPool.getDefault().get(properties.get(provided[i]).getType().getCanonicalName());
			};
			return result;
		}

		public String[] getProvided() {
			return provided;
		}
	}
}
