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

public class CMD_Spawn{

    static final String cmdName="spawn";

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment){
        dispatcher.register(CommandManager.literal(cmdName)
            .then(CommandManager.literal("world")
                .then(CommandManager.literal("get")
                    .executes(CMD_Spawn::worldGet)
                )
                .then(CommandManager.literal("tp")
                    .executes(CMD_Spawn::worldTeleport)
                )
                .executes(CMD_Spawn::worldTeleport)
            )
            .then(CommandManager.literal("player")
                .then(CommandManager.literal("get")
                    .executes(CMD_Spawn::playerGet)
                )
                .then(CommandManager.literal("tp")
                    .executes(CMD_Spawn::playerTeleport)
                )
                .executes(CMD_Spawn::playerTeleport)
            )
        );
    }

    public static int playerTeleport(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException{
        ServerPlayerEntity player=ctx.getSource().getPlayer();

        if(!ModCommandManager.RunChecks(cmdName,player)) return -1;
        
        if(player.getSpawnPointPosition()==null){
            player.sendMessage(Text.literal("\u00A7cSpawn Point position is not set in this world yet!"));
            return 1;
        }

        ServerWorld targetWorld=player.getServer().getWorld(player.getSpawnPointDimension());
        player.teleport(targetWorld, player.getSpawnPointPosition().getX(), player.getSpawnPointPosition().getY(), player.getSpawnPointPosition().getZ(), player.getYaw(), player.getPitch());
        player.sendMessage(Text.literal("Teleported you to \u00A7byour Spawnpoint"));

        return 1;
    }
    public static int worldTeleport(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException{
        ServerPlayerEntity player=ctx.getSource().getPlayer();

        if(!ModCommandManager.RunChecks("spawn",player)) return -1;
        
        ServerWorld overworld=player.getServer().getWorld(ServerWorld.OVERWORLD);
        player.teleport(overworld, player.getWorld().getSpawnPos().getX(), player.getWorld().getSpawnPos().getY(), player.getWorld().getSpawnPos().getZ(), player.getYaw(), player.getPitch());
        player.sendMessage(Text.literal("Teleported you to \u00A7bWorld Spawnpoint"));

        return 1;
    }
    public static int playerGet(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException{
        ServerPlayerEntity player=ctx.getSource().getPlayer();

        if(!ModCommandManager.RunChecks("spawn",player)) return -1;

        if(player.getSpawnPointPosition()==null){
            player.sendMessage(Text.literal("\u00A7cSpawn Point position is not set in this world yet!"));
            return 1;
        }
        RegistryKey<World> spawnDimensionKey=player.getSpawnPointDimension();
        String dimensionName=spawnDimensionKey.getValue().toString();
        player.sendMessage(Text.literal("\u00A7bYour Spawnpoint information:\u00A7r\nX: \u00A76"+player.getSpawnPointPosition().getX()+"\u00A7r\nY: \u00A76"+player.getSpawnPointPosition().getY()+"\u00A7r\nZ: \u00A76"+player.getSpawnPointPosition().getZ()+"\u00A7r\nDimension: \u00A76"+dimensionName));

        return 1;
    }
    public static int worldGet(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException{
        ServerPlayerEntity player=ctx.getSource().getPlayer();

        if(!ModCommandManager.RunChecks("spawn",player)) return -1;

        player.sendMessage(Text.literal("\u00A7bWorld Spawnpoint information:\u00A7r\nX: \u00A76"+player.getWorld().getSpawnPos().getX()+"\u00A7r\nY: \u00A76"+player.getWorld().getSpawnPos().getY()+"\u00A7r\nZ: \u00A76"+player.getWorld().getSpawnPos().getZ()));

        return 1;
    }
}