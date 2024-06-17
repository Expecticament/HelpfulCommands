package com.expecticament.helpfulcommands.command.utility;

import com.expecticament.helpfulcommands.HelpfulCommands;
import com.expecticament.helpfulcommands.command.IHelpfulCommandsCommand;
import com.expecticament.helpfulcommands.command.ModCommandManager;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;

import java.util.Collection;

public class CMD_repair implements IHelpfulCommandsCommand {

    public static ModCommandManager.ModCommand cmd;

    public static void init(ModCommandManager.ModCommand newData){
        cmd=newData;
    }

    public static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment){
        dispatcher.register(CommandManager.literal(cmd.name)
                .then(CommandManager.argument("target(s)", EntityArgumentType.players())
                        .executes(ctx->execute(ctx,EntityArgumentType.getPlayers(ctx,"target(s)")))
                )
                .executes(CMD_repair::execute)
                .requires(src->ModCommandManager.canUseCommand(src,cmd))
        );
    }

    private static int execute(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException{
        ServerCommandSource source=ctx.getSource();
        if(!source.isExecutedByPlayer()){
            source.sendError(Text.translatable("error.specifyTargets"));
            return -1;
        }

        ServerPlayerEntity player=source.getPlayer();
        if(player==null) return -1;

        ItemStack itemStack=player.getMainHandStack();
        if(itemStack==null || itemStack.isEmpty()){
            source.sendError(Text.translatable("error.nothingInMainHand"));
            return -1;
        }
        if(!itemStack.isDamageable()){
            source.sendError(Text.translatable("commands.repair.error.notDamageable"));
            return -1;
        }
        if(itemStack.getDamage()==0){
            source.sendError(Text.translatable("commands.repair.error.notDamaged"));
            return -1;
        }

        itemStack.set(DataComponentTypes.DAMAGE,itemStack.getMaxUseTime(player));
        source.sendFeedback(()-> Text.translatable("commands.repair.success.self", itemStack.getName().copy().setStyle(HelpfulCommands.style.primary)).setStyle(HelpfulCommands.style.success),true);

        return Command.SINGLE_SUCCESS;
    }

    private static int execute(CommandContext<ServerCommandSource> ctx, Collection<? extends ServerPlayerEntity> targets) throws CommandSyntaxException{
        ServerCommandSource source=ctx.getSource();

        int count=0;
        String s="";
        for(ServerPlayerEntity i : targets){
            ItemStack itemStack=i.getMainHandStack();
            if(itemStack==null || itemStack.isEmpty() || !itemStack.isDamageable() || itemStack.getDamage()==0){
                continue;
            }
            itemStack.set(DataComponentTypes.DAMAGE,itemStack.getMaxUseTime(i));
            s=s+i.getName().getString()+" - "+itemStack.getName().getString()+"\n";
            count++;
        }
        if(count>0) s=s.substring(0, s.length()-1);

        Text hoverText=Text.literal(s);

        if(count<=0){
            source.sendError(Text.translatable("error.didntFindTargets"));
        } else {
            int finalCount=count;
            source.sendFeedback(() -> Text.translatable("commands.repair.success.other", Text.literal(String.valueOf(finalCount)).setStyle(HelpfulCommands.style.primary.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText)))).setStyle(HelpfulCommands.style.success), true);
        }

        return Command.SINGLE_SUCCESS;
    }
}
