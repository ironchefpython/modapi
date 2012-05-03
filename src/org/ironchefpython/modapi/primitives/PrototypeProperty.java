package org.ironchefpython.modapi.primitives;

import org.ironchefpython.modapi.AbstractDynamicProperty;
import org.ironchefpython.modapi.DynamicProperty;
import org.ironchefpython.modapi.Prototype;
import org.ironchefpython.modapi.error.PropertyError;
import org.mozilla.javascript.Callable;


public class PrototypeProperty implements DynamicProperty {
	public static DynamicProperty PROTOTYPE_TYPE = new AbstractDynamicProperty() {
		public DynamicProperty cloneWith(Object object) {
			return object == null ? this : new PrototypeProperty((Prototype)object);
		}


	};
	
	public static class PrototypeType extends AbstractDynamicProperty {
		private Prototype type;
		public PrototypeType(Prototype type) {
			this.type = type;
		}
		public DynamicProperty cloneWith(Object object) {
			// FIXME validate that type is an ancestor of object
			return object == null ? this : new PrototypeProperty((Prototype)object);
		}

	}
	
	
	
	private Prototype value;
	
	public PrototypeProperty(Prototype object) {
		this.value = object;
	}

	public DynamicProperty cloneWith(Object object) throws PropertyError {
		if (object != null) {
			throw PropertyError.IllegalAccessException(PrototypeProperty.class.getName());
		}
		return this;
	}

	public Object getValue() {
		return value;
	}


	

}
