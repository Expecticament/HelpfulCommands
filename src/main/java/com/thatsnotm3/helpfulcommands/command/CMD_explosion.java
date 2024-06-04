package com.thatsnotm3.helpfulcommands.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.thatsnotm3.helpfulcommands.HelpfulCommands;
import com.thatsnotm3.helpfulcommands.command.util.ModCommandManager;
import com.thatsnotm3.helpfulcommands.util.ConfigManager;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class CMD_explosion implements IHelpfulCommandsCommand {
    public static ModCommandManager.ModCommand cmd;

    public static void init(ModCommandManager.ModCommand newData){
        cmd=newData;
    }

    public static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment){
        dispatcher.register(CommandManager.literal(cmd.name)
                .then(CommandManager.argument("power", IntegerArgumentType.integer(1))
                        .then(CommandManager.argument("distance", DoubleArgumentType.doubleArg(0))
                                .executes(ctx->execute(ctx, IntegerArgumentType.getInteger(ctx,"power"), DoubleArgumentType.getDouble(ctx,"distance")))
                        )
                        .executes(ctx->execute(ctx,IntegerArgumentType.getInteger(ctx,"power")))
                )
                .requires(Permissions.require(HelpfulCommands.modID+".command."+cmd.category.toString().toLowerCase()+"."+cmd.name,cmd.defaultRequiredLevel))
        );
    }

    private static int execute(CommandContext<ServerCommandSource> ctx,int power) throws CommandSyntaxException {
        ServerCommandSource src=ctx.getSource();

        if(power>ConfigManager.loadConfig(src.getServer()).explosionPowerLimit){
            powerLimitExceeded(src,power);
            return -1;
        }

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
                createExplosion(src,power,posX,posY,posZ);
            }
        }

        return Command.SINGLE_SUCCESS;
    }

    private static int execute(CommandContext<ServerCommandSource> ctx,int power,double distance) throws CommandSyntaxException {
        ServerCommandSource src=ctx.getSource();

        if(power>ConfigManager.loadConfig(src.getServer()).explosionPowerLimit){
            powerLimitExceeded(src,power);
            return -1;
        }

        if(!src.isExecutedByPlayer()) {
            src.sendError(Text.translatable("error.inGameOnly"));
            return -1;
        } else{
            ServerPlayerEntity plr=src.getPlayer();

            double posX=plr.getX()+plr.getRotationVector().getX()*distance;
            double posY=(plr.getY()+plr.getEyeHeight(plr.getPose())+plr.getRotationVector().getY()*distance)-1;
            double posZ=plr.getZ()+plr.getRotationVector().getZ()*distance;

            createExplosion(src,power,posX,posY,posZ);
        }
        return Command.SINGLE_SUCCESS;
    }

    private static void powerLimitExceeded(ServerCommandSource src, int specifiedPower){
        ConfigManager.ModConfig cfg=ConfigManager.loadConfig(src.getServer());
        src.sendError(Text.translatable("commands.explosion.error.powerLimitExceeded",Text.literal(String.valueOf(specifiedPower)).setStyle(HelpfulCommands.style.primary),Text.literal(String.valueOf(cfg.explosionPowerLimit)).setStyle(HelpfulCommands.style.primary)));
    }

    private static void createExplosion(ServerCommandSource src, int power, double posX, double posY, double posZ){
        src.getWorld().createExplosion(null, posX, posY, posZ, power, World.ExplosionSourceType.TNT);

        src.sendFeedback(()->Text.translatable("commands.explosion.success",Text.literal(String.valueOf(power)).setStyle(HelpfulCommands.style.primary),Text.literal(String.format("%.2f",posX)).setStyle(HelpfulCommands.style.primary),Text.literal(String.format("%.2f",posY)).setStyle(HelpfulCommands.style.primary),Text.literal(String.format("%.2f",posZ)).setStyle(HelpfulCommands.style.primary)).setStyle(HelpfulCommands.style.secondary
                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/tp @s "+posX+" "+posY+" "+posZ))
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.translatable("tooltips.clickToTeleportToCoordinates")))
        ),true);
    }
}