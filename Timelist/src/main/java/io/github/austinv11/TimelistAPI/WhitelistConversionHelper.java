package io.github.austinv11.TimelistAPI;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class WhitelistConversionHelper {
	private static boolean isTimelist;
	public static void whitelistToTimelist(){
		JSONParser parser = new JSONParser();
		String name;
		String uuid;
		try {
			Object obj = parser.parse(new FileReader("whitelist.json"));
			JSONArray whitelist = (JSONArray) obj;
			for(int i = 0; i < whitelist.size(); i++){
				JSONObject json = (JSONObject) whitelist.get(i);
				uuid = (String) json.get("uuid");
				name = (String) json.get("name");
				TimelistHandler.addPlayerRaw(uuid, name, -1);//Pre-whitelisted players are allowed forever
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		isTimelist = true;
	}
	public static void timelistToWhitelist(){
		JSONParser parser = new JSONParser();
		String uuid;
		try {
			Object obj = parser.parse(new FileReader("timelist.json"));
			JSONArray whitelist = (JSONArray) obj;
			for(int i = 0; i < whitelist.size(); i++){
				JSONObject json = (JSONObject) whitelist.get(i);
				uuid = (String) json.get("uuid");
				Bukkit.getPlayer(UUID.fromString(uuid)).setWhitelisted(true);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		isTimelist = false;
	}
	public static boolean getTimelistStatus(){//Returns if the whitelist mode is timelist
		return isTimelist;
	}
}
