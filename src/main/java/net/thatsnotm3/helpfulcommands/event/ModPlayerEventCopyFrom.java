package net.thatsnotm3.helpfulcommands.event;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.thatsnotm3.helpfulcommands.util.IEntityDataSaver;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public class ModPlayerEventCopyFrom implements ServerPlayerEvents.CopyFrom{
    @Override
    public void copyFromPlayer(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) {
        IEntityDataSaver original=((IEntityDataSaver) oldPlayer);
        IEntityDataSaver player=((IEntityDataSaver) newPlayer);

        // Death Pos for /back
        BlockPos deathPos=oldPlayer.getBlockPos();
        RegistryKey<World> dimensionKey=oldPlayer.getWorld().getRegistryKey();
        String dimensionName=dimensionKey.getValue().toString();
        player.getPersistentData().putIntArray("deathPosition", new int[]{deathPos.getX(),deathPos.getY(),deathPos.getZ()});
        player.getPersistentData().putString("deathDimension", dimensionName);

        // Home Position for /home
        player.getPersistentData().putIntArray("homePosition", original.getPersistentData().getIntArray("homePosition"));   
        player.getPersistentData().putString("homeDimension",original.getPersistentData().getString("homeDimension"));
    }
}