package net.thatsnotm3.helpfulcommands;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.thatsnotm3.helpfulcommands.command.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelpfulCommands implements ModInitializer {

	public static final String modID="helpfulcommands";
	public static final Logger LOGGER=LoggerFactory.getLogger(modID);

	@Override
	public void onInitialize(){
		RegisterCommands();
	}

	public static void RegisterCommands(){
		CommandRegistrationCallback.EVENT.register(CMD_Hc::register);
		CommandRegistrationCallback.EVENT.register(CMD_Gm::register);
		CommandRegistrationCallback.EVENT.register(CMD_Day::register);
		CommandRegistrationCallback.EVENT.register(CMD_Night::register);
		CommandRegistrationCallback.EVENT.register(CMD_Spawn::register);
		CommandRegistrationCallback.EVENT.register(CMD_Dimension::register);
		CommandRegistrationCallback.EVENT.register(CMD_Abilities::register);
		CommandRegistrationCallback.EVENT.register(CMD_Jump::register);
	}
}