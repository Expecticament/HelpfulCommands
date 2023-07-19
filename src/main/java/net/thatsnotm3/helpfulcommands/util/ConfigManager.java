package net.thatsnotm3.helpfulcommands.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;
import net.thatsnotm3.helpfulcommands.HelpfulCommands;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class ConfigManager{
    private static final Gson GSON=new GsonBuilder().setPrettyPrinting().create();

    static final String CONFIG_FILE_NAME="helpfulcommands.json";
    static final String CONFIG_FILE_FOLDER="config";

    public static final class ModCommandProperties{
        public Boolean enabled=true;
        public int opLevel=2;
    }
    public static final class ModConfig{
        public Boolean uncapExplosionPower=false;
        public Map<String,ModCommandProperties> commandProperties;
    }

    public static ModConfig loadConfig(MinecraftServer server){
        ModConfig ret=new ModConfig();

        Path folder=server.getSavePath(WorldSavePath.ROOT).resolve(CONFIG_FILE_FOLDER);
        String file=server.getSavePath(WorldSavePath.ROOT).normalize()+"/"+CONFIG_FILE_FOLDER+"/"+CONFIG_FILE_NAME;
        HelpfulCommands.LOGGER.info(file);
        if(Files.exists(folder)) {
            try(FileReader reader=new FileReader(file)){
                ret=GSON.fromJson(reader,ModConfig.class);
            } catch(IOException e){
                throwIOException(e);
            }
        }

        return ret;
    }

    public static void saveConfig(ModConfig cfg,MinecraftServer server){
        Path folder=server.getSavePath(WorldSavePath.ROOT).resolve(CONFIG_FILE_FOLDER);
        String file=server.getSavePath(WorldSavePath.ROOT).normalize()+"/"+CONFIG_FILE_FOLDER+"/"+CONFIG_FILE_NAME;
        HelpfulCommands.LOGGER.info(file);
        if(Files.notExists(folder)){
            try{
                Files.createDirectories(folder);
            } catch(IOException e){
                throwIOException(e);
                return;
            }
        }
        String json=GSON.toJson(cfg);
        try(FileWriter writer=new FileWriter(file)){
            writer.write(json);
        } catch(IOException e){
            throwIOException(e);
        }
    }

    static void throwIOException(IOException e){
        HelpfulCommands.LOGGER.error("Failed to save, load or create server config file", e);
    }
}