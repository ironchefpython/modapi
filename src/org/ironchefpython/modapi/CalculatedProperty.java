package org.ironchefpython.modapi;

import org.ironchefpython.modapi.error.PropertyError;
import org.mozilla.javascript.Callable;

public class CalculatedProperty implements DynamicProperty {
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

	public boolean isStatic() {
		return true;
	}

	public Class<?> getJavaType() {
		return type.getJavaType();
	}

	public Class<?> getFieldType() {
		return Callable.class;
	}

	
//	public static class CalculatedType implements DynamicProperty {
//	private DynamicProperty type;
//	public CalculatedType(DynamicProperty type) {
//		this.type = type;
//	}
//	public DynamicProperty cloneWith(Object object) {
//		// FIXME validate that object is a callable
//		// 
//		return object == null ? this : new CalculatedProperty(type, (Callable)object);
//	}
//
//	public Class<?> getJavaType() {
//		return type.getJavaType();
//	}
//	public Class<?> getFieldType() {
//		throw new NoSuchMethodError();
//	}
//
//	public Object getValue() {
//		throw new NoSuchMethodError();
//	}
//
//	public boolean isStatic() {
//		return false;
//	}
//
//}
	
}


