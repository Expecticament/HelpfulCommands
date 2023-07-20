package net.thatsnotm3.helpfulcommands.command;

import java.util.List;
import java.util.ArrayList;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.thatsnotm3.helpfulcommands.gamerule.ModGameRules;
import net.thatsnotm3.helpfulcommands.util.ConfigManager;

public class ModCommandManager{

    public static List<String> commands=new ArrayList<String>(){{
        add("abilities");
        add("back");
        add("day");
        add("dimension");
        add("explosion");
        add("extinguish");
        add("feed");
        add("gm");
        add("heal");
        add("home");
        add("jump");
        add("killitems");
        add("lightning");
        add("night");
        add("spawn");
    }};

    public static boolean RunChecks(String cmd, ServerPlayerEntity player){
        ConfigManager.ModConfig cfg;
        cfg=ConfigManager.loadConfig(player.getServer());
        ConfigManager.ModCommandProperties cmdProperties=cfg.commandProperties.getOrDefault(cmd,new ConfigManager.ModCommandProperties());

        if(!player.hasPermissionLevel(cmdProperties.opLevel)){
            player.sendMessage(Text.translatable("message.error.command.insufficientPrivileges",Text.literal("/"+cmd).formatted(Formatting.GOLD),Text.literal(Integer.toString(cmdProperties.opLevel)).formatted(Formatting.GOLD),Text.literal(Integer.toString(getPlayerPermissionLevel(player))).formatted(Formatting.GOLD)).formatted(Formatting.RED));
            return false;
        }
        if(!player.getWorld().getGameRules().getBoolean(ModGameRules.HC_ENABLED)){
            player.sendMessage(Text.translatable("message.error.mod.disabled").formatted(Formatting.RED));
            return false;
        }
        if(!cmdProperties.enabled){
            player.sendMessage(Text.translatable("message.error.command.disabled",Text.literal("/"+cmd).formatted(Formatting.GOLD)).formatted(Formatting.RED));
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