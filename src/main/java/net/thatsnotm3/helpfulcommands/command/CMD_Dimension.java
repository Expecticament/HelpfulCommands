package net.thatsnotm3.helpfulcommands.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.server.command.CommandManager;

public class CMD_Dimension{
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment){
        dispatcher.register(CommandManager.literal("dimension").executes(CMD_Dimension::getCurrentDimension)
            .then(CommandManager.literal("overworld").executes(ctx->switchDimension(ctx,0)))
            .then(CommandManager.literal("nether").executes(ctx->switchDimension(ctx,1)))
            .then(CommandManager.literal("end").executes(ctx->switchDimension(ctx,2)))
        );
    }

    public static int switchDimension(CommandContext<ServerCommandSource> ctx, Integer dimension) throws CommandSyntaxException{ // 0 - Overworld; 1 - The Nether; 2 - The End
        ServerPlayerEntity player=ctx.getSource().getPlayer();

        if(!ModCommandManager.RunChecks("dimension",player)) return -1;
        
        String dimensionName;
        ServerWorld world;
        switch(dimension){
            default:
                dimensionName="Overworld";
                world=player.getServer().getWorld(ServerWorld.OVERWORLD);
                break;
            case 1:
                dimensionName="The Nether";
                world=player.getServer().getWorld(ServerWorld.NETHER);
                break;
            case 2:
                dimensionName="The End";
                world=player.getServer().getWorld(ServerWorld.END);
                break;
        }

        player.teleport(world, player.getX(), player.getY(), player.getZ(), player.getYaw(), player.getPitch());
        player.sendMessage(Text.literal("Switched your Dimension to: "+"\u00A76"+dimensionName));

        return 1;
    }

    public static int getCurrentDimension(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException{
        ServerPlayerEntity player=ctx.getSource().getPlayer();

        if(!ModCommandManager.RunChecks("dimension",player)) return -1;

        RegistryKey<World> currentDimension=player.getWorld().getRegistryKey();
        String dimensionName=currentDimension.getValue().toString();
        player.sendMessage(Text.literal("Your current Dimension is: "+"\u00A76"+dimensionName));

        return 1;
    }
}