package com.expecticament.helpfulcommands.util;

import net.minecraft.nbt.NbtCompound;

public interface IEntityDataSaver{
    NbtCompound getPersistentData();
}