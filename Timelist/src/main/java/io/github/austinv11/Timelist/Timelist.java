package io.github.austinv11.Timelist;

import io.github.austinv11.TimelistAPI.TimelistHandler;
import io.github.austinv11.TimelistAPI.WhitelistConversionHelper;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Timelist extends JavaPlugin implements Listener{
	@Override
	public void onEnable(){
		getServer().getPluginManager().registerEvents(this, this);
		getServer().setWhitelist(true);//TODO remove
		if (getServer().hasWhitelist()){//Removes whitelist when enabled and transfers to new whitelist system
			WhitelistConversionHelper.whitelistToTimelist();
			getServer().setWhitelist(false);
			getLogger().info("Converted whitelist to timelist!");
		}
	}
	@Override
	public void onDisable(){
		
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCommandPreProcess(PlayerCommandPreprocessEvent event){
		if (event.getMessage().toLowerCase().contains("whitelist")){
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.RED+"Sorry, the command you attempted to perform has been replaced by the Timelist plugin, please use /timelist help for  information about its commands");
		}
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("timelist")){
			
			return true;
		}
		return false;
	}
}
