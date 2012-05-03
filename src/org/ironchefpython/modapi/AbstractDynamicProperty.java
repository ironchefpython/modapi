package org.ironchefpython.modapi;

import java.lang.reflect.InvocationTargetException;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.Modifier;
import javassist.NotFoundException;

import org.ironchefpython.modapi.error.PropertyError;
import org.ironchefpython.modapi.primitives.NumberProperty;

public abstract class AbstractDynamicProperty implements DynamicProperty {
//	public DynamicProperty cloneWith(Object object) throws PropertyError {
//		if (object == null) {
//			return this;
//		}
//		// FIXME (return a component or child component based on the
//		// string passed)
//		throw new NoSuchMethodError();
//	}
//
	public boolean isStatic() {
		return false;
	}
	
	public Object getValue() {
		// FIXME produce a better result for a null value
		throw new NoSuchMethodError();
	}
	
	protected static DynamicProperty makeType(final Class<?> type, final Class<? extends DynamicProperty> propertyClass) {
		return new AbstractDynamicProperty() {
			public DynamicProperty cloneWith(Object object) {
				try {
					return (object == null ? this : propertyClass.getConstructor(type).newInstance(object));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}

			public Class<?> getJavaType() {
				return type;
			}

			public Class<?> getFieldType() {
				return type;
			}


		};
	}
}
