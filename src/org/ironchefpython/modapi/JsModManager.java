package org.ironchefpython.modapi;

import java.io.*;
import java.util.*;

import org.mozilla.javascript.*;
import org.ironchefpython.modapi.error.*;

public class JsModManager {
	private Context cx;
	private Scriptable scope;
	private ModRegistry registry;
	
	public enum Primitive implements DynamicProperty {
		BOOLEAN(Boolean.TYPE), 
		STRING(String.class), 
		NUMBER(Number.class),
		COLOR(String.class), 
		FUNCTION(Callable.class), 
		TEXTURE(String.class),
		DOUBLE(Double.TYPE),
		;
		
		private final Class<?> type;
		Primitive(Class<?> type) {
			this.type = type;
		}
		
		public DynamicProperty cloneWith(Object object) {
			try {
				return (object == null ? this : new DynamicValueProperty(type, object));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		public Class<?> getJavaType() {
			return type;
		}
		
		public boolean isStatic() {
			return false;
		}
		
		public Object getValue() {
			// FIXME produce a better result for a null value
			throw new NoSuchMethodError();
		}
		
		public Class<?> getFieldType() {
			return getJavaType();
		}
		
	}
	
	public JsModManager(ModRegistry registry, String modName) {
		this.registry = registry;
        cx = Context.enter();
        scope = cx.initStandardObjects();

        ScriptableObject.putProperty(scope, "console", Context.javaToJS(new Console(), scope));
        ScriptableObject.putProperty(scope, "manager", Context.javaToJS(new Facade(), scope));
	}

	public void runScript(InputStream is, String name) throws IOException {
		cx.compileReader(new InputStreamReader(is), name, 0, null).exec(cx, scope);
	}
	
	public void runScript(File f) throws IOException {
		cx.compileReader(new FileReader(f), f.getName(), 0, null).exec(cx, scope);
	}

	public void exec(String s) {
		cx.evaluateString(scope, s, "<cmd>", 0, null);
	}

	public static class Console {
		public void log(String s) {
			System.err.println("js> " + s);
		}
	}

	public class Facade {
		@SuppressWarnings("unchecked")
		public EventDefinition registerEvent(ScriptableObject jsObject) throws InvalidEventRegistration {
//System.out.println(JSON.stringify(cx, scope, jsObject, null, null));
			
			String type = (String) jsObject.get("type");
			Map<String, DynamicProperty> properties = parseProperties((Map<String, Object>) jsObject.get("properties"));
			EventDefinition result = new EventDefinition(type);
			for (Map.Entry<String, DynamicProperty> e : properties.entrySet()) {
				result.addProperty(e.getKey(), e.getValue());
			}
			
			return registry.registerEvent(result);
		}

		public Prototype registerPrototype(ScriptableObject jsObject) throws GeneralModdingException {
			return registerPrototype(null, jsObject);
		}

		@SuppressWarnings("unchecked")
		public Prototype registerPrototype(Prototype prototype, ScriptableObject jsObject) throws GeneralModdingException {
			String id = (String) jsObject.get("id");
			Map<String, DynamicProperty> properties = new HashMap<String, DynamicProperty>();
			
			// If we have an explicit parent, the top-level jsObject is used to clone the properties.
			if (prototype != null) {
				properties.putAll(cloneProperties(prototype.getPropertyMap(), (Map<String, Object>) jsObject));
			}
			
			// For every parent component included in the component definition,
			// clone the properties into a collection and modify this collection
			// based on the jsobject provided for each collection
			Map<String, ScriptableObject> includes = (Map<String, ScriptableObject>) jsObject.get("includes");
			if (includes != null) {
				for (Map.Entry<String, ScriptableObject> e1 : includes.entrySet()) {
					properties.putAll(cloneProperties(getPrototype(e1.getKey()).getPropertyMap(), (Map<String, Object>) e1.getValue()));
				}
			}
			
			// At this point properties will have cloned version of the included
			// properties, now we layer the defined properties on top of it.
			properties.putAll(parseProperties((Map<String, Object>) jsObject.get("properties")));

			Prototype result = new JsPrototype(id);
			for (Map.Entry<String, DynamicProperty> e : properties.entrySet()) {
				result.addProperty(e.getKey(), e.getValue());
			}

			Map<String, Callable> listeners = (Map<String, Callable>) jsObject.get("handlers");
			if (listeners != null) {
				for (Map.Entry<String, Callable> e : listeners.entrySet()) {
					result.addEventListener(e.getKey(), e.getValue());
				}
			}

			Prototype.ConstructorParams constructor = (Prototype.ConstructorParams) jsObject.get("constructor");
			result.addConstructor(constructor);

			return registry.registerPrototype(result);
		}
		
		public Prototype.ConstructorParams makeConstructor(String[] provided, Callable initializer) {
			return new Prototype.ConstructorParams(provided, initializer); 
		}
		
		public DynamicProperty getStringType() {
			return Primitive.STRING;
		}

		public DynamicProperty getNumberType() {
			return Primitive.NUMBER;
		}

		public DynamicProperty getBooleanType() {
			return Primitive.BOOLEAN;
		}

		public DynamicProperty getColorType() {
			return Primitive.COLOR;
		}

		public DynamicProperty getFunctionType() {
			return Primitive.FUNCTION;
		}

		
		public DynamicProperty getTextureType() {
			return Primitive.TEXTURE;
		}

		public DynamicProperty getDoubleType() {
			return Primitive.DOUBLE;
		}

		public DynamicProperty calculatedType(DynamicProperty type, Callable callable) {
			return new CalculatedProperty(type, callable);
		}


		public EventDefinition getEvent(String name) throws UnregisteredEventException {
			return registry.getEvent(name);
		}

		public Prototype getPrototype(String name)
				throws InvalidComponentRegistration {
			return registry.getPrototype(name);
		}

		public Prototype[] getPrototypes() {
			return registry.getPrototypes();
		}
	}
	

//	private Class<?>[] getConstructorSig(NativeFunction obj,
//			Map<String, DynamicProperty> properties) {
//
//		try {
//			Method getParamCount = obj.getClass().getDeclaredMethod(
//					"getParamCount");
//			getParamCount.setAccessible(true);
//			Method getParamOrVarName = obj.getClass().getDeclaredMethod(
//					"getParamOrVarName", int.class);
//			getParamOrVarName.setAccessible(true);
//			int count = (Integer) getParamCount.invoke(obj);
//			Class<?>[] result = new Class[count];
//			for (int i = 0; i < count; i++) {
//				String name = (String) getParamOrVarName.invoke(obj, i);
//				result[i] = properties.get(name).getType();
//			}
//			return result;
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//	}
	
	private Map<String, DynamicProperty> parseProperties(Map<String, Object> source) {
		Map<String, DynamicProperty> result = new HashMap<String, DynamicProperty>();
		if (source != null) {
			for (Map.Entry<String, Object> e: source.entrySet()) {
				result.put(e.getKey(), makeProperty(e.getValue()));
			}
		}
		return result;
	}

	
	private Map<String, DynamicProperty> cloneProperties(
			Map<String, DynamicProperty> source, Map<String, Object> jsObject)
			throws PropertyError {
		Map<String, DynamicProperty> result = new HashMap<String, DynamicProperty>();
		for (Map.Entry<String, DynamicProperty> e : source.entrySet()) {
			String propName = e.getKey();
			result.put(propName, e.getValue().cloneWith(jsObject.get(propName)));
		}
		return result;
	}
	
	private DynamicProperty makeProperty(Object def) {
		if (def instanceof DynamicProperty) {
			return (DynamicProperty) def;
		} else if (def instanceof Callable) {
			return new DynamicValueProperty(Callable.class, def);
		}
		throw new NoSuchMethodError();
	}
	
	public Scriptable getScope() {
		return scope;
	}

	public Context getContext() {
		return cx;
	}

	public void putInScope(String name, Class<?> cls) {
		ScriptableObject.putProperty(scope, name, new NativeJavaClass(scope, cls));
	}
	
	public class JsPrototype extends Prototype {

		public JsPrototype(String id) throws InvalidComponentRegistration {
			super(id);
		}
		
		public Scriptable getScope() {
			return scope;
		}

		public Context getContext() {
			return cx;
		}
}
	
}
