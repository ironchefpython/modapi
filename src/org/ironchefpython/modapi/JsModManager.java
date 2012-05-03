package org.ironchefpython.modapi;


import java.io.*;
import java.util.*;

import org.mozilla.javascript.*;
import org.ironchefpython.modapi.error.*;
import org.ironchefpython.modapi.primitives.*;

import org.mockengine.Engine;
import org.mockengine.Entity;
import org.mockengine.Event;
import org.mockengine.Handler;

public class JsModManager extends ModManager {
	private static final String INIT_SUFFIX = "!!INIT";
	private Context cx;
	private Scriptable scope;
	private Map<String, Callable> methods; 

	
	public JsModManager(Engine game, String modName) {
		super(game, modName);
        cx = Context.enter();
        scope = cx.initStandardObjects();

        ScriptableObject.putProperty(scope, "console", Context.javaToJS(new Console(), scope));
        ScriptableObject.putProperty(scope, "manager", Context.javaToJS(new Facade(), scope));
        
        methods = new HashMap<String, Callable>();
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

	public class Console {
		public void log(String s) {
			System.err.println("js> " + s);
		}
	}
	
	public Object callMethod(Object obj, String name) {
		return methods.get(obj.getClass().getName()+'!'+name).call(cx, scope, (Scriptable) Context.javaToJS(obj, scope), null);
	}
	
	public void callInitializer(Object obj) {
System.out.println(obj.getClass().getName()+INIT_SUFFIX);		
System.out.println(methods.keySet());		
		methods.get(obj.getClass().getName()+INIT_SUFFIX).call(cx, scope, (Scriptable) Context.javaToJS(obj, scope), null);
	}

	public class Facade {
		@SuppressWarnings("unchecked")
		public EventFactory registerEvent(ScriptableObject jsObject) throws InvalidEventRegistration {
//System.out.println(JSON.stringify(cx, scope, jsObject, null, null));
			
			String type = (String) jsObject.get("type");
			Map<String, DynamicProperty> properties = parseProperties((Map<String, Object>) jsObject.get("properties"));
			EventFactory result = new CustomEventFactory(type, properties);
			return JsModManager.this.registerEvent(result);
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

			Prototype result = new Prototype(id);
			for (Map.Entry<String, DynamicProperty> e : properties.entrySet()) {
				
				DynamicProperty p = e.getValue();
				String name = e.getKey();
				result.addProperty(name, p);
				if (p instanceof CalculatedProperty) {
					methods.put(id+"Component!"+name, (Callable) p.getValue());
				}
			}

			
			Map<String, Callable> listeners = (Map<String, Callable>) jsObject.get("listeners");
			

			
			if (listeners != null) {
				for (Map.Entry<String, Callable> e : listeners.entrySet()) {
					Handler handler = new JsHandler(e.getValue());
					result.addEventListener(e.getKey(), handler);
				}
			}

			Prototype.ConstructorParams constructor = (Prototype.ConstructorParams) jsObject.get("constructor");
//			getConstructorSig(constructor, properties);
			result.addConstructor(constructor);
			methods.put(id+"Component"+INIT_SUFFIX, constructor.getInitializer());
			return JsModManager.this.registerPrototype(result);
		}
		
		public Prototype.ConstructorParams makeConstructor(String[] provided, Callable initializer) {
			return new Prototype.ConstructorParams(provided, initializer); 
		}
		
		public DynamicProperty getStringType() {
			return StringProperty.STRING_TYPE;
		}

		public DynamicProperty getNumberType() {
			return NumberProperty.NUMBER_TYPE;
		}

		public DynamicProperty getBooleanType() {
			return BooleanProperty.BOOLEAN_TYPE;
		}

		public DynamicProperty getColorType() {
			return ColorProperty.COLOR_TYPE;
		}

		public DynamicProperty getFunctionType() {
			return FunctionProperty.FUNCTION_TYPE;
		}

		
		public DynamicProperty getTextureType() {
			return TextureProperty.TEXTURE_TYPE;
		}

		public DynamicProperty calculatedType(DynamicProperty type, Callable callable) {
			return new CalculatedProperty(type, callable);
		}



		public EventFactory getEvent(String name) throws UnregisteredEventException {
			return JsModManager.this.getEvent(name);
		}

		public Prototype getPrototype(String name)
				throws InvalidComponentRegistration {
			return JsModManager.this.getPrototype(name);
		}

		public Prototype[] getPrototypes() {
			return JsModManager.this.getPrototypes();
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
			return new FunctionProperty((Callable) def);
		}
		throw new NoSuchMethodError();
	}
	


	public class JsHandler implements Handler {
		private Callable function;
		
		public JsHandler(Callable f) {
			this.function = f;
		}
		
		public void handleEvent(Event event, Entity currentTarget) {
			Scriptable thisObj = null;  // get this from currentTarget 
			function.call(cx, scope, thisObj, new Object[]{event});
			
		}
		

	}
}
