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
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.GameRules;

import java.util.ArrayList;
import java.util.Collection;

public class CMD_heal implements IHelpfulCommandsCommand{

    public static ModCommandManager.hcCommand cmd;

    public static void init(ModCommandManager.hcCommand newData){
        cmd=newData;
    }

    public static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment){
        dispatcher.register(CommandManager.literal(cmd.name)
                .then(CommandManager.argument("target(s)",EntityArgumentType.players())
                        .executes(ctx->execute(ctx,EntityArgumentType.getPlayers(ctx,"target(s)")))
                )
                .executes(ctx->{
                    if(!ctx.getSource().isExecutedByPlayer()){
                        ctx.getSource().sendMessage(Text.translatable("error.specifyTargets").setStyle(HelpfulCommands.style.error));
                        return -1;
                    }
                    execute(ctx,new ArrayList<>(){{ add(ctx.getSource().getPlayer()); }});
                    return Command.SINGLE_SUCCESS;
                })
                .requires(Permissions.require(HelpfulCommands.modID+".command."+cmd.category.toString().toLowerCase()+"."+cmd.name,cmd.defaultRequiredLevel))
        );
    }

    private static int execute(CommandContext<ServerCommandSource> ctx, Collection<ServerPlayerEntity> targets) throws CommandSyntaxException{
        for(ServerPlayerEntity i : targets){
            if(i.getHealth()==i.getMaxHealth()) continue;
            i.setHealth(i.getMaxHealth());

            sendFeedback(ctx.getSource(), i);
        }
        return Command.SINGLE_SUCCESS;
    }

    private static void sendFeedback(ServerCommandSource source, Entity target){
        if(source.getEntity()==target){
            source.sendFeedback(()->Text.translatable("commands.heal.success.self").setStyle(HelpfulCommands.style.success), true);
        } else {
            if(source.getWorld().getGameRules().getBoolean(GameRules.SEND_COMMAND_FEEDBACK)){
                target.sendMessage(Text.translatable("commands.heal.success.self").setStyle(HelpfulCommands.style.success));
            }
            Text name=target.getDisplayName();
            source.sendFeedback(() -> Text.translatable("commands.heal.success.other", name).setStyle(HelpfulCommands.style.inactive), true);
        }
    }
}