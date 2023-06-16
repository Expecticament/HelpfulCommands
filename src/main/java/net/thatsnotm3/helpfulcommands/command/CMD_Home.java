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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public class CMD_Home{
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment){
        dispatcher.register(CommandManager.literal("home")
            .then(CommandManager.literal("set").executes(CMD_Home::setHome))
            .then(CommandManager.literal("get").executes(CMD_Home::getHome))
            .then(CommandManager.literal("tp").executes(CMD_Home::returnHome))
            .executes(CMD_Home::returnHome)
        );
    }

    public static int returnHome(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException{
        IEntityDataSaver playerData=(IEntityDataSaver)ctx.getSource().getPlayer();
        ServerPlayerEntity player=ctx.getSource().getPlayer();

        if(!net.thatsnotm3.helpfulcommands.command.CommandManager.RunChecks("home",player)) return -1;

        int[] homePos=playerData.getPersistentData().getIntArray("homePosition");
        String dimensionName=playerData.getPersistentData().getString("homeDimension");
        if(homePos.length!=0){
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
            player.teleport(dimension,homePos[0],homePos[1],homePos[2],player.getYaw(),player.getPitch());
            player.sendMessage(Text.literal("Teleported you to \u00A7byour Home Position"));
        } else{
            player.sendMessage(Text.literal("\u00A7cHome Position is not set in this world!"));
        }

        return 1;
    }

    public static int setHome(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException{
        IEntityDataSaver playerData=(IEntityDataSaver)ctx.getSource().getPlayer();
        ServerPlayerEntity player=ctx.getSource().getPlayer();

        if(!net.thatsnotm3.helpfulcommands.command.CommandManager.RunChecks("home",player)) return -1;

        BlockPos playerPos=player.getBlockPos();
        playerData.getPersistentData().putIntArray("homePosition",new int[]{playerPos.getX(),playerPos.getY(),playerPos.getZ()});
        RegistryKey<World> dimensionKey=player.getWorld().getRegistryKey();
        String dimensionName=dimensionKey.getValue().toString();
        playerData.getPersistentData().putString("homeDimension", dimensionName);
        player.sendMessage(Text.literal("\u00A7aUpdated your Home Position"));

        return 1;
    }

    public static int getHome(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException{
        IEntityDataSaver playerData=(IEntityDataSaver)ctx.getSource().getPlayer();
        ServerPlayerEntity player=ctx.getSource().getPlayer();

        if(!net.thatsnotm3.helpfulcommands.command.CommandManager.RunChecks("home",player)) return -1;

        int[] homePos=playerData.getPersistentData().getIntArray("homePosition");
        String dimensionName=playerData.getPersistentData().getString("homeDimension");
        if(homePos.length!=0){
            player.sendMessage(Text.literal("\u00A7bYour Home Position coordinates:\u00A7r\nX: \u00A76"+homePos[0]+"\u00A7r\nY: \u00A76"+homePos[1]+"\u00A7r\nZ: \u00A76"+homePos[2]+"\u00A7r\nDimension: \u00A76"+dimensionName));
        } else{
            player.sendMessage(Text.literal("\u00A7cHome Position is not set in this world!"));
        }

        return 1;
    }
}