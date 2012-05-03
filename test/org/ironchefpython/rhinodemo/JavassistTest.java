package org.ironchefpython.rhinodemo;


import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;

public class JavassistTest {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		ClassPool cp = new ClassPool();
		cp.appendSystemPath();

		CtClass cc = cp.makeClass("Test");
		cc.addConstructor(CtNewConstructor.defaultConstructor(cc));
		cc.addConstructor(CtNewConstructor.make("public Test(String name) {System.out.println(\"making \" + name); }", cc));
		CtMethod m = CtNewMethod.make("public void test1() {System.out.println(\"test\"); }", cc); 
		cc.addMethod(m);
		Class<?> cls = cc.toClass();
		Constructor<?> c = cls.getConstructor(String.class);
		Object object = c.newInstance("test2");
		Method method = cls.getMethod("test1");
		method.invoke(object);
		
	}

}
