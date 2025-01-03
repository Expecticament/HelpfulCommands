package com.expecticament.helpfulcommands.command.entities;

import com.expecticament.helpfulcommands.command.IHelpfulCommandsCommand;
import com.expecticament.helpfulcommands.command.ModCommandManager;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.expecticament.helpfulcommands.HelpfulCommands;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;
import net.minecraft.world.GameRules;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CMD_gm implements IHelpfulCommandsCommand {

    public static ModCommandManager.ModCommand cmd;

    public static void init(ModCommandManager.ModCommand newData){
        cmd=newData;
    }

    public static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment){
        dispatcher.register(CommandManager.literal(cmd.name)
                .then(CommandManager.literal("a")
                        .then(CommandManager.argument("player(s)", EntityArgumentType.players())
                                .executes(ctx->execute(ctx,GameMode.ADVENTURE,EntityArgumentType.getPlayers(ctx,"player(s)")))
                        )
                        .executes(ctx->execute(ctx,GameMode.ADVENTURE))
                )
                .then(CommandManager.literal("c")
                        .then(CommandManager.argument("player(s)", EntityArgumentType.players())
                                .executes(ctx->execute(ctx,GameMode.CREATIVE,EntityArgumentType.getPlayers(ctx,"player(s)")))
                        )
                        .executes(ctx->execute(ctx,GameMode.CREATIVE))
                )
                .then(CommandManager.literal("s")
                        .then(CommandManager.argument("player(s)", EntityArgumentType.players())
                                .executes(ctx->execute(ctx,GameMode.SURVIVAL,EntityArgumentType.getPlayers(ctx,"player(s)")))
                        )
                        .executes(ctx->execute(ctx,GameMode.SURVIVAL))
                )
                .then(CommandManager.literal("sp")
                        .then(CommandManager.argument("player(s)", EntityArgumentType.players())
                                .executes(ctx->execute(ctx,GameMode.SPECTATOR,EntityArgumentType.getPlayers(ctx,"player(s)")))
                        )
                        .executes(ctx->execute(ctx,GameMode.SPECTATOR))
                )
                .requires(src->ModCommandManager.canUseCommand(src,cmd))
        );
    }

    private static int execute(CommandContext<ServerCommandSource> ctx, GameMode gm) throws CommandSyntaxException{
        ServerCommandSource src=ctx.getSource();

        src.getPlayer().setCustomName(Text.literal("gameModer").setStyle(HelpfulCommands.style.error));

        if(!src.isExecutedByPlayer()){
            src.sendError(Text.translatable("error.specifyTargets").setStyle(HelpfulCommands.style.error));
            return -1;
        }

        ServerPlayerEntity plr=src.getPlayer();
        if(!plr.changeGameMode(gm)) return Command.SINGLE_SUCCESS;

        src.sendFeedback(()->Text.translatable("commands.gm.success.self", Text.translatable("gameMode."+gm.getName()).setStyle(HelpfulCommands.style.primary)).setStyle(HelpfulCommands.style.success),true);

        return Command.SINGLE_SUCCESS;
    }

    private static int execute(CommandContext<ServerCommandSource> ctx, GameMode gm, Collection<? extends ServerPlayerEntity> targets) throws CommandSyntaxException{
        ServerCommandSource src=ctx.getSource();

        Map<String, Integer> entries=new HashMap<>(changeGameMode(src, gm, targets));

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
            src.sendFeedback(() -> Text.translatable("commands.gm.success.other", Text.translatable("gameMode."+gm.getName()).setStyle(HelpfulCommands.style.primary), finalCount).setStyle(HelpfulCommands.style.success), true);
        } else{
            src.sendError(Text.translatable("error.didntFindTargets").setStyle(HelpfulCommands.style.error));
        }

        return Command.SINGLE_SUCCESS;
    }
    private static Map<String,Integer> changeGameMode(ServerCommandSource src, GameMode gm, Collection<? extends ServerPlayerEntity> targets){
        Map<String, Integer> entries=new HashMap<>();
        boolean feedback=src.getWorld().getGameRules().getBoolean(GameRules.SEND_COMMAND_FEEDBACK);

        for(ServerPlayerEntity i : targets){
            if(!i.changeGameMode(gm)) continue;
            i.extinguishWithSound();
            int diff=1;
            if(i.isPlayer()){
                diff=-1;
                if(feedback){
                    if(src.getPlayer()!=i) i.sendMessage(Text.translatable("commands.gm.success.self",Text.translatable("gameMode."+gm.getName()).setStyle(HelpfulCommands.style.primary)).setStyle(HelpfulCommands.style.tertiary));
                }
            }
            String name=i.getName().getString();
            entries.put(name,entries.getOrDefault(name,0)+diff);
        }

        return entries;
    }
}