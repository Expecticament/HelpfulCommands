package com.expecticament.helpfulcommands.command.entities;

import com.expecticament.helpfulcommands.command.IHelpfulCommandsCommand;
import com.expecticament.helpfulcommands.command.ModCommandManager;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.expecticament.helpfulcommands.HelpfulCommands;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
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

public class CMD_heal implements IHelpfulCommandsCommand {

    public static ModCommandManager.ModCommand cmd;

    public static void init(ModCommandManager.ModCommand newData){
        cmd=newData;
    }

    public static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment){
        dispatcher.register(CommandManager.literal(cmd.name)
                .then(CommandManager.argument("target(s)",EntityArgumentType.entities())
                        .executes(ctx->execute(ctx, EntityArgumentType.getEntities(ctx,"target(s)")))
                        .then(CommandManager.argument("amount", FloatArgumentType.floatArg(0.5f))
                                .executes(ctx->execute(ctx, EntityArgumentType.getEntities(ctx,"target(s)"), FloatArgumentType.getFloat(ctx, "amount")))
                        )
                )
                .executes(CMD_heal::execute)
                .requires(src->ModCommandManager.canUseCommand(src,cmd))
        );
    }

    private static int execute(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException{
        ServerCommandSource src=ctx.getSource();

        if(!src.isExecutedByPlayer()){
            src.sendError(Text.translatable("error.specifyTargets").setStyle(HelpfulCommands.style.error));
            return -1;
        }

        ServerPlayerEntity plr=src.getPlayer();
        if(plr.getHealth()==plr.getMaxHealth()) return Command.SINGLE_SUCCESS;

        plr.setHealth(plr.getMaxHealth());

        src.sendFeedback(()->Text.translatable("commands.heal.success.self").setStyle(HelpfulCommands.style.success),true);

        return Command.SINGLE_SUCCESS;
    }

    private static int execute(CommandContext<ServerCommandSource> ctx, Collection<? extends Entity> targets) throws CommandSyntaxException{
        return execute(ctx, targets, 0);
    }

    private static int execute(CommandContext<ServerCommandSource> ctx, Collection<? extends Entity> targets, float amount) throws CommandSyntaxException{
        ServerCommandSource src=ctx.getSource();

        Map<String, Integer> entries = new HashMap<>(heal(src, targets, amount));

        int count = 0;
        String entryList = "";
        for(Map.Entry<String, Integer> i : entries.entrySet()){
            if(i.getValue()<0){
                entryList += i.getKey() + "\n";
                count += 1;
            } else{
                entryList += i.getValue() + "x "+i.getKey() + "\n";
                count += i.getValue();
            }
        }
        if(!entryList.isEmpty()) entryList = entryList.substring(0, entryList.length()-1);

        if(count>0) {
            MutableText finalCount=Text.literal(String.valueOf(count)).setStyle(HelpfulCommands.style.primary
                    .withHoverEvent(new HoverEvent.ShowText(Text.literal(entryList)))
            );
            MutableText msg;
            if(amount>0){
                msg = Text.translatable("commands.heal.success.hearts.other", finalCount, Text.literal(String.valueOf(amount/2)).setStyle(HelpfulCommands.style.primary));
            } else {
                msg = Text.translatable("commands.heal.success.other", finalCount);
            }
            msg.setStyle(HelpfulCommands.style.success);
            src.sendFeedback(() -> msg, true);
        } else{
            src.sendError(Text.translatable("error.didntFindTargets").setStyle(HelpfulCommands.style.error));
        }

        return Command.SINGLE_SUCCESS;
    }

    private static Map<String,Integer> heal(ServerCommandSource src, Collection<? extends Entity> targets, float amount){
        Map<String, Integer> entries = new HashMap<>();
        boolean feedback = src.getWorld().getGameRules().getBoolean(GameRules.SEND_COMMAND_FEEDBACK);

        for(Entity i : targets){
            try {
                LivingEntity le = (LivingEntity) i;
                if (le.getHealth() == le.getMaxHealth()) continue;
                le.setHealth(amount==0 ? le.getMaxHealth() : le.getHealth() + amount);
            } catch(Exception e){ continue; }
            int diff = 1;
            if(i.isPlayer()){
                diff = -1;
                if(feedback){
                    if(src.getEntity()!=i && i.isPlayer()){
                        MutableText msg = Text.translatable("commands.heal.success.self");
                        if(amount>0){
                            msg = Text.translatable("commands.heal.success.hearts.self", Text.literal(String.valueOf(amount/2)).setStyle(HelpfulCommands.style.primary));
                        }
                        msg.setStyle(HelpfulCommands.style.success);
                        ((ServerPlayerEntity) i).sendMessage(msg);
                    }
                }
            }
            String name=i.getName().getString();
            entries.put(name,entries.getOrDefault(name,0) + diff);
        }

        return entries;
    }
}