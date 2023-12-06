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
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.server.command.CommandManager;
import net.minecraft.command.argument.DimensionArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;

public class CMD_Dimension{

    static final String cmdName="dimension";

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment){
        dispatcher.register(CommandManager.literal(cmdName)
            .then(CommandManager.literal("get")
                .then(CommandManager.argument("target", EntityArgumentType.player()).executes(ctx->getDimension(ctx, EntityArgumentType.getPlayer(ctx, "target"))))
                .executes(ctx->getDimension(ctx,null))
            )
            .then(CommandManager.argument("dimension",DimensionArgumentType.dimension())
                .executes(ctx->switchDimension(ctx,DimensionArgumentType.getDimensionArgument(ctx, "dimension"),null))
                .then(CommandManager.argument("target",EntityArgumentType.entities()).executes(ctx->switchDimension(ctx, DimensionArgumentType.getDimensionArgument(ctx, "dimension"), EntityArgumentType.getEntities(ctx, "target"))))
            )
        );
    }
    static int switchDimension(CommandContext<ServerCommandSource> ctx, ServerWorld dim, Collection<? extends Entity> targets) throws CommandSyntaxException{
        ServerPlayerEntity player=ctx.getSource().getPlayer();

        if(!ModCommandManager.RunChecks(cmdName,player)) return -1;

        String dimensionName=dim.getRegistryKey().getValue().toString();

        if(targets!=null){
            int i=0;
            Iterator<? extends Entity> iter=targets.iterator();
            List<String> targetNames=new ArrayList<String>();
            while(iter.hasNext()){
                Entity target=(Entity) iter.next();
                if(target.getWorld()==dim) continue;
                if(target.teleport(dim, target.getX(), target.getY(), target.getZ(), null, target.getYaw(), target.getPitch())){
                    if(target!=player){
                        MutableText msg=Text.literal(player.getName().getString()+": ")
                            .formatted(Formatting.GRAY)
                            .append(Text.translatable("message.command.dimension.self",Text.literal(dimensionName).formatted(Formatting.GOLD)).formatted(Formatting.WHITE))
                        ;
                        target.sendMessage(msg);
                    }
                    targetNames.add(target.getName().getString());
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
                MutableText msg=Text.translatable("message.command.dimension.target",Text.translatable(dimensionName).formatted(Formatting.GOLD),Text.literal(Integer.toString(i)).setStyle(playerList)).formatted(Formatting.GREEN);
                player.sendMessage(msg);
            } else{
                player.sendMessage(Text.translatable("text.noTargets").formatted(Formatting.RED));
            }
        } else{
            if(player.getWorld()==dim) return 1;
            player.teleport(dim, player.getX(), player.getY(), player.getZ(), player.getYaw(), player.getPitch());
            MutableText msg=Text.translatable("message.command.dimension.self", Text.literal(dimensionName).formatted(Formatting.GOLD)).formatted(Formatting.GREEN);
            player.sendMessage(msg);
        }

        return 1;
    }
    static int getDimension(CommandContext<ServerCommandSource> ctx, ServerPlayerEntity target) throws CommandSyntaxException{
        ServerPlayerEntity player=ctx.getSource().getPlayer();

        if(!ModCommandManager.RunChecks("dimension",player)) return -1;

        if(target==player) target=null;

        RegistryKey<World> currentDimension;
        if(target==null) currentDimension=player.getWorld().getRegistryKey();
        else currentDimension=target.getWorld().getRegistryKey();
        String dimensionName=currentDimension.getValue().toString();

        if(target!=null){
            player.sendMessage(Text.translatable("message.command.dimension.get.target",Text.literal(target.getName().getString()).formatted(Formatting.GOLD),Text.literal(dimensionName).formatted(Formatting.GOLD)).formatted(Formatting.AQUA));
        } else{
            player.sendMessage(Text.literal("Your current Dimension is: "+"\u00A76"+dimensionName).formatted(Formatting.AQUA));
        }

        return 1;
    }
}