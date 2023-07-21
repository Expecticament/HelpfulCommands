package net.thatsnotm3.helpfulcommands.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;
import net.thatsnotm3.helpfulcommands.HelpfulCommands;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
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
        public int configOPLevel=3;
        public int explosionPowerLimit=75;
        public Map<String,ModCommandProperties> commandProperties=new HashMap<String,ModCommandProperties>();
    }

    public static ModConfig loadConfig(MinecraftServer server){
        ModConfig ret=null;

        Path folder = server.getSavePath(WorldSavePath.ROOT).resolve(CONFIG_FILE_FOLDER);
        Path filePath=folder.resolve(CONFIG_FILE_NAME);
        String file = server.getSavePath(WorldSavePath.ROOT).normalize() + "/" + CONFIG_FILE_FOLDER + "/" + CONFIG_FILE_NAME;

        if(Files.exists(filePath)){
            try{
                FileReader reader=new FileReader(file);
                ret=GSON.fromJson(reader, ModConfig.class);
                reader.close();
            } catch (IOException e) {
                ret=null;
                throwIOException(e);
            }
        }

        if(ret==null) ret=new ModConfig();
        else{
            if(ret.configOPLevel<0) ret.configOPLevel=0;
            if(ret.configOPLevel>4) ret.configOPLevel=4;
            if(ret.explosionPowerLimit<0) ret.explosionPowerLimit=0;
            for(ModCommandProperties i : ret.commandProperties.values()){
                if(i.opLevel<0) i.opLevel=0;
                if(i.opLevel>4) i.opLevel=4;
            }
        }
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

    static void throwIOException(IOException e){
        HelpfulCommands.LOGGER.error("Failed to save, load or create server config file", e);
    }
}