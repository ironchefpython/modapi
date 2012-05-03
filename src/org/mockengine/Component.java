package org.mockengine;

import java.util.Collection;
import java.util.Map;

import org.ironchefpython.modapi.DynamicProperty;

public interface Component {

	public String getId();
	public Collection<DynamicProperty> getProperties();
	public Object getPropertyValue(String propertyName);
	public Map<String, DynamicProperty> getPropertyMap();
	public DynamicProperty getType();

}
