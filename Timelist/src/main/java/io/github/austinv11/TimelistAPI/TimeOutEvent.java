package io.github.austinv11.TimelistAPI;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TimeOutEvent extends Event{
	private static final HandlerList handlers = new HandlerList();
	private Player player;
	public TimeOutEvent(Player play){
		player = play;
	}
	public HandlerList getHandlers() {
	    return handlers;
	}
	 
	public static HandlerList getHandlerList() {
	    return handlers;
	}
	public Player getPlayer(){
		return player;
	}
}
