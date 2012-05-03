package org.ironchefpython.modapi.primitives;

import org.ironchefpython.modapi.AbstractDynamicProperty;
import org.ironchefpython.modapi.DynamicProperty;

import org.ironchefpython.modapi.error.PropertyError;

public class NumberProperty extends AbstractDynamicProperty {
	public static final Class<?> JAVA_CLASS = Number.class;
	public static DynamicProperty NUMBER_TYPE = AbstractDynamicProperty.makeType(JAVA_CLASS, NumberProperty.class);
	
	private Number value;
	
	public NumberProperty(Number value) {
		this.value = value;
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

	public Class<?> getJavaType() {
		return JAVA_CLASS;
	}

	public Class<?> getFieldType() {
		return JAVA_CLASS;
	}

	

}
