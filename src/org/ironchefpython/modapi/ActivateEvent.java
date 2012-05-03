package org.ironchefpython.modapi;

import org.mockengine.Entity;
import org.mockengine.PlayerState;

public class ActivateEvent {
	private PlayerState playerState;
	private Entity target;

	public ActivateEvent(PlayerState pstate, Entity target) {
		this.playerState = pstate;
		this.target = target;
	}
	
	
}
