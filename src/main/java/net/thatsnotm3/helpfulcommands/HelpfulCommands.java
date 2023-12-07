package net.thatsnotm3.helpfulcommands;

import net.fabricmc.api.ModInitializer;
import net.thatsnotm3.helpfulcommands.command.*;
import net.thatsnotm3.helpfulcommands.event.ModEventManager;
import net.thatsnotm3.helpfulcommands.gamerule.ModGameRules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelpfulCommands implements ModInitializer{

	public static final String modID="helpfulcommands";
	public static final Logger LOGGER=LoggerFactory.getLogger(modID);

	@Override
	public void onInitialize(){
		ModCommandManager.registerCommands();
		ModEventManager.registerEvents();
		ModGameRules.registerGameRules();
	}
}