package net.thatsnotm3.helpfulcommands.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.server.command.CommandManager;
import net.minecraft.util.Formatting;

public class CMD_Day{

    static final String cmdName="day";

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment){
        dispatcher.register(CommandManager.literal(cmdName).executes(CMD_Day::run));
    }

    public static int run(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException{
        ServerPlayerEntity player = ctx.getSource().getPlayer();

        if(!ModCommandManager.RunChecks(cmdName,player)) return -1;
        
        player.getWorld().setTimeOfDay(1000);
        player.sendMessage(Text.translatable("message.command.dayTime",Text.translatable("message.command.dayTime."+cmdName).formatted(Formatting.AQUA)).formatted(Formatting.GREEN));

        return 1;
    }
}