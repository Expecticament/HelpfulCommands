package net.thatsnotm3.helpfulcommands;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.thatsnotm3.helpfulcommands.command.*;
import net.thatsnotm3.helpfulcommands.gamerule.ModGameRules;
import net.thatsnotm3.helpfulcommands.util.ConfigUtils;
import net.thatsnotm3.helpfulcommands.event.ModPlayerEventCopyFrom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelpfulCommands implements ModInitializer{

	public static final String modID="helpfulcommands";
	public static final Logger LOGGER=LoggerFactory.getLogger(modID);

	@Override
	public void onInitialize(){
		registerCommands();
		registerEvents();
		ModGameRules.registerGameRules();

		ConfigUtils.initialize();
	}

	public static void registerCommands(){
		CommandRegistrationCallback.EVENT.register(CMD_Hc::register);
		CommandRegistrationCallback.EVENT.register(CMD_Gm::register);
		CommandRegistrationCallback.EVENT.register(CMD_Day::register);
		CommandRegistrationCallback.EVENT.register(CMD_Night::register);
		CommandRegistrationCallback.EVENT.register(CMD_Spawn::register);
		CommandRegistrationCallback.EVENT.register(CMD_Dimension::register);
		CommandRegistrationCallback.EVENT.register(CMD_Abilities::register);
		CommandRegistrationCallback.EVENT.register(CMD_Jump::register);
		CommandRegistrationCallback.EVENT.register(CMD_Explosion::register);
		CommandRegistrationCallback.EVENT.register(CMD_Lightning::register);
		CommandRegistrationCallback.EVENT.register(CMD_Killitems::register);
		CommandRegistrationCallback.EVENT.register(CMD_Home::register);
		CommandRegistrationCallback.EVENT.register(CMD_Back::register);
		CommandRegistrationCallback.EVENT.register(CMD_Feed::register);
		CommandRegistrationCallback.EVENT.register(CMD_Heal::register);
		CommandRegistrationCallback.EVENT.register(CMD_Extinguish::register);
	}

	public static void registerEvents(){
		ServerPlayerEvents.COPY_FROM.register(new ModPlayerEventCopyFrom());
	}
}