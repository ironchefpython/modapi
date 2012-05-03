package org.ironchefpython.modapi;

import org.ironchefpython.modapi.error.PropertyError;

public interface DynamicProperty {

	public DynamicProperty cloneWith(Object object) throws PropertyError;
	public Object getValue();
	public Class<?> getJavaType();
	public Class<?> getFieldType();
	public boolean isStatic();

}
