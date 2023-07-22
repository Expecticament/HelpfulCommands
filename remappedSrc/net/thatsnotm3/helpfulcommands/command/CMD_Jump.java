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
import net.minecraft.util.Formatting;

public class CMD_Jump{

    static final String cmdName="jump";

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment){
        dispatcher.register(CommandManager.literal(cmdName)
            .then(CommandManager.argument("distance", FloatArgumentType.floatArg(0)).executes(ctx -> run(ctx, FloatArgumentType.getFloat(ctx, "distance"))))
        );
    }

    public static int run(CommandContext<ServerCommandSource> ctx,float distance) throws CommandSyntaxException{
        ServerPlayerEntity player=ctx.getSource().getPlayer();

        if(!ModCommandManager.RunChecks(cmdName,player)) return -1;

        double posX=player.getX()+player.getRotationVector().getX()*distance;
        double posY=player.getY()+player.getEyeHeight(player.getPose())+player.getRotationVector().getY()*distance;
        double posZ=player.getZ()+player.getRotationVector().getZ()*distance;
        player.teleport(posX,posY,posZ);

        player.sendMessage(Text.translatable("message.command.jump.success").formatted(Formatting.AQUA),true);

        return 1;
    }
}