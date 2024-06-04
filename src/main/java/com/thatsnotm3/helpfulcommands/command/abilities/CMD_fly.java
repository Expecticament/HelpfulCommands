package com.thatsnotm3.helpfulcommands.command.abilities;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.thatsnotm3.helpfulcommands.HelpfulCommands;
import com.thatsnotm3.helpfulcommands.command.IHelpfulCommandsCommand;
import com.thatsnotm3.helpfulcommands.command.util.ModCommandManager;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.world.GameRules;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CMD_fly implements IHelpfulCommandsCommand {
    public static ModCommandManager.ModCommand cmd;

    public static void init(ModCommandManager.ModCommand newData){
        cmd=newData;
    }

    public static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment){
        dispatcher.register(CommandManager.literal(cmd.name)
                        .then(CommandManager.argument("target(s)", EntityArgumentType.players())
                                .then(CommandManager.argument("state", BoolArgumentType.bool())
                                        .executes(ctx->execute(ctx,EntityArgumentType.getPlayers(ctx,"target(s)"),BoolArgumentType.getBool(ctx,"state"),true))
                                )
                                .executes(ctx->execute(ctx,EntityArgumentType.getPlayers(ctx,"target(s)"),false,false))
                        )
                .executes(CMD_fly::execute)
                .requires(Permissions.require(HelpfulCommands.modID+".command."+cmd.category.toString().toLowerCase()+"."+cmd.name,cmd.defaultRequiredLevel))
        );
    }

    private static int execute(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException{
        ServerCommandSource src=ctx.getSource();

        if(!src.isExecutedByPlayer()){
            src.sendError(Text.translatable("error.specifyTargets"));
            return -1;
        }

        ServerPlayerEntity plr=src.getPlayer();
        toggleFlyingForTarget(plr);

        boolean newState=plr.getAbilities().allowFlying;
        Style s=newState ? HelpfulCommands.style.enabled : HelpfulCommands.style.disabled;
        src.sendFeedback(()-> Text.translatable("commands.fly.success.self."+newState).setStyle(s),true);

        return Command.SINGLE_SUCCESS;
    }

    private static int execute(CommandContext<ServerCommandSource> ctx, Collection<? extends ServerPlayerEntity> targets, boolean state, boolean useState) throws CommandSyntaxException{
        ServerCommandSource src=ctx.getSource();

        Map<String, Boolean> entries=new HashMap<>(toggleFlying(src, targets,state,useState));

        int count=0;
        MutableText entryList=Text.empty();
        for(Map.Entry<String, Boolean> i : entries.entrySet()){
            Style s=HelpfulCommands.style.simpleText;
            if(!useState) s=i.getValue() ? HelpfulCommands.style.enabled : HelpfulCommands.style.disabled;
            entryList.append(Text.literal(i.getKey()).setStyle(s));
            if(count!=entries.size()-1) entryList.append("\n");

            count+=1;
        }

        if(count>0) {
            MutableText finalCount=Text.literal(String.valueOf(count)).setStyle(HelpfulCommands.style.primary
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,entryList))
            );
            String translationKey="commands.fly.success.other";
            if(useState) translationKey+="."+state;
            String finalTranslationKey=translationKey;
            src.sendFeedback(() -> Text.translatable(finalTranslationKey, finalCount).setStyle(HelpfulCommands.style.success), true);

        } else{
            src.sendError(Text.translatable("error.didntFindTargets").setStyle(HelpfulCommands.style.error));
        }

        return Command.SINGLE_SUCCESS;
    }

    private static Map<String, Boolean> toggleFlying(ServerCommandSource src, Collection<? extends ServerPlayerEntity> targets, boolean state, boolean useState){
        Map<String, Boolean> entries=new HashMap<>();
        boolean feedback=src.getWorld().getGameRules().getBoolean(GameRules.SEND_COMMAND_FEEDBACK);

        for(ServerPlayerEntity i : targets){
            if(useState){
                if(!toggleFlyingForTarget(i,state)) continue;
            } else toggleFlyingForTarget(i);
            boolean newState=useState ? state : i.getAbilities().allowFlying;
            if(feedback){
                if(src!=i.getCommandSource()){
                    Style s=newState ? HelpfulCommands.style.enabled : HelpfulCommands.style.disabled;
                    if(src.getPlayer()!=i) i.sendMessage(Text.translatable("commands.fly.success.self."+newState).setStyle(s));
                }
            }
            String name=i.getName().getString();
            entries.put(name,newState);
        }

        return entries;
    }

    private static void toggleFlyingForTarget(ServerPlayerEntity target){
        PlayerAbilities abilities=target.getAbilities();
        abilities.allowFlying=!abilities.allowFlying;
        if(!abilities.allowFlying) abilities.flying=false;
        target.sendAbilitiesUpdate();
    }
    private static boolean toggleFlyingForTarget(ServerPlayerEntity target, boolean state){
        PlayerAbilities abilities=target.getAbilities();
        if(abilities.allowFlying==state) return false;
        abilities.allowFlying=state;
        if(!state) abilities.flying=false;
        target.sendAbilitiesUpdate();
        return true;
    }
}