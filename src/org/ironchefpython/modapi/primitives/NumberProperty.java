package org.ironchefpython.modapi.primitives;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.Modifier;
import javassist.NotFoundException;


import org.ironchefpython.modapi.AbstractDynamicProperty;
import org.ironchefpython.modapi.DynamicProperty;

import org.ironchefpython.modapi.error.PropertyError;

public class NumberProperty extends AbstractDynamicProperty {
	public static final Class JAVA_CLASS = Number.class;
	public static DynamicProperty NUMBER_TYPE = new AbstractDynamicProperty() {
		public DynamicProperty cloneWith(Object object) {
			return object == null ? this : new NumberProperty(Float.valueOf(object.toString()));
		}

		public Object addToClass(String key, CtClass comp) throws CannotCompileException, NotFoundException {
			CtField f = new CtField(ClassPool.getDefault().get(JAVA_CLASS.getCanonicalName()), key, comp);
			f.setModifiers(Modifier.PUBLIC);
			comp.addField(f);
			return null;
		}

		public Class<?> getType() {
			return JAVA_CLASS;
		}


	};
	
	private float value;
	
	public NumberProperty(float f) {
		this.value = f;
	}

	public DynamicProperty cloneWith(Object object) throws PropertyError {
		if (object != null) {
			throw PropertyError.IllegalAccessException(BooleanProperty.class.getName());
		}
		return this;
	}

	public Object getValue() {
		return value;
	}

	public Object addToClass(String key, CtClass comp)
			throws CannotCompileException, NotFoundException {
		CtField f = new CtField(ClassPool.getDefault().get(JAVA_CLASS.getCanonicalName()), key, comp);
		f.setModifiers(Modifier.STATIC + Modifier.PUBLIC);
		comp.addField(f, CtField.Initializer.constant(value));
		return null;
	}

	public Class<?> getType() {
		return JAVA_CLASS;
	}

	

}
