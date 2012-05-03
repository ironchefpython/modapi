package org.mockengine.event;

import org.mockengine.Player;

/**
 * Represents a player-related event.
 * @author simplyianm
 *
 */
public abstract class PlayerEvent extends AbstractEvent {
	private final Player player;

	public PlayerEvent(String type, Player player) {
		super("player:" + type);
		this.player = player;
	}

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

}
