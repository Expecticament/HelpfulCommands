package com.expecticament.helpfulcommands.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.apache.commons.lang3.NotImplementedException;

public interface IHelpfulCommandsCommand{

    ModCommandManager.ModCommand cmd = null;

    static void init(ModCommandManager.ModCommand newData){
        throw new NotImplementedException();
    }
    static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment){
        throw new NotImplementedException();
    }
}
