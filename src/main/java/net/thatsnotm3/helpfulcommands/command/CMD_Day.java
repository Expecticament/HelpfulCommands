package net.thatsnotm3.helpfulcommands.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.server.command.CommandManager;

public class CMD_Day{
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment){
        dispatcher.register(CommandManager.literal("day").executes(CMD_Day::run));
    }

    public static int run(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException{
        ServerPlayerEntity player = ctx.getSource().getPlayer();

        if(!net.thatsnotm3.helpfulcommands.command.CommandManager.RunChecks("day",player)) return -1;
        
        player.getWorld().setTimeOfDay(1000);
        player.sendMessage(Text.literal("Changed daytime to \u00A7bDay"));

        return 1;
    }
}