package net.thatsnotm3.helpfulcommands.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class CMD_Jump{
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment){
        dispatcher.register(CommandManager.literal("jump")
            .then(CommandManager.argument("distance", FloatArgumentType.floatArg()).executes(ctx -> run(ctx, FloatArgumentType.getFloat(ctx, "distance"))))
        );
    }

    public static int run(CommandContext<ServerCommandSource> ctx,float distance) throws CommandSyntaxException{
        ServerPlayerEntity player=ctx.getSource().getPlayer();

        if(!ModCommandManager.RunChecks("jump",player)) return -1;

        double posX=player.getX()+player.getRotationVector().getX()*distance;
        double posY=player.getY()+player.getEyeHeight(player.getPose())+player.getRotationVector().getY()*distance;
        double posZ=player.getZ()+player.getRotationVector().getZ()*distance;
        player.teleport(posX,posY,posZ);
        player.sendMessage(Text.literal("Teleported to cursor position"),true);

        return 1;
    }
}