package com.thatsnotm3.helpfulcommands.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.thatsnotm3.helpfulcommands.HelpfulCommands;
import com.thatsnotm3.helpfulcommands.command.util.ModCommandManager;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.GameMode;
import net.minecraft.world.GameRules;

import java.util.ArrayList;
import java.util.Collection;

public class CMD_gm implements IHelpfulCommandsCommand{

    public static ModCommandManager.hcCommand cmd;

    public static void init(ModCommandManager.hcCommand newData){
        cmd=newData;
    }

    public static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment){
        dispatcher.register(CommandManager.literal(cmd.name)
                .then(CommandManager.literal("a")
                        .then(CommandManager.argument("player(s)", EntityArgumentType.players())
                                .executes(ctx->execute(ctx,GameMode.ADVENTURE,EntityArgumentType.getPlayers(ctx,"player(s)")))
                        )
                        .executes(ctx->{
                            if(!ctx.getSource().isExecutedByPlayer()){
                                ctx.getSource().sendMessage(Text.translatable("error.specifyTargets").setStyle(HelpfulCommands.style.error));
                                return -1;
                            }
                            execute(ctx,GameMode.ADVENTURE,new ArrayList<>(){{ add(ctx.getSource().getPlayer()); }});
                            return Command.SINGLE_SUCCESS;
                        })
                )
                .then(CommandManager.literal("c")
                        .then(CommandManager.argument("player(s)", EntityArgumentType.players())
                                .executes(ctx->execute(ctx,GameMode.CREATIVE,EntityArgumentType.getPlayers(ctx,"player(s)")))
                        )
                        .executes(ctx->{
                            if(!ctx.getSource().isExecutedByPlayer()){
                                ctx.getSource().sendMessage(Text.translatable("error.specifyTargets").setStyle(HelpfulCommands.style.error));
                                return -1;
                            }
                            execute(ctx,GameMode.CREATIVE,new ArrayList<>(){{ add(ctx.getSource().getPlayer()); }});
                            return Command.SINGLE_SUCCESS;
                        })
                )
                .then(CommandManager.literal("s")
                        .then(CommandManager.argument("player(s)", EntityArgumentType.players())
                                .executes(ctx->execute(ctx,GameMode.SURVIVAL,EntityArgumentType.getPlayers(ctx,"player(s)")))
                        )
                        .executes(ctx->{
                            if(!ctx.getSource().isExecutedByPlayer()){
                                ctx.getSource().sendMessage(Text.translatable("error.specifyTargets").setStyle(HelpfulCommands.style.error));
                                return -1;
                            }
                            execute(ctx,GameMode.SURVIVAL,new ArrayList<>(){{ add(ctx.getSource().getPlayer()); }});
                            return Command.SINGLE_SUCCESS;
                        })
                )
                .then(CommandManager.literal("sp")
                        .then(CommandManager.argument("player(s)", EntityArgumentType.players())
                                .executes(ctx->execute(ctx,GameMode.SPECTATOR,EntityArgumentType.getPlayers(ctx,"player(s)")))
                        )
                        .executes(ctx->{
                            if(!ctx.getSource().isExecutedByPlayer()){
                                ctx.getSource().sendMessage(Text.translatable("error.specifyTargets").setStyle(HelpfulCommands.style.error));
                                return -1;
                            }
                            execute(ctx,GameMode.SPECTATOR,new ArrayList<>(){{ add(ctx.getSource().getPlayer()); }});
                            return Command.SINGLE_SUCCESS;
                        })
                )
                .requires(Permissions.require(HelpfulCommands.modID+".command."+cmd.category.toString().toLowerCase()+"."+cmd.name,cmd.defaultRequiredLevel))
        );
    }

    private static int execute(CommandContext<ServerCommandSource> ctx, GameMode gameMode, Collection<ServerPlayerEntity> targets) throws CommandSyntaxException{
        for(ServerPlayerEntity serverPlayerEntity : targets){
            if(!serverPlayerEntity.changeGameMode(gameMode)) continue;
            sendFeedback(ctx.getSource(), serverPlayerEntity, gameMode);
        }
        return Command.SINGLE_SUCCESS;
    }

    private static void sendFeedback(ServerCommandSource source, ServerPlayerEntity player, GameMode gameMode){
        MutableText gmName=Text.translatable("gameMode."+gameMode.getName()).setStyle(HelpfulCommands.style.primary);
        if(source.getEntity()==player){
            source.sendFeedback(()->Text.translatable("commands.gamemode.success.self", gmName).setStyle(HelpfulCommands.style.success), true);
        } else {
            if(source.getWorld().getGameRules().getBoolean(GameRules.SEND_COMMAND_FEEDBACK)){
                player.sendMessage(Text.translatable("gameMode.changed", gmName).setStyle(HelpfulCommands.style.tertiary));
            }
            source.sendFeedback(() -> Text.translatable("commands.gamemode.success.other", player.getDisplayName(), gmName), true);
        }
    }
}