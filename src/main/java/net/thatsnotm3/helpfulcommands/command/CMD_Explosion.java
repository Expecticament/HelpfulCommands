package net.thatsnotm3.helpfulcommands.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World.ExplosionSourceType;
import net.thatsnotm3.helpfulcommands.util.ConfigManager;

public class CMD_Explosion{

    static final String cmdName="explosion";

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment){
        dispatcher.register(CommandManager.literal(cmdName)
            .then(CommandManager.argument("distance", IntegerArgumentType.integer(0))
            .then(CommandManager.argument("power", IntegerArgumentType.integer(0)).executes(ctx -> run(ctx, IntegerArgumentType.getInteger(ctx, "distance"),IntegerArgumentType.getInteger(ctx, "power")))))
        );
    }

    public static int run(CommandContext<ServerCommandSource> ctx,int distance,int power) throws CommandSyntaxException{
        ServerPlayerEntity player=ctx.getSource().getPlayer();

        if(!ModCommandManager.RunChecks(cmdName,player)) return -1;

        double posX=player.getX()+player.getRotationVector().getX()*distance;
        double posY=player.getY()+player.getEyeHeight(player.getPose())+player.getRotationVector().getY()*distance;
        double posZ=player.getZ()+player.getRotationVector().getZ()*distance;

        ConfigManager.ModConfig cfg=ConfigManager.loadConfig(player.getServer());

        if(power>cfg.explosionPowerLimit){
            player.sendMessage(Text.translatable("message.error.command.explosion.explosionTooPowerful",Text.literal(Integer.toString(power)).formatted(Formatting.GOLD),Text.literal(Integer.toString(cfg.explosionPowerLimit)).setStyle(Style.EMPTY
                    .withFormatting(Formatting.GOLD)
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.translatable("tooltip.highlight.clickToEditConfigValue")))
                    .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,"/hc config explosionPowerLimit "))
            )).formatted(Formatting.RED));
            return 1;
        }
        player.getWorld().createExplosion(null, posX, posY, posZ, power, ExplosionSourceType.TNT);
        player.sendMessage(Text.translatable("message.command.explosion.success").formatted(Formatting.AQUA),true);

        return 1;
    }
}