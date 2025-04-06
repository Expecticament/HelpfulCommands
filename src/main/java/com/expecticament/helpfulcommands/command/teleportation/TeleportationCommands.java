package com.expecticament.helpfulcommands.command.teleportation;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class TeleportationCommands {

    public static class TeleportationPosition {
        private final ServerWorld world;
        private final BlockPos pos;

        public TeleportationPosition() {
            this.world = null;
            this.pos = null;
        }

        public TeleportationPosition(ServerWorld world, BlockPos pos) {
            this.world = world;
            this.pos = pos;
        }

        public ServerWorld world() {
            return world;
        }

        public BlockPos pos() {
            return pos;
        }
    }
}
