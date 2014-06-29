package io.github.austinv11.Timelist;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

import io.github.austinv11.TimelistAPI.ConverterHelper;
import io.github.austinv11.TimelistAPI.TimeOutEvent;
import io.github.austinv11.TimelistAPI.TimelistHandler;
import io.github.austinv11.TimelistAPI.TimelistScheduler;
import io.github.austinv11.TimelistAPI.WhitelistConversionHelper;
import io.github.austinv11.WebUtils.JSONArrayReader;
import me.armar.plugins.UUIDManager.UUIDManager;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Timelist extends JavaPlugin implements Listener{
	FileConfiguration config = getConfig();
	@Override
	public void onEnable(){
		configInit(false);
		if (config.getBoolean("Options.setToDefault")){
			configInit(true);
		}
		getServer().getPluginManager().registerEvents(this, this);
		//getServer().setWhitelist(true);//TODO remove
		if (getServer().hasWhitelist()){//Removes whitelist when enabled and transfers to new whitelist system
			File f = new File("whitelist.json");
			if (f.exists()){
				WhitelistConversionHelper.whitelistToTimelist();
				getServer().setWhitelist(false);
				getLogger().info("Converted whitelist to timelist!");
			}else{
				getLogger().severe("Error: whitelist.json and/or white-list.txt could not found! Please consider updating the version of bukkit");
				getLogger().info("Disabling this plugin...");
				getServer().getPluginManager().disablePlugin(this);
			}
		}
	}
	@Override
	public void onDisable(){
		
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onAttemptedLogin(AsyncPlayerPreLoginEvent event){
		if (event.getLoginResult() == Result.ALLOWED){
			if (!TimelistHandler.isWhitelisted(event.getUniqueId().toString())){
				event.setLoginResult(Result.KICK_WHITELIST);
				event.setKickMessage(ChatColor.RED+config.getString("Options.whitelistFailureMessage"));
			}else if (TimelistHandler.getRemainingTime(event.getUniqueId().toString()) == 0){
				event.setLoginResult(Result.KICK_OTHER);
				event.setKickMessage(ChatColor.RED+config.getString("Options.timeOutLoginMessage"));
			}
		}
	}
	@EventHandler
	public void onLogin(PlayerLoginEvent event){
		if (event.getPlayer().isOp()){
			TimelistHandler.setTime(event.getPlayer().getUniqueId().toString(), -1);
			if (config.getBoolean("Options.updateNotifications")){
				try{
					JSONArray array = JSONArrayReader.readJsonFromUrl("http://austinv11.github.io/api/Timelist/news.json");
					for (int i = 0; i < array.size(); i++){
						JSONObject json = (JSONObject) array.get(i);
						String ver = (String) json.get("version");
						if (ver.contains(this.getDescription().getVersion())){
							if (((String) json.get("severity")) == "1"){
								event.getPlayer().sendMessage("[Timelist][Info] "+((String) json.get("message")));
							}else if (((String) json.get("severity")) == "2"){
								event.getPlayer().sendMessage("[Timelist]["+ChatColor.GOLD+"Important"+ChatColor.RESET+"] "+((String) json.get("message")));
							}else{
								event.getPlayer().sendMessage("[Timelist]["+ChatColor.RED+"VERY Important!"+ChatColor.RESET+"] "+((String) json.get("message")));
							}
						}
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		}
		new TimelistScheduler(event.getPlayer(), this);
		if (TimelistHandler.getRemainingTime(event.getPlayer().getUniqueId().toString()) == -1){
			event.getPlayer().sendMessage(ChatColor.GOLD+"You have: "+ChatColor.AQUA+"infinite"+ChatColor.GOLD+" hours remaining");
		}else{
			BigDecimal rTime1 = new BigDecimal(TimelistHandler.getRemainingTime(event.getPlayer().getUniqueId().toString())/60);
			BigDecimal rTime2 = rTime1.setScale(2, RoundingMode.DOWN);
			event.getPlayer().sendMessage(ChatColor.GOLD+"You have: "+ChatColor.AQUA+rTime2+ChatColor.GOLD+" hours remaining");
		}
	}
	@EventHandler
	public void onTimeOut(TimeOutEvent event){
		if (config.getBoolean("Options.kickOnTimeOut")){
			event.getPlayer().kickPlayer(ChatColor.RED+config.getString("Options.timeOutMessage"));
		}else{
			event.getPlayer().sendMessage(ChatColor.RED+config.getString("Options.timeOutMessage"));
		}
	}
	public void configInit(boolean revert){
		if (revert == false){
			config.addDefault("Options.setToDefault", false);
			config.addDefault("Options.whitelistFailureMessage", "Sorry, you have not been whitelisted");
			config.addDefault("Options.timeOutLoginMessage", "Sorry, you have run out of time");
			config.addDefault("Options.timeOutMessage", "Uh oh! You've run out of time!");
			config.addDefault("Options.kickOnTimeOut", true);
			config.addDefault("Options.updateNotifications", true);
			config.options().copyDefaults(true);
			saveConfig();
		}else{
			config.set("Options.setToDefault", false);
			config.set("Options.whitelistFailureMessage", "Sorry, you have not been whitelisted");
			config.set("Options.timeOutLoginMessage", "Sorry, you have run out of time");
			config.set("Options.timeOutMessage", "Uh oh! You've run out of time!");
			config.set("Options.kickOnTimeOut", true);
			config.set("Options.updateNotifications", true);
			saveConfig();
		}
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
			if (args[0].equalsIgnoreCase("help") && sender.hasPermission("Timelist.timelistHelp")){
				if (args.length < 2){
					sender.sendMessage("Here's a list of possible /timelist commands:");
					sender.sendMessage("/timelist help, /timelist list, /timelist add, /timelist remove, /timelist set, /timelist time");
					sender.sendMessage("Use /timelist help <command> for help with that command");
				}else{
					if (args[2].toLowerCase() == "help"){
						sender.sendMessage(ChatColor.RED+"Usages: /timelist help <optional:command>");
						sender.sendMessage(ChatColor.RED+"Use /timelist help <command> for help with that command");
					}else if (args[2].toLowerCase() == "list"){
						sender.sendMessage(ChatColor.RED+"Usages: /timelist list <optional:uuid|player>");
						sender.sendMessage(ChatColor.RED+"Lists the players whitelisted (with time remaining)");
					}else if (args[2].toLowerCase() == "add"){
						sender.sendMessage(ChatColor.RED+"Usages: /timelist add player <player> <optional:time> or /timelist add uuid <uuid> <optional: time> or /timelist add time <player> <optional:time>");
						sender.sendMessage(ChatColor.RED+"Adds a player with specified time (infinite if empty) or adds specified time (infinite if empty) to a player");
					}else if (args[2].toLowerCase() == "remove"){
						sender.sendMessage(ChatColor.RED+"Usages: /timelist remove player <player> or or /timelist remove uuid <uuid> /timelist remove time player <player> or /timelist remove time uuid <uuid>");
						sender.sendMessage(ChatColor.RED+"Removes a player from the whitelist or sets time to 0");
					}else if (args[2].toLowerCase() == "set"){
						sender.sendMessage(ChatColor.RED+"Usages: /timelist set <player> <optional:time>");
						sender.sendMessage(ChatColor.RED+"Sets the time (infinite if empty) for the player");
					}else if (args[2].toLowerCase() == "time"){
						sender.sendMessage(ChatColor.RED+"Usages: /timelist time <optional:player>");
						sender.sendMessage(ChatColor.RED+"Gets the remaining time the given player");
					}
				}
			}else if (args[0].equalsIgnoreCase("list") && sender.hasPermission("Timelist.timelistList")){
				if (args.length < 2){
					sender.sendMessage(TimelistHandler.listTimelistByName());
				}else if (args[1].equalsIgnoreCase("uuid")){
					sender.sendMessage(TimelistHandler.listTimelistByUUID());
				}else if (args[1].equalsIgnoreCase("player")){
					sender.sendMessage(TimelistHandler.listTimelistByName());
				}
			}else if (args[0].equalsIgnoreCase("add")){
				if (args[1].equalsIgnoreCase("player")){
					if (args.length < 4){
						TimelistHandler.addPlayerRaw(UUIDManager.getUUIDFromPlayer(args[2]).toString(), args[2], -1);
					}else{
						String[] args2 = ConverterHelper.removeElements(args, 3);
						TimelistHandler.addPlayerRaw(UUIDManager.getUUIDFromPlayer(args[2]).toString(), args[2], ConverterHelper.getTotalTimes(args2));
					}
					sender.sendMessage("Added "+args[2]+" to the timelist!");
				}else if (args[1].equalsIgnoreCase("uuid")){
					if (args.length < 4){
						TimelistHandler.addPlayerRaw(args[2], UUIDManager.getPlayerFromUUID(UUID.fromString(args[2])), -1);
					}else{
						String[] args2 = ConverterHelper.removeElements(args, 3);
						TimelistHandler.addPlayerRaw(args[2], UUIDManager.getPlayerFromUUID(UUID.fromString(args[2])), ConverterHelper.getTotalTimes(args2));
					}
					sender.sendMessage("Added a player with the uuid of: "+args[2]+" to the timelist!");
				}else if (args[1].equalsIgnoreCase("time")){
					if (args.length < 4){
						TimelistHandler.setTime(UUIDManager.getUUIDFromPlayer(args[2]).toString(), -1);
					}else{
						String[] args2 = ConverterHelper.removeElements(args, 3);
						TimelistHandler.setTime(UUIDManager.getUUIDFromPlayer(args[2]).toString(), TimelistHandler.getRemainingTime(UUIDManager.getUUIDFromPlayer(args[2]).toString())+ConverterHelper.getTotalTimes(args2));
					}
					sender.sendMessage("Added time for "+args[3]+"!");
				}
			}else if (args[0].equalsIgnoreCase("remove")){
				if (args[1].equalsIgnoreCase("player")){
					TimelistHandler.removePlayer(UUIDManager.getUUIDFromPlayer(args[2]).toString());
					sender.sendMessage("Removed "+args[2]+" from the timelist!");
				}else if (args[1].equalsIgnoreCase("uuid")){
					TimelistHandler.removePlayer(args[2]);
					sender.sendMessage("Removed a player with the uuid of: "+args[2]+" from the timelist!");
				}else if (args[1].equalsIgnoreCase("time") && args[2].equalsIgnoreCase("player")){
					String[] args2 = ConverterHelper.removeElements(args, 4);
					TimelistHandler.setTime(UUIDManager.getUUIDFromPlayer(args[3]).toString(), TimelistHandler.getRemainingTime(UUIDManager.getUUIDFromPlayer(args[3]).toString())-ConverterHelper.getTotalTimes(args2));
					sender.sendMessage("Removed time from "+args[3]+"!");
				}else{
					String[] args2 = ConverterHelper.removeElements(args, 4);
					TimelistHandler.setTime(args[3], TimelistHandler.getRemainingTime(args[3])-ConverterHelper.getTotalTimes(args2));
					sender.sendMessage("Removed time from a player with the uuid of:"+args[3]+"!");
				}
			}else if (args[0].equalsIgnoreCase("set")){
				if (args.length < 4){
					TimelistHandler.setTime(UUIDManager.getUUIDFromPlayer(args[1]).toString(), -1);
					sender.sendMessage(args[2]+" now has an infinite amount of time!");
				}else{
					String[] args2 = ConverterHelper.removeElements(args, 2);
					TimelistHandler.setTime(UUIDManager.getUUIDFromPlayer(args[1]).toString(), ConverterHelper.getTotalTimes(args2));
					sender.sendMessage("Set the time of "+args[1]+"!");
				}
			}else if (args[0].equalsIgnoreCase("time")){
				if (args.length < 3){
					BigDecimal rTime1 = new BigDecimal(TimelistHandler.getRemainingTime((Player) sender)/60);
					BigDecimal rTime2 = rTime1.setScale(2, RoundingMode.DOWN);
					sender.sendMessage(ChatColor.GOLD+"You currently have "+ChatColor.AQUA+rTime2+ChatColor.GOLD+" hours remaining");
				}else{
					BigDecimal rTime1 = new BigDecimal(TimelistHandler.getRemainingTime(UUIDManager.getUUIDFromPlayer(args[1]).toString())/60);
					BigDecimal rTime2 = rTime1.setScale(2, RoundingMode.DOWN);
					sender.sendMessage(ChatColor.GOLD+args[1]+" currently has "+ChatColor.AQUA+rTime2+ChatColor.GOLD+" hours remaining");
				}
			}else{
				sender.sendMessage("Use /timelist help for help");
			}
			return true;
		}
		return false;
	}
}
