package com.thatsnotm3.helpfulcommands.util;

import net.minecraft.nbt.NbtCompound;

public interface IEntityDataSaver{
    NbtCompound getPersistentData();
}