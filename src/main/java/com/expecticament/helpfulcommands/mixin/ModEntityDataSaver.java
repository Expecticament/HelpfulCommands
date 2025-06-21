package com.expecticament.helpfulcommands.mixin;

import com.expecticament.helpfulcommands.HelpfulCommands;
import com.expecticament.helpfulcommands.util.IEntityDataSaver;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class ModEntityDataSaver implements IEntityDataSaver {
    private NbtCompound persistentData;

    @Override
    public NbtCompound getPersistentData() {
        if(this.persistentData == null){
            this.persistentData = new NbtCompound();
        }

        return persistentData;
    }

    @Inject(method="writeData", at=@At("HEAD"))
    protected void injectWriteMethod(WriteView writeView, CallbackInfo ci) {
        if(persistentData != null){
            writeView.put(HelpfulCommands.modID + ".data", NbtCompound.CODEC, persistentData);
        }
    }

    @Inject(method = "readData", at = @At("HEAD"))
    protected void injectReadMethod(ReadView readView, CallbackInfo ci) {
        readView.read(HelpfulCommands.modID + ".data", NbtCompound.CODEC).ifPresent(tag -> persistentData = tag);
    }
}