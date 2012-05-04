
package org.mockengine;

import java.util.Map;

import org.mozilla.javascript.Callable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.terasology.entitySystem.EntityManager;
import org.terasology.entitySystem.EntityRef;
import org.terasology.entitySystem.Event;
import org.terasology.entitySystem.EventReceiver;
import org.terasology.entitySystem.EventSystem;
import org.terasology.game.CoreRegistry;


public abstract class DynamicSystem {
    private EntityManager entityManager;
	private EventSystem eventSystem;
	
    public void initialise() {
        entityManager = CoreRegistry.get(EntityManager.class);
    	eventSystem = CoreRegistry.get(EventSystem.class);
    	
    	for (final Map.Entry<String, Callable> e : _getHandlers().entrySet()) {
    		Class<? extends Event> eventClass = getEventByName(e.getKey());

			EventReceiver reciever = new EventReceiver(){
    			public void onEvent(Event event, EntityRef entity) {
    				call(e.getValue(), entity.getComponent(_getComponentClass()), new Object[]{event, entity});
    			}
    		};
    		eventSystem.registerEventReceiver(reciever, eventClass, _getComponentClass());
    	}
    }

    protected abstract Scriptable _getScope();
    protected abstract Context _getContext();
    protected abstract Callable _getUpdater();
    protected abstract Class<? extends DynamicComponent> _getComponentClass();
    protected abstract Map<String, Callable> _getHandlers();
    
    @SuppressWarnings("unchecked")
	public void update(float delta) {
        for (EntityRef entity : entityManager.iteratorEntities(_getComponentClass())) {
        	DynamicComponent component = entity.getComponent(_getComponentClass());
        	call( _getUpdater(), component, new Object[] {delta});
            entity.saveComponent(component);
        }
    }
    
	private Object call(Callable callable, DynamicComponent reciever,
			Object[] args) {
		return callable.call(_getContext(), _getScope(),
				(Scriptable) Context.javaToJS(reciever, _getScope()), args);
	}
	
	private Class<? extends Event> getEventByName(String eventType) {
		try {
			return (Class<? extends Event>) ClassLoader.getSystemClassLoader().loadClass(eventType);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	

    
	
}

