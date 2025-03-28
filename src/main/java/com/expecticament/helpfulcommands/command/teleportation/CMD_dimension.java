package com.expecticament.helpfulcommands.command.teleportation;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.expecticament.helpfulcommands.HelpfulCommands;
import com.expecticament.helpfulcommands.command.IHelpfulCommandsCommand;
import com.expecticament.helpfulcommands.command.ModCommandManager;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class CMD_dimension implements IHelpfulCommandsCommand {

    public static ModCommandManager.ModCommand cmd;
    private static MinecraftServer server=null;
    private static CommandDispatcher<ServerCommandSource> dispatcher_;
    private static CommandRegistryAccess registryAccess_;
    private static CommandManager.RegistrationEnvironment environment_;

    public static void init(ModCommandManager.ModCommand newData){
        cmd=newData;
        ServerLifecycleEvents.SERVER_STARTED.register(CMD_dimension::onServerStarted);
    }

    public static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment){
        if(server==null){
            dispatcher_=dispatcher;
            registryAccess_=registryAccess;
            environment_=environment;
            return;
        }

        LiteralArgumentBuilder<ServerCommandSource> dimensions=CommandManager.literal("switch");
        for(RegistryKey<World> i : server.getWorldRegistryKeys()){
            ServerWorld dimension=server.getWorld(i);
            if(dimension==null) continue;

            dimensions.then(CommandManager.literal(i.getValue().toString())
                    .executes(ctx->execute(ctx,dimension))
                    .then(CommandManager.argument("target(s)",EntityArgumentType.entities())
                            .executes(ctx->execute(ctx,dimension,EntityArgumentType.getEntities(ctx,"target(s)")))
                    )
            );
        }
        dispatcher.register(CommandManager.literal(cmd.name)
                .then(dimensions)
                .then(CommandManager.literal("get")
                        .then(CommandManager.argument("target",EntityArgumentType.entity())
                                .executes(ctx->getDimension(ctx,EntityArgumentType.getEntity(ctx,"target")))
                        )
                        .executes(CMD_dimension::getDimension)
                )
                .executes(null)
                .requires(src->ModCommandManager.canUseCommand(src,cmd))
        );
    }

    private static void onServerStarted(MinecraftServer newServer) {
        server=newServer;
        registerCommand(dispatcher_,registryAccess_,environment_);
    }

    private static int execute(CommandContext<ServerCommandSource> ctx, ServerWorld dimension) throws CommandSyntaxException{
        ServerCommandSource src=ctx.getSource();

        if(!src.isExecutedByPlayer()){
            src.sendError(Text.translatable("error.specifyTargets").setStyle(HelpfulCommands.style.error));
            return -1;
        }

        ServerPlayerEntity plr=src.getPlayer();
        if(plr.getServerWorld()==dimension){
            src.sendError(Text.translatable("commands.dimension.error.alreadyInDimension").setStyle(HelpfulCommands.style.error));
            return -1;
        }
        BlockPos plrPos=plr.getBlockPos();
        plr.teleport(dimension, plrPos.getX(), plrPos.getY(), plrPos.getZ(), new HashSet<>(), plr.getYaw(), plr.getPitch(), false);

        src.sendFeedback(()->Text.translatable("commands.dimension.success.self",Text.literal(dimension.getRegistryKey().getValue().toString()).setStyle(HelpfulCommands.style.primary)).setStyle(HelpfulCommands.style.success),true);

        return Command.SINGLE_SUCCESS;
    }

    private static int execute(CommandContext<ServerCommandSource> ctx, ServerWorld dimension, Collection<? extends Entity> targets) throws CommandSyntaxException{
        ServerCommandSource src=ctx.getSource();

        Map<String, Integer> entries=new HashMap<>(switchDimension(src, dimension, targets));

        int count=0;
        String entryList="";
        for(Map.Entry<String, Integer> i : entries.entrySet()){
            if(i.getValue()<0){
                entryList+=i.getKey()+"\n";
                count+=1;
            } else{
                entryList+=i.getValue()+"x "+i.getKey()+"\n";
                count+=i.getValue();
            }
        }
        if(!entryList.isEmpty()) entryList=entryList.substring(0, entryList.length()-1);

        if(count>0) {
            MutableText finalCount=Text.literal(String.valueOf(count)).setStyle(HelpfulCommands.style.primary
                    .withHoverEvent(new HoverEvent.ShowText(Text.literal(entryList)))
            );
            src.sendFeedback(() -> Text.translatable("commands.dimension.success.other", Text.literal(dimension.getRegistryKey().getValue().toString()).setStyle(HelpfulCommands.style.primary), finalCount).setStyle(HelpfulCommands.style.success), true);
        } else{
            src.sendError(Text.translatable("error.didntFindTargets").setStyle(HelpfulCommands.style.error));
        }

        return Command.SINGLE_SUCCESS;
    }
    private static Map<String,Integer> switchDimension(ServerCommandSource src, ServerWorld dimension, Collection<? extends Entity> targets){
        Map<String, Integer> entries=new HashMap<>();
        boolean feedback=src.getWorld().getGameRules().getBoolean(GameRules.SEND_COMMAND_FEEDBACK);

        for(Entity i : targets){
            if(i.getWorld()==dimension) continue;
            BlockPos pos=i.getBlockPos();
            if(!i.teleport(dimension, pos.getX(), pos.getY(), pos.getZ(), new HashSet<>(), i.getYaw(), i.getPitch(), false)) continue;
            int diff=1;
            if(i.isPlayer()){
                diff=-1;
                if(feedback){
                    if(src.getEntity()!=i && i.isPlayer()) ((ServerPlayerEntity) i).sendMessage(Text.translatable("commands.dimension.success.self", Text.literal(dimension.getRegistryKey().getValue().toString()).setStyle(HelpfulCommands.style.primary)).setStyle(HelpfulCommands.style.tertiary));
                }
            }
            String name=i.getName().getString();
            entries.put(name,entries.getOrDefault(name,0)+diff);
        }

        return entries;
    }

    private static int getDimension(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException{
        ServerCommandSource src=ctx.getSource();

        if(!src.isExecutedByPlayer()){
            src.sendError(Text.translatable("error.specifyTargets").setStyle(HelpfulCommands.style.error));
            return -1;
        }

        String dimensionName=src.getPlayer().getWorld().getRegistryKey().getValue().toString();

        src.sendMessage(Text.translatable("commands.dimension.get.success.self",Text.literal(dimensionName).setStyle(HelpfulCommands.style.primary
                .withHoverEvent(new HoverEvent.ShowText(Text.translatable("tooltips.clickToTeleportToDimension")))
                .withClickEvent(new ClickEvent.RunCommand("/dimension switch "+dimensionName))
        )).setStyle(HelpfulCommands.style.secondary));

        return Command.SINGLE_SUCCESS;
    }
    private static int getDimension(CommandContext<ServerCommandSource> ctx, Entity target) throws CommandSyntaxException{
        ServerCommandSource src=ctx.getSource();

        if(src.isExecutedByPlayer()) if(target==src.getEntity()){
            getDimension(ctx);
            return Command.SINGLE_SUCCESS;
        }

        String dimensionName=target.getWorld().getRegistryKey().getValue().toString();

        src.sendMessage(Text.translatable("commands.dimension.get.success.other",Text.literal(target.getName().getString()).setStyle(HelpfulCommands.style.primary),Text.literal(dimensionName).setStyle(HelpfulCommands.style.primary
                .withHoverEvent(new HoverEvent.ShowText(Text.translatable("tooltips.clickToTeleportToDimension")))
                .withClickEvent(new ClickEvent.RunCommand("/dimension switch "+dimensionName))
        )).setStyle(HelpfulCommands.style.secondary));

        return Command.SINGLE_SUCCESS;
    }
}