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
			if (args[0] == "help"){
				if (args.length < 2){
					sender.sendMessage("Here's a list of possible /timelist commands:");
					sender.sendMessage("/timelist help, /timelist list, /timelist add, /timelist remove, /timelist set");
					sender.sendMessage("Use /timelist help <command> for help with that command");
				}else{
					if (args[1].toLowerCase() == "help"){
						sender.sendMessage(ChatColor.RED+"Usages: /timelist help <optional:command>");
						sender.sendMessage(ChatColor.RED+"Use /timelist help <command> for help with that command");
					}else if (args[1].toLowerCase() == "list"){
						sender.sendMessage(ChatColor.RED+"Usages: /timelist list <optional:uuid|player>");
						sender.sendMessage(ChatColor.RED+"Lists the players whitelisted (with time remaining)");
					}else if (args[1].toLowerCase() == "add"){
						sender.sendMessage(ChatColor.RED+"Usages: /timelist add player <player> <optional:time> or /timelist add time <player> <optional:time>");
						sender.sendMessage(ChatColor.RED+"Adds a player with specified time (infinite if empty) or adds specified time (infinite if empty) to a player");
					}else if (args[1].toLowerCase() == "remove"){
						sender.sendMessage(ChatColor.RED+"Usages: /timelist remove <player> or /timelist remove time <player>");
						sender.sendMessage(ChatColor.RED+"Removes a player from the whitelist or sets time to 0");
					}else if (args[1].toLowerCase() == "set"){
						sender.sendMessage(ChatColor.RED+"Usages: /timelist set <player> <optional:time>");
						sender.sendMessage(ChatColor.RED+"Sets the time (infinite if empty) for the player");
					}
				}
			}else{
				sender.sendMessage("Use /timelist help for help");
			}
			return true;
		}
		return false;
	}
}
