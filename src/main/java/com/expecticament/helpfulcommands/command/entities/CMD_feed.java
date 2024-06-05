package com.expecticament.helpfulcommands.command.entities;

import com.expecticament.helpfulcommands.command.IHelpfulCommandsCommand;
import com.expecticament.helpfulcommands.command.ModCommandManager;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.expecticament.helpfulcommands.HelpfulCommands;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.world.GameRules;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CMD_feed implements IHelpfulCommandsCommand {

    public static ModCommandManager.ModCommand cmd;

    public static void init(ModCommandManager.ModCommand newData){
        cmd=newData;
    }

    public static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment){
        dispatcher.register(CommandManager.literal(cmd.name)
                .then(CommandManager.argument("target(s)",EntityArgumentType.players())
                        .executes(ctx->execute(ctx,EntityArgumentType.getPlayers(ctx,"target(s)")))
                )
                .executes(CMD_feed::execute)
                .requires(Permissions.require(HelpfulCommands.modID+".command."+cmd.category.toString().toLowerCase()+"."+cmd.name,cmd.defaultRequiredLevel))
        );
    }

    private static int execute(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException{
        if(!ModCommandManager.checkBeforeExecuting(ctx,cmd)) return -1;

        ServerCommandSource src=ctx.getSource();

        if(!src.isExecutedByPlayer()){
            src.sendError(Text.translatable("error.specifyTargets").setStyle(HelpfulCommands.style.error));
            return -1;
        }

        ServerPlayerEntity plr=src.getPlayer();
        HungerManager hm=plr.getHungerManager();
        if(!hm.isNotFull()) return Command.SINGLE_SUCCESS;

        hm.setFoodLevel(20);
        hm.setSaturationLevel(5);
        hm.setExhaustion(0);

        src.sendFeedback(()->Text.translatable("commands.feed.success.self").setStyle(HelpfulCommands.style.success),true);

        return Command.SINGLE_SUCCESS;
    }

    private static int execute(CommandContext<ServerCommandSource> ctx, Collection<? extends ServerPlayerEntity> targets) throws CommandSyntaxException{
        if(!ModCommandManager.checkBeforeExecuting(ctx,cmd)) return -1;

        ServerCommandSource src=ctx.getSource();

        Map<String, Integer> entries=new HashMap<>(feed(src, targets));

        int count=0;
        String entryList="";
        for(Map.Entry<String, Integer> i : entries.entrySet()){
            if(i.getValue()<0){
                entryList+=i.getKey()+"\n";
                count+=1;
            }
            else{
                entryList+=i.getValue()+"x "+i.getKey()+"\n";
                count+=i.getValue();
            }
        }
        if(!entryList.isEmpty()) entryList=entryList.substring(0, entryList.length()-1);

        if(count>0) {
            MutableText finalCount=Text.literal(String.valueOf(count)).setStyle(HelpfulCommands.style.primary
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.literal(entryList)))
            );
            src.sendFeedback(() -> Text.translatable("commands.feed.success.other", finalCount).setStyle(HelpfulCommands.style.success), true);
        } else{
            src.sendError(Text.translatable("error.didntFindTargets").setStyle(HelpfulCommands.style.error));
        }

        return Command.SINGLE_SUCCESS;
    }

    private static Map<String,Integer> feed(ServerCommandSource src, Collection<? extends ServerPlayerEntity> targets){
        Map<String, Integer> entries=new HashMap<>();
        boolean feedback=src.getWorld().getGameRules().getBoolean(GameRules.SEND_COMMAND_FEEDBACK);

        for(ServerPlayerEntity i : targets){
            HungerManager hm=i.getHungerManager();
            if(!hm.isNotFull()) continue;
            hm.setFoodLevel(20);
            hm.setSaturationLevel(5);
            hm.setExhaustion(0);

            int diff=1;
            diff=-1;
            if(feedback){
                if(src.getPlayer()!=i) i.sendMessage(Text.translatable("commands.feed.success.self").setStyle(HelpfulCommands.style.success));
            }
            String name=i.getName().getString();
            entries.put(name,entries.getOrDefault(name,0)+diff);
        }

        return entries;
    }
}