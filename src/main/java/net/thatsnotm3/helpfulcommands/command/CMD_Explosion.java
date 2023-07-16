package net.thatsnotm3.helpfulcommands.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World.ExplosionSourceType;
import net.thatsnotm3.helpfulcommands.gamerule.ModGameRules;

public class CMD_Explosion{
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment){
        dispatcher.register(CommandManager.literal("explosion")
            .then(CommandManager.argument("distance", FloatArgumentType.floatArg())
            .then(CommandManager.argument("power", FloatArgumentType.floatArg()).executes(ctx -> run(ctx, FloatArgumentType.getFloat(ctx, "distance"),FloatArgumentType.getFloat(ctx, "power")))))
        );
    }

    public static int run(CommandContext<ServerCommandSource> ctx,float distance,float power) throws CommandSyntaxException{
        ServerPlayerEntity player=ctx.getSource().getPlayer();

        if(!ModCommandManager.RunChecks("explosion",player)) return -1;

        double posX=player.getX()+player.getRotationVector().getX()*distance;
        double posY=player.getY()+player.getEyeHeight(player.getPose())+player.getRotationVector().getY()*distance;
        double posZ=player.getZ()+player.getRotationVector().getZ()*distance;
        if(power>75) if(!player.getWorld().getGameRules().getBoolean(ModGameRules.HC_UNCAP_EXPLOSION_POWER)){
            player.sendMessage(Text.literal("\u00A7cExplosions this powerful ( >75 ) require \u00A76\"hcUncapExplosionPower\" \u00A7cgame rule to be enabled!"));
            return 1;
        }
        player.getWorld().createExplosion(null, posX, posY, posZ, power, ExplosionSourceType.TNT);
        player.sendMessage(Text.literal("Created an explosion"),true);

        return 1;
    }
}