package org.ironchefpython.modapi.primitives;

import org.ironchefpython.modapi.AbstractDynamicProperty;
import org.ironchefpython.modapi.DynamicProperty;
import org.ironchefpython.modapi.error.PropertyError;

public class TextureProperty implements DynamicProperty {
	public static DynamicProperty TEXTURE_TYPE = new AbstractDynamicProperty() {
		public DynamicProperty cloneWith(Object object) {
			return object == null ? this : new TextureProperty(object);
		}


	};
	
	private Object value;
	
	public TextureProperty(Object object) {
		this.value = object; // FIXME do real texture loading
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