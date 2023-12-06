package net.thatsnotm3.helpfulcommands.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.GameMode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;

public class CMD_Feed{

    static final String cmdName="feed";

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment){
        dispatcher.register(CommandManager.literal(cmdName)
            .then(CommandManager.argument("target", EntityArgumentType.players()).executes(ctx->run(ctx,EntityArgumentType.getPlayers(ctx, "target"))))
            .executes(ctx->run(ctx,null))
        );
    }

    public static int run(CommandContext<ServerCommandSource> ctx, Collection<ServerPlayerEntity> targets) throws CommandSyntaxException{
        ServerPlayerEntity player=ctx.getSource().getPlayer();

        if(!ModCommandManager.RunChecks(cmdName,player)) return -1;

        if(targets!=null){
            int i=0;
            Iterator<? extends Entity> iter=targets.iterator();
            List<String> targetNames=new ArrayList<String>();
            while(iter.hasNext()){
                ServerPlayerEntity target=(ServerPlayerEntity) iter.next();
                target.getHungerManager().setFoodLevel(20);
                target.getHungerManager().setSaturationLevel(5);
                if(target.interactionManager.getGameMode()==GameMode.SURVIVAL || target.interactionManager.getGameMode()==GameMode.ADVENTURE) target.getHungerManager().setExhaustion(0);
                
                if(target!=player){
                    MutableText msg=Text.literal(player.getName().getString()+": ")
                        .formatted(Formatting.GRAY)
                        .append(Text.translatable("message.command.feed.self").formatted(Formatting.GREEN))
                    ;
                    target.sendMessage(msg);
                }

                targetNames.add(target.getName().getString());
                ++i;
            }
            if(i>0){
                String allTargetNames="";
                for(String n : targetNames) allTargetNames=allTargetNames+n+"\n";
                allTargetNames=allTargetNames.substring(0, allTargetNames.length()-1);
                Style playerList=Style.EMPTY
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.literal(allTargetNames)))
                    .withColor(Formatting.AQUA)
                ;
                MutableText msg=Text.translatable("message.command.feed.target",Text.literal(Integer.toString(i)).setStyle(playerList)).formatted(Formatting.GREEN);
                player.sendMessage(msg);
            } else{
                player.sendMessage(Text.translatable("text.noTargets").formatted(Formatting.RED));
            }
        } else{
            player.getHungerManager().setFoodLevel(20);
            player.getHungerManager().setSaturationLevel(5);
            if(player.interactionManager.getGameMode()==GameMode.SURVIVAL || player.interactionManager.getGameMode()==GameMode.ADVENTURE) player.getHungerManager().setExhaustion(0);
            player.sendMessage(Text.translatable("message.command.feed.self").formatted(Formatting.GREEN));
        }

        return 1;
    }
}