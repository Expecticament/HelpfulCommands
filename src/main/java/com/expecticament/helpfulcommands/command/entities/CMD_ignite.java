package com.expecticament.helpfulcommands.command.entities;

import com.expecticament.helpfulcommands.command.IHelpfulCommandsCommand;
import com.expecticament.helpfulcommands.command.ModCommandManager;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.expecticament.helpfulcommands.HelpfulCommands;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
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

public class CMD_ignite implements IHelpfulCommandsCommand {

    public static ModCommandManager.ModCommand cmd;

    public static void init(ModCommandManager.ModCommand newData){
        cmd=newData;
    }

    public static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment){
        dispatcher.register(CommandManager.literal(cmd.name)
                .then(CommandManager.argument("target(s)",EntityArgumentType.entities())
                        .then(CommandManager.argument("duration", IntegerArgumentType.integer(0))
                                .executes(ctx->execute(ctx,EntityArgumentType.getEntities(ctx,"target(s)"), IntegerArgumentType.getInteger(ctx,"duration")))
                        )
                )
                .requires(src->ModCommandManager.canUseCommand(src,cmd))
        );
    }

    private static int execute(CommandContext<ServerCommandSource> ctx, Collection<? extends Entity> targets, int duration) throws CommandSyntaxException{
        if(!ModCommandManager.checkBeforeExecuting(ctx,cmd)) return -1;

        ServerCommandSource src=ctx.getSource();

        Map<String, Integer> entries=new HashMap<>(ignite(src, targets, duration));

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
            src.sendFeedback(() -> Text.translatable("commands.ignite.success.other", finalCount, Text.literal(String.valueOf(duration)).setStyle(HelpfulCommands.style.primary)).setStyle(HelpfulCommands.style.success), true);
        } else{
            src.sendError(Text.translatable("error.didntFindTargets").setStyle(HelpfulCommands.style.error));
        }

        return Command.SINGLE_SUCCESS;
    }
    private static Map<String,Integer> ignite(ServerCommandSource src, Collection<? extends Entity> targets, int duration){
        Map<String, Integer> entries=new HashMap<>();
        boolean feedback=src.getWorld().getGameRules().getBoolean(GameRules.SEND_COMMAND_FEEDBACK);

        for(Entity i : targets){
            if(i.isPlayer()){
                ServerPlayerEntity plr=(ServerPlayerEntity) i;
                if(plr.getAbilities().invulnerable) continue;
            }
            i.setOnFireFor(duration);
            int diff=1;
            if(i.isPlayer()){
                diff=-1;
                if(feedback){
                    if(src.getEntity()!=i) i.sendMessage(Text.translatable("commands.ignite.success.self").setStyle(HelpfulCommands.style.tertiary));
                }
            }
            String name=i.getName().getString();
            entries.put(name,entries.getOrDefault(name,0)+diff);
        }

        return entries;
    }
}