package net.thatsnotm3.helpfulcommands.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.server.command.CommandManager;

public class CMD_Heal{
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment){
        dispatcher.register(CommandManager.literal("heal").executes(CMD_Heal::run));
    }

    public static int run(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException{
        ServerPlayerEntity player = ctx.getSource().getPlayer();

        if(!ModCommandManager.RunChecks("heal",player)) return -1;

        player.setHealth(player.getMaxHealth());

        player.sendMessage(Text.literal("\u00A7aYou were healed"));

        return 1;
    }
}