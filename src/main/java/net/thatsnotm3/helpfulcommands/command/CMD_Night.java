package net.thatsnotm3.helpfulcommands.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.server.command.CommandManager;

public class CMD_Night{
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment){
        dispatcher.register(CommandManager.literal("night").executes(CMD_Night::run));
    }

    public static int run(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException{
        ServerPlayerEntity player = ctx.getSource().getPlayer();

        if(!ModCommandManager.RunChecks("night",player)) return -1;
        
        player.getServerWorld().setTimeOfDay(13000);
        player.sendMessage(Text.literal("Changed daytime to \u00A7bNight"));

        return 1;
    }
}