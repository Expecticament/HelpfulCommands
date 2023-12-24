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
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.*;

import com.thatsnotm3.helpfulcommands.util.IEntityDataSaver;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

public class CMD_back implements IHelpfulCommandsCommand {
    public static ModCommandManager.hcCommand cmd;

    public static void init(ModCommandManager.hcCommand newData){
        cmd=newData;
    }

    public static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment){
        dispatcher.register(CommandManager.literal(cmd.name)
                        .then(CommandManager.literal("get")
                                .executes(CMD_back::get)
                        )
                        .then(CommandManager.literal("tp")
                                .executes(CMD_back::teleport)
                        )
                .executes(CMD_back::teleport)
                .requires(Permissions.require(HelpfulCommands.modID+".command."+cmd.category.toString().toLowerCase()+"."+cmd.name,cmd.defaultRequiredLevel))
        );
    }

    private static int teleport(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        ServerCommandSource src=ctx.getSource();
        if(!src.isExecutedByPlayer()){
            src.sendError(Text.translatable("error.inGameOnly"));
            return -1;
        }

        ServerPlayerEntity plr=src.getPlayer();
        IEntityDataSaver playerData=(IEntityDataSaver) plr;

        int[] deathPos=playerData.getPersistentData().getIntArray("deathPosition");
        String dimensionName=playerData.getPersistentData().getString("deathDimension");
        if(deathPos.length!=0){
            ServerWorld dimension=null;
            for(RegistryKey<World> i : src.getWorldKeys()){
                if(i.getValue().toString().equals(dimensionName)){
                    dimension=src.getServer().getWorld(i);
                    break;
                }
            }
            if(dimension==null){
                plr.sendMessage(Text.translatable("error.unknownDimension").setStyle(HelpfulCommands.style.error));
                return -1;
            }
            plr.teleport(dimension,deathPos[0],deathPos[1],deathPos[2],plr.getYaw(),plr.getPitch());
            src.sendFeedback(()-> Text.translatable("commands.back.success").setStyle(HelpfulCommands.style.secondary
                    .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,"/tp "+deathPos[0]+" "+deathPos[1]+" "+deathPos[2]))
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.literal("x: "+deathPos[0]+"\ny: "+deathPos[1]+"\nz: "+deathPos[2])))
            ),true);
        } else{
            src.sendError(Text.translatable("commands.back.error.noDeathPos").setStyle(HelpfulCommands.style.error));
            return -1;
        }

        return Command.SINGLE_SUCCESS;
    }

    private static int get(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        ServerCommandSource src=ctx.getSource();
        if(!src.isExecutedByPlayer()){
            src.sendError(Text.translatable("error.inGameOnly"));
            return -1;
        }

        ServerPlayerEntity plr=src.getPlayer();
        IEntityDataSaver playerData=(IEntityDataSaver) plr;

        int[] deathPos=playerData.getPersistentData().getIntArray("deathPosition");
        String dimensionName=playerData.getPersistentData().getString("deathDimension");
        Style valueStyle=HelpfulCommands.style.tertiary;

        MutableText msg=Text.empty().setStyle(HelpfulCommands.style.primary);
        msg.append(Text.literal("« ").formatted(Formatting.BOLD));
        msg
                .append(Text.literal(src.getName()).formatted(Formatting.BOLD))
                .append(Text.literal(" || ").setStyle(HelpfulCommands.style.secondary))
                .append(Text.translatable("deathPosition.title").setStyle(HelpfulCommands.style.secondary))
                .append(Text.literal(" »").formatted(Formatting.BOLD))
                .append("\n")
                .append(Text.literal("x: ").setStyle(HelpfulCommands.style.simpleText))
                .append(Text.literal(String.valueOf(deathPos[0])).setStyle(valueStyle))
                .append("\n")
                .append(Text.literal("y: ").setStyle(HelpfulCommands.style.simpleText))
                .append(Text.literal(String.valueOf(deathPos[1])).setStyle(valueStyle))
                .append("\n")
                .append(Text.literal("z: ").setStyle(HelpfulCommands.style.simpleText))
                .append(Text.literal(String.valueOf(deathPos[2])).setStyle(valueStyle))
                .append("\n")
                .append(Text.translatable("phrase.dimension").append(Text.literal(": ")).setStyle(HelpfulCommands.style.simpleText))
                .append(Text.literal(dimensionName).setStyle(valueStyle))
                .append("\n")
                .append("\n")
                .append(Text.literal("〚").setStyle(HelpfulCommands.style.secondary))
                .append(Text.translatable("phrase.teleport").setStyle(HelpfulCommands.style.secondary
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/back"))
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.translatable("tooltips.clickToTeleport")))
                ))
                .append(Text.literal("〛").setStyle(HelpfulCommands.style.secondary))
        ;

        src.sendMessage(msg);

        return Command.SINGLE_SUCCESS;
    }
}