package net.thatsnotm3.helpfulcommands.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.server.command.CommandManager;

public class CMD_Killitems{
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment){
        dispatcher.register(CommandManager.literal("killitems").executes(CMD_Killitems::run));
    }

    public static int run(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException{
        ServerPlayerEntity player=ctx.getSource().getPlayer();

        if(!ModCommandManager.RunChecks("killitems",player)) return -1;

        player.getServer().getCommandManager().executeWithPrefix(ctx.getSource(), "kill @e[type=item]");
        player.sendMessage(Text.literal("\u00A7b/kill @e[type=item]"),true);

        return 1;
    }
}