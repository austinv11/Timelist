package io.github.austinv11.Timelist;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Timelist extends JavaPlugin{
	@Override
	public void onEnable(){
		
	}
	@Override
	public void onDisable(){
		
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("opengui")){
			return true;
		}
		return false;
	}
}
