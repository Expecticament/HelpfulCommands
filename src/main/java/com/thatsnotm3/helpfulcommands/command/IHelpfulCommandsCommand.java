package com.thatsnotm3.helpfulcommands.command;

import com.mojang.brigadier.CommandDispatcher;
import com.thatsnotm3.helpfulcommands.command.util.ModCommandManager;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public interface IHelpfulCommandsCommand{

    ModCommandManager.hcCommand cmd=null;

    static void init(ModCommandManager.hcCommand newData){}
    static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment){}
}
