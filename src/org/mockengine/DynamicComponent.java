package org.mockengine;

import java.lang.reflect.InvocationTargetException;

import org.mozilla.javascript.Callable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.terasology.entitySystem.AbstractComponent;
import org.terasology.entitySystem.Component;

public class DynamicComponent extends  AbstractComponent {
	
	public static class Constructor {
		private final java.lang.reflect.Constructor<? extends DynamicComponent> wrapped;
		private final Context cx;
		private final Scriptable scope;
		private final Callable callable;
		
		public Constructor(Class<? extends DynamicComponent> cls, Class<?>... parameterTypes) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException {
			this.wrapped = cls.getConstructor(parameterTypes);
			this.cx = (Context) cls.getField("__CX__").get(null);
			this.scope = (Scriptable) cls.getField("__SCOPE__").get(null);
			this.callable = (Callable) cls.getField("__INIT__").get(null);
		}
		public DynamicComponent newInstance(Object... params) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
			DynamicComponent result = wrapped.newInstance(params);
			callable.call(cx, scope, (Scriptable) Context.javaToJS(result, scope), null);
			return result;
		}

	}

	public class Getter {
		private final Context cx;
		private final Scriptable scope;
		private final Callable callable;
		
		public Getter(Class<? extends DynamicComponent> cls, String fieldName)
				throws IllegalArgumentException, SecurityException,
				IllegalAccessException, NoSuchFieldException {
			this.cx = (Context) cls.getField("__CX__").get(null);
			this.scope = (Scriptable) cls.getField("__SCOPE__").get(null);
			this.callable = (Callable) cls.getField(fieldName).get(null);
		}		
		
		public Object get(DynamicComponent instance) {
			return callable.call(cx, scope, (Scriptable) Context.javaToJS(instance, scope), null);
		}
	}
}
