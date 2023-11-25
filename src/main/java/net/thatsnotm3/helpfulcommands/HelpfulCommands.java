package net.thatsnotm3.helpfulcommands;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.thatsnotm3.helpfulcommands.command.*;
import net.thatsnotm3.helpfulcommands.gamerule.ModGameRules;
import net.thatsnotm3.helpfulcommands.event.ModPlayerEventCopyFrom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelpfulCommands implements ModInitializer{

	public static final String modID="helpfulcommands";
	public static final Logger LOGGER=LoggerFactory.getLogger(modID);

	@Override
	public void onInitialize(){
		ModCommandManager.registerCommands();
		registerEvents();
		ModGameRules.registerGameRules();
	}

	public static void registerEvents(){
		ServerPlayerEvents.COPY_FROM.register(new ModPlayerEventCopyFrom());
	}
}