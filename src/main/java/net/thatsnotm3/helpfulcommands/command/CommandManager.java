package net.thatsnotm3.helpfulcommands.command;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class CommandManager{

    public static boolean RunChecks(String cmd, ServerPlayerEntity player){
        if(!player.hasPermissionLevel(2)){
            player.sendMessage(Text.literal("\u00A7cYou can't use Helpful Commands: Insufficient Privileges!"));
            return false;
        }
        return true;
    }
}
