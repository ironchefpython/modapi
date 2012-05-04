package org.ironchefpython.modapi;

import java.util.HashMap;
import java.util.Map;

import org.ironchefpython.modapi.error.InvalidComponentRegistration;
import org.ironchefpython.modapi.error.InvalidEventRegistration;
import org.ironchefpython.modapi.error.UnregisteredEventException;


public class ModManager {

	private Map<String, Prototype> prototypes;
	private Map<String, EventFactory> events;
	private String name;
	private static Map<String, ModManager> mods = new HashMap<String, ModManager>();
	
	public static final String[] STRING_ARRAY = new String[0];
	
	public ModManager(String modName) {
		this.name = modName;
		mods.put(name, this);
		prototypes = new HashMap<String, Prototype>();
		events = new HashMap<String, EventFactory>();
	}
	
	
	public EventFactory registerEvent(EventFactory factory) throws InvalidEventRegistration {
		String type = factory.getType();
		if (events.containsKey(type)) {
			throw new InvalidEventRegistration(type);
		}
		events.put(type, factory);
		return factory;
	}

	public EventFactory getEvent(String name) throws UnregisteredEventException {
		EventFactory result = events.get(name);
		if (result == null) {
			throw new UnregisteredEventException(name);
		}
		return result;
	}

	
	public Prototype getPrototype(String name) throws InvalidComponentRegistration {
		Prototype result = prototypes.get(name);
		if (result == null) {
			throw InvalidComponentRegistration.unregisteredPrototype(name);
		}
		return result;
	}
	




	
	public Prototype registerPrototype(Prototype prototype) throws InvalidComponentRegistration {
		String id = prototype.getId();
		if (prototypes.containsKey(id)) {
			throw InvalidComponentRegistration.duplicateRegistration(id);
		}
		prototypes.put(id, prototype);
		return prototype;
	}

	public Prototype[] getPrototypes() {
		return prototypes.values().toArray(new Prototype[prototypes.size()]);
	}



	
	

	
//void makeTools(ModManager m, Prototype material) {
//	String id = material.getId() + "_axe";
//	Prototype axeWithMat = new Prototype(id, m.getPrototype("Axe"));
//	axeWithMat.setProperty("material", material);
//	m.registerPrototype(axeWithMat);
//}
	




}
