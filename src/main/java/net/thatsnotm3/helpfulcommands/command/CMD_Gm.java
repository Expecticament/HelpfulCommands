package net.thatsnotm3.helpfulcommands.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.GameMode;

public class CMD_Gm{

    static final String cmdName="gm";

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment){
        dispatcher.register(CommandManager.literal(cmdName)
            .then(CommandManager.literal("c").executes(ctx->switchGameMode(ctx,0,null))
                .then(CommandManager.argument("target", EntityArgumentType.players())
                .executes(ctx->switchGameMode(ctx,0,EntityArgumentType.getPlayers(ctx, "target")))
                )
            )
            .then(CommandManager.literal("s").executes(ctx->switchGameMode(ctx,1,null))
                .then(CommandManager.argument("target", EntityArgumentType.players())
                .executes(ctx->switchGameMode(ctx,1,EntityArgumentType.getPlayers(ctx, "target")))
                )
            )
            .then(CommandManager.literal("a").executes(ctx->switchGameMode(ctx,2,null))
                .then(CommandManager.argument("target", EntityArgumentType.players())
                .executes(ctx->switchGameMode(ctx,2,EntityArgumentType.getPlayers(ctx, "target")))
                )
            )
            .then(CommandManager.literal("sp").executes(ctx->switchGameMode(ctx,3,null))
                .then(CommandManager.argument("target", EntityArgumentType.players())
                .executes(ctx->switchGameMode(ctx,3,EntityArgumentType.getPlayers(ctx, "target")))
                )
            )
        );
    }

    public static int switchGameMode(CommandContext<ServerCommandSource> ctx, int gm, Collection<ServerPlayerEntity> targets) throws CommandSyntaxException{ // 0 - Creative; 1 - Survival; 2 - Adventure; 3 - Spectator
        ServerPlayerEntity player=ctx.getSource().getPlayer();
        
        if(!ModCommandManager.RunChecks(cmdName,player)) return -1;

        GameMode gameMode;
        switch(gm){
            default:
                gameMode=GameMode.CREATIVE;
                break;
            case 1:
                gameMode=GameMode.SURVIVAL;
                break;
            case 2:
                gameMode=GameMode.ADVENTURE;
                break;
            case 3:
                gameMode=GameMode.SPECTATOR;
                break;
        }

        if(targets!=null){
            int i=0;
            Iterator iter=targets.iterator();
            List<String> targetNames=new ArrayList<String>();
            while(iter.hasNext()){
                ServerPlayerEntity target=(ServerPlayerEntity)iter.next();
                if(target.changeGameMode(gameMode)){
                    if(target!=player){
                        MutableText part1=Text.literal("\u00A77"+player.getEntityName()+":\u00A7r ");
                        MutableText part2=Text.translatable("message.command.gm.self", Text.translatable("gameMode."+gameMode.getName()).formatted(Formatting.GOLD));
                        MutableText msg=part1.append(part2);
                        target.sendMessage(msg);
                    }
                    targetNames.add(target.getEntityName());
                    ++i;
                }
            }
            if(i>0){
                String allTargetNames="";
                for(String n : targetNames) allTargetNames=allTargetNames+n+"\n";
                allTargetNames=allTargetNames.substring(0, allTargetNames.length()-1);
                Style playerList=Style.EMPTY
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.literal(allTargetNames)))
                    .withColor(Formatting.AQUA)
                ;
                MutableText msg=Text.translatable("message.command.gm.target",Text.translatable("gameMode."+gameMode.getName()).formatted(Formatting.GOLD),Text.literal(Integer.toString(i)).setStyle(playerList));
                player.sendMessage(msg);
            }
        } else{
            if(player.changeGameMode(gameMode)){
                MutableText msg=Text.translatable("message.command.gm.self",Text.translatable("gameMode."+gameMode.getName()).formatted(Formatting.GOLD));
                player.sendMessage(msg);
            }
        }
        return 1;
    }
}