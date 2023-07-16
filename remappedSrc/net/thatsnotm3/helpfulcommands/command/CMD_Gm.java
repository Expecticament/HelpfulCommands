package net.thatsnotm3.helpfulcommands.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;

public class CMD_Gm{
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment){
        dispatcher.register(CommandManager.literal("gm")
            .then(CommandManager.literal("c").executes(ctx->switchGameMode(ctx,0)))
            .then(CommandManager.literal("s").executes(ctx->switchGameMode(ctx,1)))
            .then(CommandManager.literal("a").executes(ctx->switchGameMode(ctx,2)))
            .then(CommandManager.literal("sp").executes(ctx->switchGameMode(ctx,3)))
        );
    }

    public static int switchGameMode(CommandContext<ServerCommandSource> ctx, int gm) throws CommandSyntaxException{ // 0 - Creative; 1 - Survival; 2 - Adventure; 3 - Spectator
        ServerPlayerEntity player=ctx.getSource().getPlayer();

        if(!net.thatsnotm3.helpfulcommands.command.CommandManager.RunChecks("gm",player)) return -1;

        GameMode gameMode;
        String name;
        switch(gm){
            default:
                name="Creative";
                gameMode=GameMode.CREATIVE;
                break;
            case 1:
                name="Survival";
                gameMode=GameMode.SURVIVAL;
                break;
            case 2:
                name="Adventure";
                gameMode=GameMode.ADVENTURE;
                break;
            case 3:
                name="Spectator";
                gameMode=GameMode.SPECTATOR;
                break;
        }

        player.changeGameMode(gameMode);
        player.sendMessage(Text.literal("Updated your Game Mode to \u00A76"+name));
        return 1;
    }
}