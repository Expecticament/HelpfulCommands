package com.thatsnotm3.helpfulcommands.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;
import com.thatsnotm3.helpfulcommands.HelpfulCommands;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager{

    private static final Gson GSON=new GsonBuilder().setPrettyPrinting().create();

    static final String CONFIG_FILE_NAME="helpfulcommands3.json";
    static final String CONFIG_FILE_FOLDER="config";

    public static final class ModCommandProperties{
        public Boolean enabled=true;
        //public String[] aliases=new String[]{};
    }
    public static final class ModConfig{
        public int explosionPowerLimit=25;
        public Map<String,ModCommandProperties> commandProperties=new HashMap<String,ModCommandProperties>();
    }

    public static ModConfig loadConfig(MinecraftServer server){
        ModConfig ret;

        Path folder = server.getSavePath(WorldSavePath.ROOT).resolve(CONFIG_FILE_FOLDER);
        Path filePath=folder.resolve(CONFIG_FILE_NAME);
        String file = server.getSavePath(WorldSavePath.ROOT).normalize() + "/" + CONFIG_FILE_FOLDER + "/" + CONFIG_FILE_NAME;

        if(Files.exists(filePath)){
            try{
                FileReader reader=new FileReader(file);
                ret=GSON.fromJson(reader, ModConfig.class);
                reader.close();
            } catch (IOException e) {
                ret=new ModConfig();
                throwIOException(e);
            }
        } else{ ret=new ModConfig(); }

        return ret;
    }

    public static void saveConfig(ModConfig cfg,MinecraftServer server){
        Path folder=server.getSavePath(WorldSavePath.ROOT).resolve(CONFIG_FILE_FOLDER);
        Path file=folder.resolve(CONFIG_FILE_NAME);

        if(Files.notExists(folder)){
            try{
                Files.createDirectories(folder);
            } catch(IOException e){
                throwIOException(e);
                return;
            }
        }

        ModConfig newCfg=loadConfig(server);
        newCfg.commandProperties.putAll(cfg.commandProperties);

        try{
            FileWriter writer=new FileWriter(file.normalize().toString());
            writer.write(GSON.toJson(cfg));
            writer.flush();
            writer.close();
        } catch(IOException e) {
            throwIOException(e);
            return;
        }
    }

    public static Map<String,ModCommandProperties> getModCommandProperties(MinecraftServer server){
        return loadConfig(server).commandProperties;
    }

    static void throwIOException(IOException e){
        HelpfulCommands.LOGGER.error("Config file operation failed!", e);
    }
}