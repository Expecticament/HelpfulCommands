package net.thatsnotm3.helpfulcommands.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.thatsnotm3.helpfulcommands.util.IEntityDataSaver;

public class CMD_Back{
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment){
        dispatcher.register(CommandManager.literal("back")
            .then(CommandManager.literal("get").executes(CMD_Back::getPos))
            .then(CommandManager.literal("tp").executes(CMD_Back::returnToPos))
            .executes(CMD_Back::returnToPos)
        );
    }

    public static int returnToPos(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException{
        IEntityDataSaver playerData=(IEntityDataSaver)ctx.getSource().getPlayer();
        ServerPlayerEntity player=ctx.getSource().getPlayer();

        if(!ModCommandManager.RunChecks("back",player)) return -1;

        int[] deathPos=playerData.getPersistentData().getIntArray("deathPosition");
        String dimensionName=playerData.getPersistentData().getString("deathDimension");
        if(deathPos.length!=0){
            // TODO: Support for modded dimensions
            ServerWorld dimension=null;
            switch(dimensionName){
                case "minecraft:overworld":
                    dimension=player.getServer().getWorld(ServerWorld.OVERWORLD);
                    break;
                case "minecraft:the_end":
                    dimension=player.getServer().getWorld(ServerWorld.END);
                    break;
                case "minecraft:the_nether":
                    dimension=player.getServer().getWorld(ServerWorld.NETHER);
                    break;
            }
            if(dimension==null){
                player.sendMessage(Text.literal("\u00A7cUnknown Dimension!"));
                return 1;
            }
            player.teleport(dimension,deathPos[0],deathPos[1],deathPos[2],player.getYaw(),player.getPitch());
            player.sendMessage(Text.literal("Teleported you to \u00A7byour Death Position"));
        } else{
            player.sendMessage(Text.literal("\u00A7cNo Death Position saved yet!"));
        }

        return 1;
    }

    public static int getPos(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException{
        IEntityDataSaver playerData=(IEntityDataSaver)ctx.getSource().getPlayer();
        ServerPlayerEntity player=ctx.getSource().getPlayer();

        if(!ModCommandManager.RunChecks("back",player)) return -1;

        int[] deathPos=playerData.getPersistentData().getIntArray("deathPosition");
        String dimensionName=playerData.getPersistentData().getString("deathDimension");
        if(deathPos.length!=0){
            player.sendMessage(Text.literal("\u00A7bYour Death Position coordinates:\u00A7r\nX: \u00A76"+deathPos[0]+"\u00A7r\nY: \u00A76"+deathPos[1]+"\u00A7r\nZ: \u00A76"+deathPos[2]+"\u00A7r\nDimension: \u00A76"+dimensionName));
        } else{
            player.sendMessage(Text.literal("\u00A7cNo Death Position saved yet!"));
        }

        return 1;
    }
}