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
import net.minecraft.text.Text;
import net.minecraft.world.GameRules;

import java.util.Collection;

public class CMD_god implements IHelpfulCommandsCommand {
    public static ModCommandManager.hcCommand cmd;

    public static void init(ModCommandManager.hcCommand newData){
        cmd=newData;
    }

    public static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment){
        dispatcher.register(CommandManager.literal(cmd.name)
                        .then(CommandManager.argument("target(s)", EntityArgumentType.players())
                                .then(CommandManager.argument("state", BoolArgumentType.bool())
                                        .executes(ctx->execute(ctx,EntityArgumentType.getPlayers(ctx,"target(s)"),BoolArgumentType.getBool(ctx,"state")))
                                )
                                .executes(ctx->execute(ctx,EntityArgumentType.getPlayers(ctx,"target(s)")))
                        )
                .executes(CMD_god::execute)
                .requires(Permissions.require(HelpfulCommands.modID+".command."+cmd.category.toString().toLowerCase()+"."+cmd.name,cmd.defaultRequiredLevel))
        );
    }

    private static int execute(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        ServerCommandSource src=ctx.getSource();

        if(!src.isExecutedByPlayer()){
            src.sendError(Text.translatable("error.specifyTargets"));
            return -1;
        }

        ServerPlayerEntity plr=src.getPlayer();
        sendFeedback(src,plr,toggleInvulnerabilityForTarget(plr));
        return Command.SINGLE_SUCCESS;
    }
    private static int execute(CommandContext<ServerCommandSource> ctx, Collection<ServerPlayerEntity> targets) throws CommandSyntaxException {
        for(ServerPlayerEntity i : targets){
            sendFeedback(ctx.getSource(), i, toggleInvulnerabilityForTarget(i));
        }
        return Command.SINGLE_SUCCESS;
    }
    private static int execute(CommandContext<ServerCommandSource> ctx, Collection<ServerPlayerEntity> targets,boolean state) throws CommandSyntaxException {
        for(ServerPlayerEntity i : targets){
            if(i.getAbilities().invulnerable==state) continue;
            sendFeedback(ctx.getSource(), i, toggleInvulnerabilityForTarget(i,state));
        }
        return Command.SINGLE_SUCCESS;
    }

    private static void sendFeedback(ServerCommandSource source, ServerPlayerEntity player, boolean state){
        if(source.getEntity()==player){
            if(state) source.sendFeedback(()->Text.translatable("commands.god.success.self.true").setStyle(HelpfulCommands.style.enabled), true);
            else source.sendFeedback(()->Text.translatable("commands.god.success.self.false").setStyle(HelpfulCommands.style.disabled), true);
        } else {
            if(source.getWorld().getGameRules().getBoolean(GameRules.SEND_COMMAND_FEEDBACK)){
                if(state) player.sendMessage(Text.translatable("commands.god.success.self.true").setStyle(HelpfulCommands.style.enabled));
                else player.sendMessage(Text.translatable("commands.god.success.self.false").setStyle(HelpfulCommands.style.disabled));
            }
            if(state) source.sendFeedback(() -> Text.translatable("commands.god.success.other.true", player.getDisplayName()).setStyle(HelpfulCommands.style.enabled), true);
            else source.sendFeedback(() -> Text.translatable("commands.god.success.other.false", player.getDisplayName()).setStyle(HelpfulCommands.style.disabled), true);
        }
    }

    private static boolean toggleInvulnerabilityForTarget(ServerPlayerEntity target){
        PlayerAbilities abilities=target.getAbilities();
        abilities.invulnerable=!abilities.invulnerable;
        target.sendAbilitiesUpdate();
        return abilities.invulnerable;
    }
    private static boolean toggleInvulnerabilityForTarget(ServerPlayerEntity target, boolean state){
        PlayerAbilities abilities=target.getAbilities();
        abilities.invulnerable=state;
        target.sendAbilitiesUpdate();
        return state;
    }
}