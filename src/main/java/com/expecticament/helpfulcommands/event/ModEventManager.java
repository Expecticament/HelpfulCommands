package com.expecticament.helpfulcommands.event;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;

public class ModEventManager{
    public static void registerEvents(){
        ServerPlayerEvents.COPY_FROM.register(new ModPlayerEventCopyFrom());
    }
}