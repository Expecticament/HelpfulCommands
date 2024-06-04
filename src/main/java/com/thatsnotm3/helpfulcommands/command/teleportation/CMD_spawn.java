package com.thatsnotm3.helpfulcommands.command.teleportation;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.thatsnotm3.helpfulcommands.HelpfulCommands;
import com.thatsnotm3.helpfulcommands.command.IHelpfulCommandsCommand;
import com.thatsnotm3.helpfulcommands.command.util.ModCommandManager;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CMD_spawn implements IHelpfulCommandsCommand {
    public static ModCommandManager.ModCommand cmd;

    public static void init(ModCommandManager.ModCommand newData){
        cmd=newData;
    }

    public static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment){
        dispatcher.register(CommandManager.literal(cmd.name)
                        .then(CommandManager.literal("player")
                                .then(CommandManager.literal("get")
                                        .executes(ctx->get(ctx,0))
                                )
                                .then(CommandManager.literal("tp")
                                        .executes(CMD_spawn::teleportPlayer)
                                )
                                .executes(CMD_spawn::teleportPlayer)
                        )
                        .then(CommandManager.literal("world")
                                .then(CommandManager.literal("get")
                                        .executes(ctx->get(ctx,1))
                                )
                                .then(CommandManager.literal("tp")
                                        .executes(CMD_spawn::teleportWorld)
                                )
                                .executes(CMD_spawn::teleportWorld)
                        )
                .requires(Permissions.require(HelpfulCommands.modID+".command."+cmd.category.toString().toLowerCase()+"."+cmd.name,cmd.defaultRequiredLevel))
        );
    }

    private static int teleportPlayer(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        ServerCommandSource src=ctx.getSource();
        if(!src.isExecutedByPlayer()){
            src.sendError(Text.translatable("error.inGameOnly"));
            return -1;
        }

        ServerPlayerEntity plr=src.getPlayer();
        ServerWorld world=src.getServer().getWorld(plr.getSpawnPointDimension());
        BlockPos pos=plr.getSpawnPointPosition();

        if(pos==null){
            src.sendError(Text.translatable("commands.spawn.player.error.notSet").setStyle(HelpfulCommands.style.error));
            return -1;
        }

        plr.teleport(world,pos.getX(),pos.getY(),pos.getZ(),plr.getYaw(),plr.getPitch());
        src.sendFeedback(()-> Text.translatable("commands.spawn.player.self.success").setStyle(HelpfulCommands.style.secondary
                .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,"/tp "+pos.getX()+" "+pos.getY()+" "+pos.getZ()))
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.literal("x: "+pos.getX()+"\ny: "+pos.getY()+"\nz: "+pos.getZ())))
        ),true);

        return Command.SINGLE_SUCCESS;
    }

    private static int teleportWorld(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        ServerCommandSource src=ctx.getSource();
        if(!src.isExecutedByPlayer()){
            src.sendError(Text.translatable("error.inGameOnly"));
            return -1;
        }

        ServerPlayerEntity plr=src.getPlayer();
        ServerWorld world=src.getServer().getWorld(World.OVERWORLD);
        BlockPos pos=world.getSpawnPos();

        plr.teleport(world,pos.getX(),pos.getY(),pos.getZ(),plr.getYaw(),plr.getPitch());
        src.sendFeedback(()-> Text.translatable("commands.spawn.world.success").setStyle(HelpfulCommands.style.secondary
                .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,"/tp "+pos.getX()+" "+pos.getY()+" "+pos.getZ()))
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.literal("x: "+pos.getX()+"\ny: "+pos.getY()+"\nz: "+pos.getZ())))
        ),true);

        return Command.SINGLE_SUCCESS;
    }

    private static int get(CommandContext<ServerCommandSource> ctx,int n) throws CommandSyntaxException { // 0 - player, 1 - world
        ServerCommandSource src=ctx.getSource();
        if(!src.isExecutedByPlayer()){
            src.sendError(Text.translatable("error.inGameOnly"));
            return -1;
        }

        ServerPlayerEntity plr=src.getPlayer();

        ServerWorld world=null;
        BlockPos pos=null;
        MutableText targetTitle=Text.empty();
        String runCmd="";
        switch(n){
            case 0:
                world=src.getServer().getWorld(plr.getSpawnPointDimension());
                pos=plr.getSpawnPointPosition();
                targetTitle=Text.literal(src.getName());
                runCmd="player";
                break;
            case 1:
                world=src.getServer().getOverworld();
                pos=world.getSpawnPos();
                targetTitle=Text.translatable("phrase.world");
                runCmd="world";
                break;
        }

        if(world==null || pos==null){
            src.sendError(Text.translatable("commands.spawn.player.error.notSet").setStyle(HelpfulCommands.style.error));
            return -1;
        }

        Style valueStyle=HelpfulCommands.style.tertiary;

        MutableText msg=Text.empty().setStyle(HelpfulCommands.style.primary);
        msg.append(Text.literal("« ").formatted(Formatting.BOLD));
        msg
                .append(targetTitle.formatted(Formatting.BOLD))
                .append(Text.literal(" || ").setStyle(HelpfulCommands.style.secondary))
                .append(Text.translatable("spawnPoint.title").setStyle(HelpfulCommands.style.secondary))
                .append(Text.literal(" »").formatted(Formatting.BOLD))
                .append("\n")
                .append(Text.literal("x: ").setStyle(HelpfulCommands.style.simpleText))
                .append(Text.literal(String.valueOf(pos.getX())).setStyle(valueStyle))
                .append("\n")
                .append(Text.literal("y: ").setStyle(HelpfulCommands.style.simpleText))
                .append(Text.literal(String.valueOf(pos.getY())).setStyle(valueStyle))
                .append("\n")
                .append(Text.literal("z: ").setStyle(HelpfulCommands.style.simpleText))
                .append(Text.literal(String.valueOf(pos.getZ())).setStyle(valueStyle))
                .append("\n")
                .append(Text.translatable("phrase.dimension").append(Text.literal(": ")).setStyle(HelpfulCommands.style.simpleText))
                .append(Text.literal(world.getRegistryKey().getValue().toString()).setStyle(valueStyle))
                .append("\n")
                .append("\n")
                .append(Text.literal("〚").setStyle(HelpfulCommands.style.secondary))
                .append(Text.translatable("phrase.teleport").setStyle(HelpfulCommands.style.secondary
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/spawn "+runCmd))
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.translatable("tooltips.clickToTeleport")))
                ))
                .append(Text.literal("〛").setStyle(HelpfulCommands.style.secondary))
        ;

        src.sendMessage(msg);

        return Command.SINGLE_SUCCESS;
    }
}