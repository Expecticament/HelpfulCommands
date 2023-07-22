package net.thatsnotm3.helpfulcommands.command;

import java.io.IOException;
import java.util.Map;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.thatsnotm3.helpfulcommands.HelpfulCommands;
import net.thatsnotm3.helpfulcommands.gamerule.ModGameRules;
import net.thatsnotm3.helpfulcommands.util.ConfigUtils;

public class CommandManager{

    public static boolean RunChecks(String cmd, ServerPlayerEntity player){
        if(!player.hasPermissionLevel(2)){
            player.sendMessage(Text.literal("\u00A7cYou can't use Helpful Commands: Insufficient Privileges!"));
            return false;
        }
        if(!player.method_48926().getGameRules().getBoolean(ModGameRules.HC_ENABLED)){
            player.sendMessage(Text.literal("\u00A7cHelpful Commands Mod is disabled in this world!"));
            return false;
        }

        Map<String, Boolean> map;
        try{
            map=ConfigUtils.loadConfig(player.getServer());
        } catch (IOException e){
            HelpfulCommands.LOGGER.error("Failed to load or create server config file", e);
            return false;
        }
        if(map.containsKey(cmd)) if(!map.get(cmd)){
            player.sendMessage(Text.literal("\u00A7cThis command is disabled in this world!"));
            return false;
        }
        return true;
    }
}