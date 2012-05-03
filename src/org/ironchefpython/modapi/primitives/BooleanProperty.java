package org.ironchefpython.modapi.primitives;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import javassist.Modifier;

import org.ironchefpython.modapi.AbstractDynamicProperty;
import org.ironchefpython.modapi.DynamicProperty;
import org.ironchefpython.modapi.error.PropertyError;

public class BooleanProperty implements DynamicProperty {
	public static DynamicProperty BOOLEAN_TYPE = new AbstractDynamicProperty() {
		public DynamicProperty cloneWith(Object object) {
			return (object == null ? this : new BooleanProperty(Boolean.valueOf(object.toString())));
		}

		public void addToClass(String key, CtClass comp) throws CannotCompileException {
			CtField f = new CtField(CtClass.booleanType, key, comp);
			comp.addField(f);
		}

		public Class<?> getType() {
			return boolean.class;
		}



	};
	
	private boolean value;
	
	public BooleanProperty(boolean bool) {
		this.value = bool;
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

	public void addToClass(String key, CtClass comp)
			throws CannotCompileException {
		CtField f = new CtField(CtClass.booleanType, key, comp);
		f.setModifiers(Modifier.STATIC);
		comp.addField(f, CtField.Initializer.constant(value));
	}

	public Class<?> getType() {
		return boolean.class;
	}



	

}
