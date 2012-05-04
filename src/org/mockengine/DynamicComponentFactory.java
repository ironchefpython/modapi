package org.mockengine;

import java.util.Map;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;

import org.ironchefpython.modapi.DynamicProperty;
import org.ironchefpython.modapi.JsModManager;
import org.ironchefpython.modapi.Prototype;
import org.mozilla.javascript.Callable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class DynamicComponentFactory {
	private static final String COMPONENT_NAME_TEMPLATE = "%sComponent";
	private static final String SYSTEM_NAME_TEMPLATE = "%sComponentSystem";
	private final ClassPool cp;
	private final JsModManager manager;
	private final Context cx;
	private final Scriptable scope;
	
	public DynamicComponentFactory(JsModManager mm) {
		cp = new ClassPool();
		cp.appendSystemPath();
		
		this.manager = mm;
		this.cx = this.manager.getContext();
		this.scope = this.manager.getScope();
	}
	
	public Class<? extends DynamicComponent> makeComponent(String name) throws Exception {
		return makeComponent(manager.getPrototype(name));
	}
	
	public Class<? extends DynamicSystem> makeSystem(String name) throws Exception {
		return makeSystem(manager.getPrototype(name));
	}
	
	public Class<? extends DynamicComponent> makeComponent(Prototype p) throws Exception {
		String className = makeComponentClassName(p.getId());
		CtClass comp = cp.makeClass(className, ClassPool.getDefault().get(DynamicComponent.class.getName()));

		for (Map.Entry<String, DynamicProperty> e : p.getPropertyMap().entrySet()) {
			DynamicProperty prop = e.getValue();
			addToClass(e.getKey(), comp, prop.getFieldType(), prop.isStatic());
		}
		
		CtField scopeField = new CtField(ClassPool.getDefault().get(Scriptable.class.getName()), "__SCOPE__", comp);
		scopeField.setModifiers(Modifier.STATIC + Modifier.PUBLIC);
		comp.addField(scopeField);

		CtField cxField = new CtField(ClassPool.getDefault().get(Context.class.getName()), "__CX__", comp);
		cxField.setModifiers(Modifier.STATIC + Modifier.PUBLIC);
		comp.addField(cxField);

		
		comp.addConstructor(CtNewConstructor.defaultConstructor(comp));

		CtClass[] types = getParamClasses(p.getConstructor().getProvided(), p.getPropertyMap()); 
		String body = "{";
		String[] provided = p.getConstructor().getProvided();
		for (int i = 0; i < provided.length; i++) {
			body += "this." + provided[i] + "=$" + (i+1) + ";";
		}
		body += "}";
		comp.addConstructor(CtNewConstructor.make(types, new CtClass[0], body, comp));
		
		CtField initField = new CtField(ClassPool.getDefault().get(Callable.class.getName()), "__INIT__", comp);
		initField.setModifiers(Modifier.STATIC + Modifier.PUBLIC);
		comp.addField(initField);
		
		@SuppressWarnings("unchecked")
		Class<? extends DynamicComponent> result = comp.toClass();
		
		for (Map.Entry<String, DynamicProperty> e : p.getPropertyMap().entrySet()) {
			DynamicProperty prop = e.getValue();
			if (prop.isStatic()) {				
				result.getField(e.getKey()).set(null, prop.getValue());
			}
		}
		
		result.getField("__SCOPE__").set(null, scope);
		result.getField("__CX__").set(null, cx);
		result.getField("__INIT__").set(null, p.getConstructor().getInitializer());
	
		return result;

	}

	public Class<? extends DynamicSystem> makeSystem(Prototype p) throws Exception {
		String className = makeSystemClassName(p.getId());
		CtClass comp = cp.makeClass(className, cp.get(DynamicSystem.class.getName()));
		comp.addConstructor(CtNewConstructor.defaultConstructor(comp));

		CtField scopeField = new CtField(cp.get(Scriptable.class.getName()), "__SCOPE__", comp);
		scopeField.setModifiers(Modifier.STATIC + Modifier.PUBLIC);
		comp.addField(scopeField);

		CtField cxField = new CtField(cp.get(Context.class.getName()), "__CX__", comp);
		cxField.setModifiers(Modifier.STATIC + Modifier.PUBLIC);
		comp.addField(cxField);
		
		CtField updateCallable = new CtField(cp.get(Callable.class.getName()), "__UPDATER__", comp);
		updateCallable.setModifiers(Modifier.STATIC + Modifier.PUBLIC);
		comp.addField(updateCallable);

		CtMethod updater = CtNewMethod.make(
				Modifier.PROTECTED,
				CtClass.voidType,
				"_update",
				new CtClass[] { cp.get(Float.class.getName()),
						cp.get(DynamicComponent.class.getName()) },
				new CtClass[0],
				"{__UPDATER__.call(__CX__, __SCOPE__, ("
						+ Scriptable.class.getName() + ") "
						+ Context.class.getName()
						+ ".javaToJS($2, __SCOPE__), new Object[] {$1} );}", 
				comp);
		comp.addMethod(updater);
    	

//		for (Map.Entry<String, DynamicProperty> e : p.getPropertyMap().entrySet()) {
//			DynamicProperty prop = e.getValue();
//			addToClass(e.getKey(), comp, prop.getFieldType(), prop.isStatic());
//		}
//		

//
//		
//		CtClass[] types = getParamClasses(p.getConstructor().getProvided(), p.getPropertyMap()); 
//		String body = "{";
//		String[] provided = p.getConstructor().getProvided();
//		for (int i = 0; i < provided.length; i++) {
//			body += "this." + provided[i] + "=$" + (i+1) + ";";
//		}
//		body += "}";
//		comp.addConstructor(CtNewConstructor.make(types, new CtClass[0], body, comp));
//		
//		CtField initField = new CtField(ClassPool.getDefault().get(Callable.class.getName()), "__INIT__", comp);
//		initField.setModifiers(Modifier.STATIC + Modifier.PUBLIC);
//		comp.addField(initField);
//		
//		@SuppressWarnings("unchecked")
		Class<? extends DynamicSystem> result = comp.toClass();
//		
//		for (Map.Entry<String, DynamicProperty> e : p.getPropertyMap().entrySet()) {
//			DynamicProperty prop = e.getValue();
//			if (prop.isStatic()) {				
//				result.getField(e.getKey()).set(null, prop.getValue());
//			}
//		}
//		
		result.getField("__SCOPE__").set(null, scope);
		result.getField("__CX__").set(null, cx);
		result.getField("__UPDATER__").set(null, p.getUpdater());
//	
		return result;

	}

	
	private void addToClass(String key, CtClass cls, Class<?> type,
			boolean isStatic) throws CannotCompileException, NotFoundException {
		CtField f = new CtField(cp.get(type.getCanonicalName()), key, cls);
		if (isStatic) {
			f.setModifiers(Modifier.STATIC + Modifier.PUBLIC);
		} else {
			f.setModifiers(Modifier.PUBLIC);
		}
		cls.addField(f);
	}
	
	private String makeComponentClassName(String id) {
		return String.format(COMPONENT_NAME_TEMPLATE, id);
	}

	private String makeSystemClassName(String id) {
		return String.format(SYSTEM_NAME_TEMPLATE, id);
	}

	
	public CtClass[] getParamClasses(String[] provided, Map<String, DynamicProperty> properties) throws NotFoundException {
		CtClass[] result = new CtClass[provided.length];
		for (int i = 0; i < provided.length; i++) {
			result[i] = ClassPool.getDefault().get(properties.get(provided[i]).getJavaType().getCanonicalName());
		};
		return result;
	}


}
