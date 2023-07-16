package net.thatsnotm3.helpfulcommands.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;
import net.minecraft.server.command.CommandManager;

public class CMD_Feed{
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment){
        dispatcher.register(CommandManager.literal("feed").executes(CMD_Feed::run));
    }

    public static int run(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException{
        ServerPlayerEntity player = ctx.getSource().getPlayer();

        if(!ModCommandManager.RunChecks("feed",player)) return -1;

        player.getHungerManager().setFoodLevel(20);
        player.getHungerManager().setSaturationLevel(5);
        if(player.interactionManager.getGameMode()==GameMode.SURVIVAL || player.interactionManager.getGameMode()==GameMode.ADVENTURE) player.getHungerManager().setExhaustion(0);

        player.sendMessage(Text.literal("\u00A7aYou were fed and completely saturated"));

        return 1;
    }
}