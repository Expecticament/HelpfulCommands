package com.thatsnotm3.helpfulcommands.command.teleportation;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.thatsnotm3.helpfulcommands.HelpfulCommands;
import com.thatsnotm3.helpfulcommands.command.IHelpfulCommandsCommand;
import com.thatsnotm3.helpfulcommands.command.util.ModCommandManager;
import com.thatsnotm3.helpfulcommands.util.IEntityDataSaver;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CMD_home implements IHelpfulCommandsCommand {
    public static ModCommandManager.ModCommand cmd;

    public static void init(ModCommandManager.ModCommand newData){
        cmd=newData;
    }

    public static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment){
        dispatcher.register(CommandManager.literal(cmd.name)
                        .then(CommandManager.literal("set")
                                .executes(CMD_home::set)
                        )
                        .then(CommandManager.literal("get")
                                .executes(CMD_home::get)
                        )
                        .then(CommandManager.literal("tp")
                                .executes(CMD_home::teleport)
                        )
                .executes(CMD_home::teleport)
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

        int[] pos=playerData.getPersistentData().getIntArray("homePosition");
        String dimensionName=playerData.getPersistentData().getString("homeDimension");
        if(pos.length!=0){
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
            plr.teleport(dimension,pos[0],pos[1],pos[2],plr.getYaw(),plr.getPitch());
            src.sendFeedback(()-> Text.translatable("commands.home.teleport.success").setStyle(HelpfulCommands.style.secondary
                    .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,"/tp "+pos[0]+" "+pos[1]+" "+pos[2]))
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.literal("x: "+pos[0]+"\ny: "+pos[1]+"\nz: "+pos[2])))
            ),true);
        } else{
            src.sendError(Text.translatable("commands.home.error.noHomePos",Text.literal("/home set").setStyle(HelpfulCommands.style.primary
                    .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,"/home set"))
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.translatable("tooltips.clickToSuggestThisCommand")))
            )).setStyle(HelpfulCommands.style.error));
            return -1;
        }

        return Command.SINGLE_SUCCESS;
    }

    private static int set(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        ServerCommandSource src=ctx.getSource();
        if(!src.isExecutedByPlayer()){
            src.sendError(Text.translatable("error.inGameOnly"));
            return -1;
        }

        ServerPlayerEntity plr=ctx.getSource().getPlayer();
        IEntityDataSaver iEntityDataSaver=((IEntityDataSaver) plr);

        BlockPos pos=plr.getBlockPos();
        RegistryKey<World> dimensionKey=plr.getWorld().getRegistryKey();
        String dimensionName=dimensionKey.getValue().toString();
        iEntityDataSaver.getPersistentData().putIntArray("homePosition", new int[]{pos.getX(),pos.getY(),pos.getZ()});
        iEntityDataSaver.getPersistentData().putString("homeDimension", dimensionName);

        src.sendFeedback(()-> Text.translatable("commands.home.set.success").setStyle(HelpfulCommands.style.success),true);

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

        int[] pos=playerData.getPersistentData().getIntArray("homePosition");
        if(pos.length==0){
            src.sendError(Text.translatable("commands.home.error.noHomePos").setStyle(HelpfulCommands.style.error));
            return -1;
        }
        String dimensionName=playerData.getPersistentData().getString("homeDimension");
        Style valueStyle=HelpfulCommands.style.tertiary;

        MutableText msg=Text.empty().setStyle(HelpfulCommands.style.primary);
        msg.append(Text.literal("« ").formatted(Formatting.BOLD));
        msg
                .append(Text.literal(src.getName()).formatted(Formatting.BOLD))
                .append(Text.literal(" || ").setStyle(HelpfulCommands.style.secondary))
                .append(Text.translatable("homePosition.title").setStyle(HelpfulCommands.style.secondary))
                .append(Text.literal(" »").formatted(Formatting.BOLD))
                .append("\n")
                .append(Text.literal("x: ").setStyle(HelpfulCommands.style.simpleText))
                .append(Text.literal(String.valueOf(pos[0])).setStyle(valueStyle))
                .append("\n")
                .append(Text.literal("y: ").setStyle(HelpfulCommands.style.simpleText))
                .append(Text.literal(String.valueOf(pos[1])).setStyle(valueStyle))
                .append("\n")
                .append(Text.literal("z: ").setStyle(HelpfulCommands.style.simpleText))
                .append(Text.literal(String.valueOf(pos[2])).setStyle(valueStyle))
                .append("\n")
                .append(Text.translatable("phrase.dimension").append(Text.literal(": ")).setStyle(HelpfulCommands.style.simpleText))
                .append(Text.literal(dimensionName).setStyle(valueStyle))
                .append("\n")
                .append("\n")
                .append(Text.literal("〚").setStyle(HelpfulCommands.style.secondary))
                .append(Text.translatable("phrase.teleport").setStyle(HelpfulCommands.style.secondary
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/home"))
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.translatable("tooltips.clickToTeleport")))
                ))
                .append(Text.literal("〛").setStyle(HelpfulCommands.style.secondary))
        ;

        src.sendMessage(msg);

        return Command.SINGLE_SUCCESS;
    }
}