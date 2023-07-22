package net.thatsnotm3.helpfulcommands.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
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
            player.sendMessage(Text.translatable("message.error.command.spawn.self.notSet").formatted(Formatting.RED));
            return -1;
        }

        ServerWorld targetWorld=player.getServer().getWorld(player.getSpawnPointDimension());
        player.teleport(targetWorld, player.getSpawnPointPosition().getX(), player.getSpawnPointPosition().getY(), player.getSpawnPointPosition().getZ(), player.getYaw(), player.getPitch());
        player.sendMessage(Text.translatable("message.command.spawn.self.teleported").setStyle(Style.EMPTY
            .withFormatting(Formatting.GREEN)
            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.literal("x: "+player.getSpawnPointPosition().getX()+"\ny: "+player.getSpawnPointPosition().getY()+"\nz: "+player.getSpawnPointPosition().getZ())))
            .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tp "+player.getSpawnPointPosition().getX()+" "+player.getSpawnPointPosition().getY()+" "+player.getSpawnPointPosition().getZ()))
        ));

        return 1;
    }
    public static int worldTeleport(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException{
        ServerPlayerEntity player=ctx.getSource().getPlayer();

        if(!ModCommandManager.RunChecks("spawn",player)) return -1;
        
        ServerWorld overworld=player.getServer().getWorld(ServerWorld.OVERWORLD);
        player.teleport(overworld, player.getWorld().getSpawnPos().getX(), player.getWorld().getSpawnPos().getY(), player.getWorld().getSpawnPos().getZ(), player.getYaw(), player.getPitch());
        player.sendMessage(Text.translatable("message.command.spawn.world.teleported").setStyle(Style.EMPTY
            .withFormatting(Formatting.GREEN)
            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.literal("x: "+player.getWorld().getSpawnPos().getX()+"\ny: "+player.getWorld().getSpawnPos().getY()+"\nz: "+player.getWorld().getSpawnPos().getZ())))
            .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tp "+player.getWorld().getSpawnPos().getX()+" "+player.getWorld().getSpawnPos().getY()+" "+player.getWorld().getSpawnPos().getZ()))
        ));

        return 1;
    }
    public static int playerGet(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException{
        ServerPlayerEntity player=ctx.getSource().getPlayer();

        if(!ModCommandManager.RunChecks("spawn",player)) return -1;

        if(player.getSpawnPointPosition()==null){
            player.sendMessage(Text.translatable("message.error.command.spawn.self.notSet").formatted(Formatting.RED));
            return -1;
        }
        RegistryKey<World> spawnDimensionKey=player.getSpawnPointDimension();
        String dimensionName=spawnDimensionKey.getValue().toString();
        Style buttonStyle=Style.EMPTY
            .withFormatting(Formatting.AQUA)
            .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/spawn player"))
            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("tooltip.highlight.spawn.self.get.teleportButton")))
        ;
        MutableText msg=Text.literal("")
            .append(Text.literal("\u00AB ").formatted(Formatting.AQUA,Formatting.BOLD))
            .append(Text.translatable("message.command.spawn.self.get.title").formatted(Formatting.AQUA,Formatting.BOLD))
            .append(Text.literal(" \u00BB").formatted(Formatting.AQUA,Formatting.BOLD))
            .append(Text.literal("\nx: "))
            .append(Text.literal(Integer.toString(player.getSpawnPointPosition().getX())).formatted(Formatting.GOLD))
            .append(Text.literal("\ny: "))
            .append(Text.literal(Integer.toString(player.getSpawnPointPosition().getY())).formatted(Formatting.GOLD))
            .append(Text.literal("\nz: "))
            .append(Text.literal(Integer.toString(player.getSpawnPointPosition().getZ())).formatted(Formatting.GOLD))
            .append(Text.literal("\n"))
            .append(Text.translatable("text.dimension",Text.literal(dimensionName).formatted(Formatting.GOLD)))
            .append(Text.literal("\n\n[").setStyle(buttonStyle))
            .append(Text.translatable("text.teleport").setStyle(buttonStyle))
            .append(Text.literal("]").setStyle(buttonStyle))
        ;
        player.sendMessage(msg);

        return 1;
    }
    public static int worldGet(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException{
        ServerPlayerEntity player=ctx.getSource().getPlayer();

        if(!ModCommandManager.RunChecks("spawn",player)) return -1;

        String dimensionName="minecraft:overworld";

        Style buttonStyle=Style.EMPTY
            .withFormatting(Formatting.AQUA)
            .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/spawn world"))
            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("tooltip.highlight.spawn.world.get.teleportButton")))
        ;
        MutableText msg=Text.literal("")
            .append(Text.literal("\u00AB ").formatted(Formatting.AQUA,Formatting.BOLD))
            .append(Text.translatable("message.command.spawn.world.get.title").formatted(Formatting.AQUA,Formatting.BOLD))
            .append(Text.literal(" \u00BB").formatted(Formatting.AQUA,Formatting.BOLD))
            .append(Text.literal("\nx: "))
            .append(Text.literal(Integer.toString(player.getWorld().getSpawnPos().getX())).formatted(Formatting.GOLD))
            .append(Text.literal("\ny: "))
            .append(Text.literal(Integer.toString(player.getWorld().getSpawnPos().getY())).formatted(Formatting.GOLD))
            .append(Text.literal("\nz: "))
            .append(Text.literal(Integer.toString(player.getWorld().getSpawnPos().getZ())).formatted(Formatting.GOLD))
            .append(Text.literal("\n"))
            .append(Text.translatable("text.dimension",Text.literal(dimensionName).formatted(Formatting.GOLD)))
            .append(Text.literal("\n\n[").setStyle(buttonStyle))
            .append(Text.translatable("text.teleport").setStyle(buttonStyle))
            .append(Text.literal("]").setStyle(buttonStyle))
        ;
        player.sendMessage(msg);

        return 1;
    }
}