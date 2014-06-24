package io.github.austinv11.TimelistAPI;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TimelistRunnable extends BukkitRunnable{
	private Player play;
	public TimelistRunnable(Player player){
		play = player;
	}
	@Override
	public void run(){
		int time = TimelistHandler.getRemainingTime(play.getUniqueId().toString());
		if (time != -1 && time != 0){
			time--;
			TimelistHandler.setTime(play.getUniqueId().toString(), time);
		}else if (time == 0){
			TimeOutEvent event = new TimeOutEvent(play);
			Bukkit.getServer().getPluginManager().callEvent(event);
			Bukkit.getLogger().info("Alert: Player "+play.getName()+", with UUID "+play.getUniqueId().toString()+" has ran out of time!");
			this.cancel();
		}
	}
}
