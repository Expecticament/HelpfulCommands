package com.thatsnotm3.helpfulcommands.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.thatsnotm3.helpfulcommands.HelpfulCommands;
import com.thatsnotm3.helpfulcommands.command.util.ModCommandManager;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.util.hit.HitResult;

public class CMD_lightning implements IHelpfulCommandsCommand {
    public static ModCommandManager.ModCommand cmd;

    public static void init(ModCommandManager.ModCommand newData){
        cmd=newData;
    }

    public static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment){
        dispatcher.register(CommandManager.literal(cmd.name)
                .then(CommandManager.argument("distance", DoubleArgumentType.doubleArg(0))
                        .executes(ctx->execute(ctx, DoubleArgumentType.getDouble(ctx,"distance")))
                )
                .executes(CMD_lightning::execute)
                .requires(Permissions.require(HelpfulCommands.modID+".command."+cmd.category.toString().toLowerCase()+"."+cmd.name,cmd.defaultRequiredLevel))
        );
    }

    private static int execute(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        ServerCommandSource src=ctx.getSource();

        if(!src.isExecutedByPlayer()){
            src.sendError(Text.translatable("error.inGameOnly"));
            return -1;
        } else{
            ServerPlayerEntity plr=src.getPlayer();
            HitResult hit=plr.raycast(plr.getViewDistance() * 16, 0, false);
            if (hit != null && hit.getType()==HitResult.Type.BLOCK) {
                double posX=hit.getPos().getX();
                double posY=hit.getPos().getY();
                double posZ=hit.getPos().getZ();
                strike(src,posX,posY,posZ);
            }
        }

        return Command.SINGLE_SUCCESS;
    }

    private static int execute(CommandContext<ServerCommandSource> ctx,double distance) throws CommandSyntaxException {
        ServerCommandSource src=ctx.getSource();

        if(!src.isExecutedByPlayer()) {
            src.sendError(Text.translatable("error.inGameOnly"));
            return -1;
        } else{
            ServerPlayerEntity plr=src.getPlayer();

            double posX=plr.getX()+plr.getRotationVector().getX()*distance;
            double posY=(plr.getY()+plr.getEyeHeight(plr.getPose())+plr.getRotationVector().getY()*distance)-1;
            double posZ=plr.getZ()+plr.getRotationVector().getZ()*distance;
            strike(src,posX,posY,posZ);
        }
        return Command.SINGLE_SUCCESS;
    }

    private static void strike(ServerCommandSource src, double posX, double posY, double posZ){
        ServerWorld world=src.getWorld();
        LightningEntity le=new LightningEntity(EntityType.LIGHTNING_BOLT, world);
        le.setPos(posX,posY,posZ);
        world.spawnEntity(le);

        src.sendFeedback(()->Text.translatable("commands.lightning.success",Text.literal(String.format("%.2f",posX)).setStyle(HelpfulCommands.style.primary),Text.literal(String.format("%.2f",posY)).setStyle(HelpfulCommands.style.primary),Text.literal(String.format("%.2f",posZ)).setStyle(HelpfulCommands.style.primary)).setStyle(HelpfulCommands.style.secondary
                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/tp @s "+posX+" "+posY+" "+posZ))
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.translatable("tooltips.clickToTeleportToCoordinates")))
        ),true);
    }
}