
package org.mockengine;

import java.util.Map;

import org.mozilla.javascript.Callable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.terasology.entitySystem.ComponentSystem;
import org.terasology.entitySystem.EntityManager;
import org.terasology.entitySystem.EntityRef;
import org.terasology.entitySystem.Event;
import org.terasology.entitySystem.EventReceiver;
import org.terasology.entitySystem.EventSystem;
import org.terasology.game.CoreRegistry;


public class DynamicSystem implements ComponentSystem {
    private final Scriptable scope;
    private final Context cx;
    private final Callable updater;
    private final Class<? extends DynamicComponent> componentClass;
    private final Map<String, Callable> handlers;
    private final String name;
	
	public DynamicSystem(String name, Scriptable scope, Context cx, Callable updater,
			Class<? extends DynamicComponent> compClass,
			Map<String, Callable> handlers) {
		this.name = name;
		this.scope = scope;
		this.cx = cx;
		this.updater = updater;
		this.componentClass = compClass;
		this.handlers = handlers;
	}
	
    private EntityManager entityManager;
	private EventSystem eventSystem;

	public void initialise() {
        entityManager = CoreRegistry.get(EntityManager.class);
    	eventSystem = CoreRegistry.get(EventSystem.class);
    	
    	for (final Map.Entry<String, Callable> e : handlers.entrySet()) {
    		Class<? extends Event> eventClass = getEventByName(e.getKey());

			EventReceiver reciever = new EventReceiver(){
    			public void onEvent(Event event, EntityRef entity) {
    				call(e.getValue(), entity.getComponent(componentClass), new Object[]{event, entity});
    			}
    		};
    		eventSystem.registerEventReceiver(reciever, eventClass, componentClass);
    	}
    }

    
    @SuppressWarnings("unchecked")
	public void update(float delta) {
        for (EntityRef entity : entityManager.iteratorEntities(componentClass)) {
        	DynamicComponent component = entity.getComponent(componentClass);
        	call(updater, component, new Object[] {delta});
            entity.saveComponent(component);
        }
    }
    
	private Object call(Callable callable, DynamicComponent reciever,
			Object[] args) {
		return callable.call(cx, scope,
				(Scriptable) Context.javaToJS(reciever, scope), args);
	}
	
	private Class<? extends Event> getEventByName(String eventType) {
		try {
			return (Class<? extends Event>) ClassLoader.getSystemClassLoader().loadClass(eventType);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	

    
	
}

