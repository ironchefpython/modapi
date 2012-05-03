package org.ironchefpython.modapi;

import org.ironchefpython.modapi.error.PropertyError;

public class DynamicValueProperty implements DynamicProperty {
	private Class<?> type;
	private Object value;

	public DynamicValueProperty(Class<?> type, Object value) {
		this.type = type;
		this.value = value;
	}
	
	public boolean isStatic() {
		return false;
	}
	
	public Object getValue() {
		return value;
	}
	
	public DynamicProperty cloneWith(Object object) throws PropertyError {
		if (object == null) {
			return this;
		}
		throw PropertyError.IllegalAccessException(this.getClass().getName());
	}

	public Class<?> getJavaType() {
		return type;
	}

	public Class<?> getFieldType() {
		return type;
	}
	
	protected static DynamicProperty makeType(final Class<?> type) {
		return new DynamicTypeProperty() {
			public DynamicProperty cloneWith(Object object) {
				try {
					return (object == null ? this : new DynamicValueProperty(type, object));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}

			public Class<?> getJavaType() {
				return type;
			}
		};
	}
	
	abstract static class DynamicTypeProperty implements DynamicProperty {
		public boolean isStatic() {
			return false;
		}
		
		public Object getValue() {
			// FIXME produce a better result for a null value
			throw new NoSuchMethodError();
		}
		
		public abstract Class<?> getJavaType();

		public Class<?> getFieldType() {
			return getJavaType();
		}
	
	}

}
