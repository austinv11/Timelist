package io.github.austinv11.TimelistAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TimelistHandler {
	public static void addPlayer(Player player, int time){
		String uuid = player.getUniqueId().toString().replace("-", "");
		JSONObject json = new JSONObject();
		JSONParser parser = new JSONParser();
		Object obj;
		JSONArray whitelist;
		try {
			File f = new File("timelist.json");
			if (f.exists()){
				obj = parser.parse(new FileReader("timelist.json"));
				whitelist = (JSONArray) obj;
			}else{
				whitelist = new JSONArray();
			}
			json.put("uuid", uuid);
			json.put("name", player.getName());
			json.put("time", Integer.toString(time));
			whitelist.add(json);
			FileWriter file = new FileWriter("timelist.json");
			file.write(whitelist.toJSONString());
			file.flush();
			file.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
	}
	public static void addPlayerRaw(String UUID, String playerName, int time){
		UUID = UUID.replace("-", "");
		JSONObject json = new JSONObject();
		JSONParser parser = new JSONParser();
		Object obj;
		JSONArray whitelist;
		try {
			File f = new File("timelist.json");
			if (f.exists()){
				obj = parser.parse(new FileReader("timelist.json"));
				whitelist = (JSONArray) obj;
			}else{
				whitelist = new JSONArray();
			}
			json.put("uuid", UUID);
			json.put("name", playerName);
			json.put("time", Integer.toString(time));
			whitelist.add(json);
			FileWriter file = new FileWriter("timelist.json");
			file.write(whitelist.toJSONString());
			file.flush();
			file.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
	}
	public static String listTimelistByName(){
		JSONParser parser = new JSONParser();
		String returnVal = null;
		String name;
		int time;
		try {
			Object obj = parser.parse(new FileReader("timelist.json"));
			JSONArray whitelist = (JSONArray) obj;
			for(int i = 0; i < whitelist.size(); i++){
				JSONObject json = (JSONObject) whitelist.get(i);
				name = (String) json.get("name");
				time = Integer.parseInt((String) json.get("time"));
				BigDecimal rTime1 = new BigDecimal(time/60);
				BigDecimal rTime2 = rTime1.setScale(2, RoundingMode.DOWN);
				if (time > 0){
					if (returnVal == null){
						returnVal = name+"- "+rTime2+"h";
					}else{
						returnVal = returnVal+", "+name+"- "+rTime2+"h";
					}
				}else if (time == -1){
					if (returnVal == null){
						returnVal = name+"- infinite h";
					}else{
						returnVal = returnVal+", "+name+"- infinite h";
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return returnVal;
	}
	public static String listTimelistByUUID(){
		JSONParser parser = new JSONParser();
		String returnVal = null;
		String uuid;
		int time;
		try {
			Object obj = parser.parse(new FileReader("timelist.json"));
			JSONArray whitelist = (JSONArray) obj;
			for(int i = 0; i < whitelist.size(); i++){
				JSONObject json = (JSONObject) whitelist.get(i);
				uuid = (String) json.get("uuid");
				time = Integer.parseInt((String) json.get("time"));
				BigDecimal rTime1 = new BigDecimal(time/60);
				BigDecimal rTime2 = rTime1.setScale(2, RoundingMode.DOWN);
				if (time != 0){
					if (returnVal == null){
						returnVal = uuid+"- "+rTime2+"h";
					}else{
						returnVal = returnVal+", "+uuid+"- "+rTime2+"h";
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return returnVal;
	}
	public static void setTime(String UUID, int time){
		JSONParser parser = new JSONParser();
		JSONArray whitelist2 = new JSONArray();
		String uuid;
		UUID = UUID.replace("-", "");
		String player;
		try {
			Object obj = parser.parse(new FileReader("timelist.json"));
			JSONArray whitelist = (JSONArray) obj;
			for(int i = 0; i < whitelist.size(); i++){
				JSONObject json = (JSONObject) whitelist.get(i);
				uuid = (String) json.get("uuid");
				uuid = uuid.replace("-", "");
				player = (String) json.get("name");
				if (uuid.contains(UUID)){
					JSONObject json2 = new JSONObject();
					json2.put("uuid", uuid);
					json2.put("player", player);
					json2.put("time", Integer.toString(time));
					whitelist2.add(json2);
				}else{
					whitelist2.add(json);
				}
			}
			FileWriter file = new FileWriter("timelist.json");
			file.write(whitelist2.toJSONString());
			file.flush();
			file.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	public static void setTime(Player player, int time){
		JSONParser parser = new JSONParser();
		JSONArray whitelist2 = new JSONArray();
		String uuid;
		String playerName;
		try {
			Object obj = parser.parse(new FileReader("timelist.json"));
			JSONArray whitelist = (JSONArray) obj;
			for(int i = 0; i < whitelist.size(); i++){
				JSONObject json = (JSONObject) whitelist.get(i);
				uuid = (String) json.get("uuid");
				uuid = uuid.replace("-", "");
				playerName = (String) json.get("name");
				if (player.getName().contains(playerName)){
					JSONObject json2 = new JSONObject();
					json2.put("uuid", uuid);
					json2.put("player", player);
					json2.put("time", Integer.toString(time));
					whitelist2.add(json2);
				}else{
					whitelist2.add(json);
				}
			}
			FileWriter file = new FileWriter("timelist.json");
			file.write(whitelist2.toJSONString());
			file.flush();
			file.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	public static void removePlayer(String UUID){
		JSONParser parser = new JSONParser();
		JSONArray whitelist2 = new JSONArray();
		String uuid;
		UUID = UUID.replace("-", "");
		try {
			Object obj = parser.parse(new FileReader("timelist.json"));
			JSONArray whitelist = (JSONArray) obj;
			for(int i = 0; i < whitelist.size(); i++){
				JSONObject json = (JSONObject) whitelist.get(i);
				uuid = (String) json.get("uuid");
				uuid = uuid.replace("-", "");
				if (!uuid.contains(UUID)){
					whitelist2.add(json);
				}
			}
			FileWriter file = new FileWriter("timelist.json");
			file.write(whitelist2.toJSONString());
			file.flush();
			file.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	public static void removePlayer(Player player){
		JSONParser parser = new JSONParser();
		JSONArray whitelist2 = new JSONArray();
		String playerName;
		try {
			Object obj = parser.parse(new FileReader("timelist.json"));
			JSONArray whitelist = (JSONArray) obj;
			for(int i = 0; i < whitelist.size(); i++){
				JSONObject json = (JSONObject) whitelist.get(i);
				playerName = (String) json.get("name");
				if (!player.getName().contains(playerName)){
					whitelist2.add(json);
				}
			}
			FileWriter file = new FileWriter("timelist.json");
			file.write(whitelist2.toJSONString());
			file.flush();
			file.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	public static boolean timedWhitelistStatus(String UUID){
		JSONParser parser = new JSONParser();
		boolean returnVal = false;
		String uuid;
		UUID = UUID.replace("-", "");
		int time;
		try {
			Object obj = parser.parse(new FileReader("timelist.json"));
			JSONArray whitelist = (JSONArray) obj;
			for(int i = 0; i < whitelist.size(); i++){
				JSONObject json = (JSONObject) whitelist.get(i);
				uuid = (String) json.get("uuid");
				uuid = uuid.replace("-", "");
				time = Integer.parseInt((String) json.get("time"));
				if (uuid.contains(UUID) && time != 0){
					returnVal = true;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return returnVal;
	}
	public static boolean timedWhitelistStatus(Player player){
		JSONParser parser = new JSONParser();
		boolean returnVal = false;
		String playerName;
		int time;
		try {
			Object obj = parser.parse(new FileReader("timelist.json"));
			JSONArray whitelist = (JSONArray) obj;
			for(int i = 0; i < whitelist.size(); i++){
				JSONObject json = (JSONObject) whitelist.get(i);
				playerName = (String) json.get("name");
				time = Integer.parseInt((String) json.get("time"));
				if (playerName.contains(player.getName()) && time != 0){
					returnVal = true;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return returnVal;
	}
	public static boolean isWhitelisted(String UUID){
		JSONParser parser = new JSONParser();
		boolean returnVal = false;
		String uuid;
		UUID = UUID.replace("-", "");
		try {
			Object obj = parser.parse(new FileReader("timelist.json"));
			JSONArray whitelist = (JSONArray) obj;
			for(int i = 0; i < whitelist.size(); i++){
				JSONObject json = (JSONObject) whitelist.get(i);
				uuid = (String) json.get("uuid");
				uuid = uuid.replace("-", "");
				if (uuid.contains(UUID)){//FIXME Improve
					returnVal = true;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return returnVal;
	}
	public static boolean isWhitelisted(Player player){
		JSONParser parser = new JSONParser();
		boolean returnVal = false;
		String playerName;
		try {
			Object obj = parser.parse(new FileReader("timelist.json"));
			JSONArray whitelist = (JSONArray) obj;
			for(int i = 0; i < whitelist.size(); i++){
				JSONObject json = (JSONObject) whitelist.get(i);
				playerName = (String) json.get("name");
				if (playerName.contains(player.getName())){//FIXME Improve
					returnVal = true;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return returnVal;
	}
	public static int getRemainingTime(String UUID){
		JSONParser parser = new JSONParser();
		int returnVal = 0;
		String uuid;
		UUID = UUID.replace("-", "");
		int time;
		String sTime;
		try {
			Object obj = parser.parse(new FileReader("timelist.json"));
			JSONArray whitelist = (JSONArray) obj;
			for(int i = 0; i < whitelist.size(); i++){
				JSONObject json = (JSONObject) whitelist.get(i);
				uuid = (String) json.get("uuid");
				uuid = uuid.replace("-", "");
				sTime = (String) json.get("time");
				time = Integer.parseInt(sTime);
				if (uuid.contains(UUID)){
					returnVal = time;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return returnVal;
	}
	public static int getRemainingTime(Player player){
		JSONParser parser = new JSONParser();
		int returnVal = 0;
		String playerName;
		int time;
		String sTime;
		try {
			Object obj = parser.parse(new FileReader("timelist.json"));
			JSONArray whitelist = (JSONArray) obj;
			for(int i = 0; i < whitelist.size(); i++){
				JSONObject json = (JSONObject) whitelist.get(i);
				playerName = (String) json.get("name");
				sTime = (String) json.get("time");
				time = Integer.parseInt(sTime);
				if (playerName.contains(player.getName())){
					returnVal = time;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return returnVal;
	}
}