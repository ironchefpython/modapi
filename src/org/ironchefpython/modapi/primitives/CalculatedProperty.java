package org.ironchefpython.modapi.primitives;

import org.ironchefpython.modapi.AbstractDynamicProperty;
import org.ironchefpython.modapi.DynamicProperty;
import org.ironchefpython.modapi.error.PropertyError;
import org.mozilla.javascript.Callable;

public class CalculatedProperty implements DynamicProperty {

	public static class CalculatedType extends AbstractDynamicProperty {
		private DynamicProperty type;
		public CalculatedType(DynamicProperty type) {
			this.type = type;
		}
		public DynamicProperty cloneWith(Object object) {
			// FIXME validate that object is a callable
			// 
			return object == null ? this : new CalculatedProperty(type, (Callable)object);
		}

		public Class<?> getJavaType() {
			return type.getJavaType();
		}
		public Class<?> getFieldType() {
			return null;
		}

	}
	
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



	public Class<?> getJavaType() {
		return type.getJavaType();
	}

	public boolean isStatic() {
		return true;
	}

	public Class<?> getFieldType() {
		return Callable.class;
	}

}


