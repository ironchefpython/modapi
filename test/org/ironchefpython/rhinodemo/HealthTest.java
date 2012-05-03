package org.ironchefpython.rhinodemo;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.Modifier;

import org.ironchefpython.modapi.*;
import org.ironchefpython.modapi.error.GeneralModdingException;
import org.ironchefpython.modapi.error.InvalidEventRegistration;

import org.junit.Test;
import org.mockengine.DynamicComponent;
import org.mockengine.DynamicComponent.Getter;
import org.mockengine.DynamicComponent.Constructor;
import org.mockengine.Engine;
import org.mockengine.MockEngine;
import org.mockito.ArgumentCaptor;
import org.mozilla.javascript.Callable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ast.Scope;

import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.MouseEvent;


import static org.mockito.Mockito.*;

public class HealthTest {

	
	public static void main(String[] args) throws Exception {
		new HealthTest().mockTest();
	}
	
	@Test
	public void mockTest() throws Exception {
		
		Engine game = new MockEngine();
		JsModManager mm = new JsModManager(game, "health");
		mm.registerEvent(new CustomEventFactory("full_health", null));
		mm.registerEvent(new CustomEventFactory("no_health", null));
		mm.runScript(HealthTest.class.getResourceAsStream("health.js"), "health.js");
		
		ClassPool cp = new ClassPool();
		cp.appendSystemPath();

		Prototype p = mm.getPrototype("Health");
		Class<? extends DynamicComponent> HealthComponent = ComponentFactory(cp, p, mm);
		Constructor ctr = new Constructor(HealthComponent, Number.class, Number.class, Number.class);
		DynamicComponent o = ctr.newInstance(1,2,3);
		Getter getRatio = new Getter(HealthComponent, "ratio");
		System.out.println( getRatio.get(o) );

//		for (Prototype p : mm.getPrototypes()) {
//			
//		}
		
//		mm.exec("game.addEventListener('test', function(evt) { console.log(evt.button) }, false)");

//		ArgumentCaptor<EventListener> argument = ArgumentCaptor.forClass(EventListener.class);
//		verify(game).addEventListener(anyString(), argument.capture(), eq(false));
//
//		MouseEvent uiEvent= mock(MouseEvent.class);
//		when(uiEvent.getType()).thenReturn("mouseevent");
//		when(uiEvent.getButton()).thenReturn((short) 1);
//		argument.getValue().handleEvent(uiEvent);
	}


	
	public static Class<? extends DynamicComponent> ComponentFactory(ClassPool classPool, Prototype p, JsModManager mm) throws Exception {
		
		CtClass comp = classPool.makeClass(p.getId()+"Component", ClassPool.getDefault().get(DynamicComponent.class.getName()));
		comp.addConstructor(CtNewConstructor.defaultConstructor(comp));
		
		
		Map<String, Object> callables = new HashMap<String, Object>();
		for (Map.Entry<String, DynamicProperty> e : p.getPropertyMap().entrySet()) {
			String name = e.getKey();
			Object initializer = e.getValue().addToClass(name, comp);
			if (initializer != null) {
				callables.put(name, initializer);
			}
		}
		
		CtField scope = new CtField(ClassPool.getDefault().get(Scriptable.class.getName()), "__SCOPE__", comp);
		scope.setModifiers(Modifier.STATIC + Modifier.PUBLIC);
		comp.addField(scope);

		CtField cxField = new CtField(ClassPool.getDefault().get(Context.class.getName()), "__CX__", comp);
		cxField.setModifiers(Modifier.STATIC + Modifier.PUBLIC);
		comp.addField(cxField);

		
		CtClass[] types = p.getConstructor().getParamClasses(); 
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
		
		for (Field f : result.getFields()) {
			if (callables.containsKey(f.getName())) {
				f.set(null, callables.get(f.getName()));
			}
		}
		
		result.getField("__SCOPE__").set(null, mm.getScope());
		result.getField("__CX__").set(null, mm.getContext());
		result.getField("__INIT__").set(null, p.getConstructor().getInitializer());
	
		return result;
		
//		CtField f = new CtField()
//		CtNewMethod.make()

		//		
//		cc.addConstructor(CtNewConstructor.defaultConstructor(cc));
//		cc.addConstructor(CtNewConstructor.make("public Test(String name) {System.out.println(\"making \" + name); }", cc));
//		CtMethod m = CtNewMethod.make("public void test1() {System.out.println(\"test\"); }", cc); 
//		cc.addMethod(m);
//		Class<?> cls = cc.toClass();
//		Constructor<?> c = cls.getConstructor(String.class);
//		Object object = c.newInstance("test2");
//		Method method = cls.getMethod("test1");
//		method.invoke(object);
	}
}
