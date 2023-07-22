package net.thatsnotm3.helpfulcommands.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;
import net.thatsnotm3.helpfulcommands.HelpfulCommands;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ConfigUtils{
    private static final Gson GSON=new GsonBuilder().setPrettyPrinting().create();

    static final String CONFIG_FILE_NAME="hcConfig.json";
    static final String CONFIG_FILE_FOLDER="config";

    static Map<String, Boolean> currentMap;

    public static Map<String, Boolean> loadConfig(MinecraftServer server) throws IOException{
        Map<String, Boolean> currentMap=new HashMap<>();
        Path folder=server.getSavePath(WorldSavePath.ROOT).resolve(CONFIG_FILE_FOLDER);
        Path file=folder.resolve(CONFIG_FILE_NAME);

        if(Files.notExists(folder)){
            return new HashMap<String,Boolean>();
        }

        try(BufferedReader reader=Files.newBufferedReader(file)){
            JsonObject json=JsonParser.parseReader(reader).getAsJsonObject();
            for(Map.Entry<String, JsonElement> entry : json.entrySet()) {
                String command=entry.getKey();
                boolean enabled=entry.getValue().getAsBoolean();
                currentMap.put(command, enabled);
            }
        } catch(IOException e){
            throw new IOException("Failed to load config file!", e);
        }
        return currentMap;
    }

    public static void saveConfig(Map<String,Boolean> map,MinecraftServer server) throws IOException{
        Path folder=server.getSavePath(WorldSavePath.ROOT).resolve(CONFIG_FILE_FOLDER);
        Path file=folder.resolve(CONFIG_FILE_NAME);
        if(Files.notExists(folder)){ 
            try{
                Files.createDirectories(folder);
            } catch(IOException e){
                HelpfulCommands.LOGGER.error("Failed to load or create server config file", e);
                return;
            }
        }
        JsonObject json=new JsonObject();
        for(Map.Entry<String, Boolean> entry : map.entrySet()){
            String command=entry.getKey();
            boolean enabled=entry.getValue();
            json.addProperty(command, enabled);
        }

        try(BufferedWriter writer=Files.newBufferedWriter(file)){
            GSON.toJson(json, writer);
        } catch(IOException e){
            throw new IOException("Failed to save config file!", e);
        }
    }

    public static void initialize(){
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            MinecraftServer minecraftServer=server;
			Path folder=minecraftServer.getSavePath(WorldSavePath.ROOT).resolve(CONFIG_FILE_FOLDER);
			Path file=folder.resolve(CONFIG_FILE_NAME);
			if(Files.notExists(folder)){ 
				try{
					Files.createDirectories(folder);
				} catch(IOException e){
					HelpfulCommands.LOGGER.error("Failed to load or create server config file", e);
				}
			}
			currentMap=new HashMap<>();
            try{
                if(Files.exists(file)){
                    Map<String, Boolean> serverCurrentMap=ConfigUtils.loadConfig(server);
                    currentMap.putAll(serverCurrentMap);
                } else{
                    ConfigUtils.saveConfig(currentMap,server);
                }
            } catch (IOException e){
                HelpfulCommands.LOGGER.error("Failed to load or create server config file", e);
            }
        });
    }
}