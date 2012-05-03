package org.ironchefpython.modapi;

import org.ironchefpython.modapi.error.PropertyError;

public abstract class AbstractDynamicProperty implements DynamicProperty {
	public DynamicProperty cloneWith(Object object) throws PropertyError {
		if (object == null) {
			return this;
		}
		// FIXME (return a component or child component based on the
		// string passed)
		throw new NoSuchMethodError();
	}

	public Object getValue() {
		// FIXME produce a better result for a null value
		throw new NoSuchMethodError();
	}
}
