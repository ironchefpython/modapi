package org.ironchefpython.rhinodemo;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;

import org.ironchefpython.modapi.*;
import org.ironchefpython.modapi.error.GeneralModdingException;
import org.ironchefpython.modapi.error.InvalidEventRegistration;
import org.junit.Test;
import org.mockengine.Engine;
import org.mockengine.MockEngine;
import org.mockito.ArgumentCaptor;
import org.mozilla.javascript.Callable;

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
		
		for (Prototype p : mm.getPrototypes()) {
			Class<?> cls = ComponentFactory(cp, p);
			Constructor<?> c = cls.getConstructor(Number.class, Number.class, Number.class);
			Object o = c.newInstance(1,2,3);
			mm.callInitializer(o);
			System.out.println(mm.callMethod(o, "ratio"));
			
		}
		
//		mm.exec("game.addEventListener('test', function(evt) { console.log(evt.button) }, false)");

//		ArgumentCaptor<EventListener> argument = ArgumentCaptor.forClass(EventListener.class);
//		verify(game).addEventListener(anyString(), argument.capture(), eq(false));
//
//		MouseEvent uiEvent= mock(MouseEvent.class);
//		when(uiEvent.getType()).thenReturn("mouseevent");
//		when(uiEvent.getButton()).thenReturn((short) 1);
//		argument.getValue().handleEvent(uiEvent);
	}


	
	public static Class ComponentFactory(ClassPool classPool, Prototype p) throws Exception {
		CtClass comp = classPool.makeClass(p.getId()+"Component");
		comp.addConstructor(CtNewConstructor.defaultConstructor(comp));
		

		
		
		for (Map.Entry<String, DynamicProperty> e : p.getPropertyMap().entrySet()) {
			System.out.println(e.getValue().getClass());
			e.getValue().addToClass(e.getKey(), comp);
		}
		
		CtClass[] types = p.getConstructor().getParamClasses(); 
		String body = "{";
		String[] provided = p.getConstructor().getProvided();
		for (int i = 0; i < provided.length; i++) {
			body += "this." + provided[i] + "=$" + (i+1) + ";";
		}
		body += "}";
		comp.addConstructor(CtNewConstructor.make(types, new CtClass[0], body, comp));
		
		return comp.toClass();
		
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
