package com.expecticament.helpfulcommands.util;

import com.expecticament.helpfulcommands.command.ModCommandManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.WorldSavePath;
import com.expecticament.helpfulcommands.HelpfulCommands;
import org.apache.commons.lang3.NotImplementedException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class ConfigManager{

    private static final Gson GSON=new GsonBuilder().setPrettyPrinting().create();

    static final String CONFIG_FILE_NAME="helpfulcommands3.json";
    static final String CONFIG_FILE_FOLDER="config";

    public static class ModConfigFieldEntry {
        public Object defaultValue;
        public ArgumentBuilder<ServerCommandSource, ?> configCommandArgument;
        public Callable<Object> getValue=()->{ throw new NotImplementedException(); };
        public CommandContext<ServerCommandSource> context=null;
    }
    public static class ModConfigCommandEntry {
        public boolean isEnabled=true;
        public boolean isPublic=false;
        //public String[] aliases=new String[]{};
    }
    public static class ModConfig{
        public Map<String,Object> fields=new HashMap<>();
        public Map<String, ModConfigCommandEntry> commands=new HashMap<>();
    }
    public static final Map<String, ModConfigFieldEntry> defaultConfigFieldEntries=new HashMap<>(){{
        put("explosionPowerLimit",new ModConfigFieldEntry(){{
            defaultValue=15;
            configCommandArgument=CommandManager.argument("value",IntegerArgumentType.integer(1));
            getValue=()-> IntegerArgumentType.getInteger(context,"value");
        }});
    }};

    public static ModConfig loadConfig(MinecraftServer server){
        ModConfig ret=null;

        Path folder=server.getSavePath(WorldSavePath.ROOT).resolve(CONFIG_FILE_FOLDER);
        Path filePath=folder.resolve(CONFIG_FILE_NAME);
        String file=server.getSavePath(WorldSavePath.ROOT).normalize() + "/" + CONFIG_FILE_FOLDER + "/" + CONFIG_FILE_NAME;

        if(Files.exists(filePath)){
            try{
                FileReader reader=new FileReader(file);
                ret=GSON.fromJson(reader, ModConfig.class);
                reader.close();
            } catch (IOException e) {
                throwIOException(e);
            }
        }

        if(ret==null) ret=new ModConfig();
        for(ModCommandManager.ModCommand i : ModCommandManager.commands) {
            if(i.category==ModCommandManager.ModCommandCategory.Main) continue;
            ModConfigCommandEntry newEntry=new ModConfigCommandEntry();
            newEntry.isEnabled=i.defaultState;
            newEntry.isPublic=i.defaultPublic;
            ret.commands.putIfAbsent(i.name,newEntry);
        }
        for(Map.Entry<String, ModConfigFieldEntry> e : defaultConfigFieldEntries.entrySet()){
            ret.fields.putIfAbsent(e.getKey(),e.getValue().defaultValue);
        }

        return ret;
    }

    public static void saveConfig(ModConfig newCfg,MinecraftServer server){
        Path folder=server.getSavePath(WorldSavePath.ROOT).resolve(CONFIG_FILE_FOLDER);
        Path file=folder.resolve(CONFIG_FILE_NAME);

        if(Files.notExists(folder)) {
            try {
                Files.createDirectories(folder);
            } catch (IOException e) {
                throwIOException(e);
                return;
            }
        }

        try{
            FileWriter writer=new FileWriter(file.normalize().toString());
            writer.write(GSON.toJson(newCfg));
            writer.flush();
            writer.close();
        } catch(IOException e) {
            throwIOException(e);
            return;
        }
    }

    static void throwIOException(IOException e){
        HelpfulCommands.LOGGER.error("Config file operation failed!", e);
    }
}