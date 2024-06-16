package com.expecticament.helpfulcommands.command.utility;

import com.expecticament.helpfulcommands.command.IHelpfulCommandsCommand;
import com.expecticament.helpfulcommands.command.ModCommandManager;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.expecticament.helpfulcommands.HelpfulCommands;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class CMD_rename implements IHelpfulCommandsCommand {

    public static ModCommandManager.ModCommand cmd;

    public static void init(ModCommandManager.ModCommand newData){
        cmd=newData;
    }

    public static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment){
        dispatcher.register(CommandManager.literal(cmd.name)
                .then(CommandManager.argument("newName", StringArgumentType.string())
                        .executes(ctx->execute(ctx,StringArgumentType.getString(ctx,"newName")))
                )
                .executes(CMD_rename::execute)
                .requires(src->ModCommandManager.canUseCommand(src,cmd))
        );
    }

    private static int execute(CommandContext<ServerCommandSource> ctx, String newName) throws CommandSyntaxException{
        if(!ModCommandManager.checkBeforeExecuting(ctx,cmd)) return -1;

        ServerCommandSource source=ctx.getSource();
        if(!source.isExecutedByPlayer()){
            source.sendError(Text.translatable("error.inGameOnly"));
            return -1;
        } else{
            ServerPlayerEntity player=source.getPlayer();
            if(player==null) return -1;

            ItemStack itemStack=player.getMainHandStack();
            if(itemStack==null || itemStack.isEmpty()){
                source.sendError(Text.translatable("error.nothingInMainHand"));
                return -1;
            }

            if(newName.isEmpty()){
                itemStack.set(DataComponentTypes.CUSTOM_NAME,null);
                source.sendFeedback(()-> Text.translatable("commands.rename.success.customNameRemoved").setStyle(HelpfulCommands.style.success),true);
            } else{
                MutableText oldName=((MutableText) itemStack.getName()).setStyle(HelpfulCommands.style.primary);
                if(oldName.getString().equals(newName)){
                    source.sendError(Text.translatable("commands.rename.error.sameName"));
                    return -1;
                }
                itemStack.set(DataComponentTypes.CUSTOM_NAME,Text.literal(newName));
                Text newNameText=Text.literal(newName).setStyle(HelpfulCommands.style.primary);
                source.sendFeedback(()-> Text.translatable("commands.rename.success",oldName,newNameText).setStyle(HelpfulCommands.style.success),true);
            }
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int execute(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException{
        if(!ModCommandManager.checkBeforeExecuting(ctx,cmd)) return -1;

        ServerCommandSource source=ctx.getSource();
        if(!source.isExecutedByPlayer()){
            source.sendError(Text.translatable("error.inGameOnly"));
            return -1;
        } else{
            ServerPlayerEntity player=source.getPlayer();
            if(player==null) return -1;

            ItemStack itemStack=player.getMainHandStack();
            if(itemStack==null || itemStack.isEmpty()){
                source.sendError(Text.translatable("error.nothingInMainHand"));
                return -1;
            }

            if(itemStack.get(DataComponentTypes.CUSTOM_NAME)==null){
                source.sendError(Text.translatable("commands.rename.error.customNameNotSet"));
                return -1;
            }

            itemStack.set(DataComponentTypes.CUSTOM_NAME,null);
            source.sendFeedback(()-> Text.translatable("commands.rename.success.customNameRemoved").setStyle(HelpfulCommands.style.success),true);
        }
        return Command.SINGLE_SUCCESS;
    }
}
