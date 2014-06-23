package io.github.austinv11.TimelistAPI;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class TimelistScheduler {
	public TimelistScheduler(Player player, Plugin plugin){
		BukkitTask task = new TimelistRunnable(player.getPlayer()).runTaskTimer(plugin, 0, 1200);
	}
}
