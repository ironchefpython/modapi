package org.ironchefpython.modapi;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;

import org.ironchefpython.modapi.error.PropertyError;

public interface DynamicProperty {

	public DynamicProperty cloneWith(Object object) throws PropertyError;
	public Object getValue();
	public void addToClass(String key, CtClass comp) throws CannotCompileException, NotFoundException;
	public Class<?> getType();

}
