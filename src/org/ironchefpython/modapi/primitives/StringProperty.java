package org.ironchefpython.modapi.primitives;

import org.ironchefpython.modapi.AbstractDynamicProperty;
import org.ironchefpython.modapi.DynamicProperty;
import org.ironchefpython.modapi.error.PropertyError;

public class StringProperty implements DynamicProperty {
	public static DynamicProperty STRING_TYPE = new AbstractDynamicProperty() {
		public DynamicProperty cloneWith(Object object) {
			return object == null ? this : new StringProperty(object.toString());
		}

	};
	
	private String value;
	
	public StringProperty(String string) {
		this.value = string;
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
