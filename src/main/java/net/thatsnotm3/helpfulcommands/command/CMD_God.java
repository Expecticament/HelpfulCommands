package net.thatsnotm3.helpfulcommands.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class CMD_God {

    static final String cmdName="god";

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment){
        dispatcher.register(CommandManager.literal(cmdName)
            .then(CommandManager.argument("state", BoolArgumentType.bool())
                    .executes(ctx->run(ctx, BoolArgumentType.getBool(ctx, "state"),null))
                    .then(CommandManager.argument("target", EntityArgumentType.players()).executes(ctx->run(ctx,BoolArgumentType.getBool(ctx, "state"),EntityArgumentType.getPlayers(ctx, "target"))))
            )
        );
    }

    public static int run(CommandContext<ServerCommandSource> ctx, Boolean state, Collection<ServerPlayerEntity> targets) throws CommandSyntaxException{
        ServerPlayerEntity player=ctx.getSource().getPlayer();

        if(!ModCommandManager.RunChecks(cmdName,player)) return -1;

        Formatting f=Formatting.GREEN;
        if(!state) f=Formatting.RED;

        if(targets!=null){
            int i=0;
            Iterator<? extends Entity> iter=targets.iterator();
            List<String> targetNames=new ArrayList<String>();
            while(iter.hasNext()){
                ServerPlayerEntity target=(ServerPlayerEntity) iter.next();

                target.getAbilities().invulnerable=state;
                target.sendAbilitiesUpdate();

                if(target!=player){
                    MutableText msg=Text.literal(player.getName().getString()+": ")
                        .formatted(Formatting.GRAY)
                        .append(Text.translatable("message.command.god.self."+state).formatted(f))
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
                MutableText msg=Text.translatable("message.command.god.target."+state,Text.literal(Integer.toString(i)).setStyle(playerList)).formatted(Formatting.GREEN);
                player.sendMessage(msg);
            } else{
                player.sendMessage(Text.translatable("text.noTargets").formatted(Formatting.RED));
            }
        } else{
            player.getAbilities().invulnerable=state;
            player.sendAbilitiesUpdate();
            player.sendMessage(Text.translatable("message.command.god.self."+state).formatted(f));
        }

        return 1;
    }
}