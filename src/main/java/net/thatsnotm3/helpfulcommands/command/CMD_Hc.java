package net.thatsnotm3.helpfulcommands.command;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class CMD_Hc{
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment){
        dispatcher.register(CommandManager.literal("hc")
            .then(CommandManager.literal("conf")
                .then(CommandManager.literal("commands")
                    .then(CommandManager.literal("gm")
                        .then(CommandManager.literal("false"))
                        .then(CommandManager.literal("true"))
                    )
                )
            )
            .then(CommandManager.literal("commandList"))
            .then(CommandManager.literal("info"))
        );
    }
}