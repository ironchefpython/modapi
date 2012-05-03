package org.ironchefpython.modapi.primitives;

import org.ironchefpython.modapi.AbstractDynamicProperty;
import org.ironchefpython.modapi.DynamicProperty;
import org.ironchefpython.modapi.error.PropertyError;

public class ColorProperty implements DynamicProperty {
	public static DynamicProperty COLOR_TYPE = new AbstractDynamicProperty() {
		public DynamicProperty cloneWith(Object object) {
			return object == null ? this : new ColorProperty(object.toString());
		}


	};
	
	private String value;
	
	public ColorProperty(String color) {
		this.value = color;
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

	

}
