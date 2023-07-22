package net.thatsnotm3.helpfulcommands.command;

import java.util.List;
import java.util.ArrayList;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.thatsnotm3.helpfulcommands.gamerule.ModGameRules;
import net.thatsnotm3.helpfulcommands.util.ConfigManager;

public class ModCommandManager{

    public static List<String> commands=new ArrayList<String>(){{
        add("back");
        add("day");
        add("dimension");
        add("explosion");
        add("extinguish");
        add("feed");
        add("fly");
        add("gm");
        add("god");
        add("heal");
        add("home");
        add("ignite");
        add("jump");
        add("killitems");
        add("lightning");
        add("night");
        add("spawn");
    }};

    public static void registerCommands(){
        CommandRegistrationCallback.EVENT.register(CMD_Hc::register);
        CommandRegistrationCallback.EVENT.register(CMD_Gm::register);
        CommandRegistrationCallback.EVENT.register(CMD_Day::register);
        CommandRegistrationCallback.EVENT.register(CMD_Night::register);
        CommandRegistrationCallback.EVENT.register(CMD_Spawn::register);
        CommandRegistrationCallback.EVENT.register(CMD_Dimension::register);
        CommandRegistrationCallback.EVENT.register(CMD_Jump::register);
        CommandRegistrationCallback.EVENT.register(CMD_Explosion::register);
        CommandRegistrationCallback.EVENT.register(CMD_Lightning::register);
        CommandRegistrationCallback.EVENT.register(CMD_Killitems::register);
        CommandRegistrationCallback.EVENT.register(CMD_Home::register);
        CommandRegistrationCallback.EVENT.register(CMD_Back::register);
        CommandRegistrationCallback.EVENT.register(CMD_Feed::register);
        CommandRegistrationCallback.EVENT.register(CMD_Heal::register);
        CommandRegistrationCallback.EVENT.register(CMD_Extinguish::register);
        CommandRegistrationCallback.EVENT.register(CMD_Ignite::register);
        CommandRegistrationCallback.EVENT.register(CMD_Fly::register);
        CommandRegistrationCallback.EVENT.register(CMD_God::register);
    }

    public static boolean RunChecks(String cmd, ServerPlayerEntity player){
        ConfigManager.ModConfig cfg;
        cfg=ConfigManager.loadConfig(player.getServer());
        ConfigManager.ModCommandProperties cmdProperties=cfg.commandProperties.getOrDefault(cmd,new ConfigManager.ModCommandProperties());

        if(!player.method_48926().getGameRules().getBoolean(ModGameRules.HC_ENABLED)){
            player.sendMessage(Text.translatable("message.error.mod.disabled").formatted(Formatting.RED));
            return false;
        }
        if(!cmdProperties.enabled && cmd!="hc"){
            player.sendMessage(Text.translatable("message.error.command.disabled",Text.literal("/"+cmd).formatted(Formatting.GOLD)).formatted(Formatting.RED));
            return false;
        }
        if(!player.hasPermissionLevel(cmdProperties.opLevel) && cmd!="hc"){
            player.sendMessage(Text.translatable("message.error.command.insufficientPrivileges",Text.literal("/"+cmd).formatted(Formatting.GOLD),Text.literal(Integer.toString(cmdProperties.opLevel)).formatted(Formatting.GOLD),Text.literal(Integer.toString(getPlayerPermissionLevel(player))).formatted(Formatting.GOLD)).formatted(Formatting.RED));
            return false;
        }
        return true;
    }

    public static int getPlayerPermissionLevel(ServerPlayerEntity player){
        return player.hasPermissionLevel(4) ? 4 : player.hasPermissionLevel(3) ? 3 : player.hasPermissionLevel(2) ? 2 : player.hasPermissionLevel(1) ? 1 : 0;
    }

    public static void sendConfigEditingInsufficientPrivilegesMessage(ServerPlayerEntity player){
        ConfigManager.ModConfig cfg=ConfigManager.loadConfig(player.getServer());
        Formatting opLevel=Formatting.GOLD;
        player.sendMessage(Text.translatable("message.error.config.insufficientPrivileges",Text.literal(Integer.toString(cfg.configOPLevel)).formatted(opLevel),Text.literal(Integer.toString(getPlayerPermissionLevel(player))).formatted(opLevel)).formatted(Formatting.RED));
    }
}