package org.ironchefpython.modapi.primitives;

import org.ironchefpython.modapi.AbstractDynamicProperty;
import org.ironchefpython.modapi.DynamicProperty;
import org.ironchefpython.modapi.error.PropertyError;
import org.mozilla.javascript.Callable;


public class FunctionProperty implements DynamicProperty {
	public static DynamicProperty FUNCTION_TYPE = new AbstractDynamicProperty() {
		public DynamicProperty cloneWith(Object object) {
			return object == null ? this : new FunctionProperty((Callable)object);
		}


	};
	
	private Callable value;
	
	public FunctionProperty(Callable object) {
		this.value = object;
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
