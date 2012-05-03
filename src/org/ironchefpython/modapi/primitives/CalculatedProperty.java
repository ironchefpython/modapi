package org.ironchefpython.modapi.primitives;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.Modifier;
import javassist.NotFoundException;

import org.ironchefpython.modapi.AbstractDynamicProperty;
import org.ironchefpython.modapi.DynamicProperty;
import org.ironchefpython.modapi.Prototype;
import org.ironchefpython.modapi.error.PropertyError;
import org.mozilla.javascript.Callable;

public class CalculatedProperty implements DynamicProperty {

	public static class CalculatedType extends AbstractDynamicProperty {
		private DynamicProperty type;
		public CalculatedType(DynamicProperty type) {
			this.type = type;
		}
		public DynamicProperty cloneWith(Object object) {
			// FIXME validate that object is a callable
			// 
			return object == null ? this : new CalculatedProperty(type, (Callable)object);
		}
		public Object addToClass(String key, CtClass comp) throws CannotCompileException, NotFoundException {
			throw new NoSuchMethodError();
		}
		public Class<?> getType() {
			return type.getType();
		}

	}
	
	private DynamicProperty type;
	private Callable callable;
	
	public CalculatedProperty(DynamicProperty type, Callable callable) {
		this.callable = callable;
	}

	public DynamicProperty cloneWith(Object object) throws PropertyError {
		if (object != null) {
			throw PropertyError.IllegalAccessException(CalculatedProperty.class.getName());
		}
		return this;
	}

	public Object getValue() {
		return callable;
	}

	public Object addToClass(String key, CtClass comp)
			throws CannotCompileException, NotFoundException {
		CtField f = new CtField(ClassPool.getDefault().get(Callable.class.getName()), key, comp);
		f.setModifiers(Modifier.STATIC + Modifier.PUBLIC);
		comp.addField(f);
		return callable;
	}

	public Class<?> getType() {
		return type.getType();
	}

}


