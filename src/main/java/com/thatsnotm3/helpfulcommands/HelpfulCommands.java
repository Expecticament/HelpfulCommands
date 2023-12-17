package com.thatsnotm3.helpfulcommands;

import com.thatsnotm3.helpfulcommands.command.util.ModCommandManager;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.Person;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class HelpfulCommands implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.

	public static final String modID="helpfulcommands";
	public static final String modName=FabricLoader.getInstance().getModContainer(HelpfulCommands.modID).get().getMetadata().getName();
	public static final String modDescription=FabricLoader.getInstance().getModContainer(HelpfulCommands.modID).get().getMetadata().getDescription();
	public static final String modVersion=FabricLoader.getInstance().getModContainer(HelpfulCommands.modID).get().getMetadata().getVersion().toString();
    public static final Logger LOGGER = LoggerFactory.getLogger(modID);

	public static class style {
		public static final Style simpleText=Style.EMPTY;
		public static final Style error=simpleText.withColor(Formatting.RED);
		public static final Style critical=simpleText.withColor(Formatting.DARK_RED);
		public static final Style warning=simpleText.withColor(Formatting.YELLOW);
		public static final Style success=simpleText.withColor(Formatting.GREEN);
		public static final Style primary=simpleText.withColor(Formatting.GOLD);
		public static final Style secondary=simpleText.withColor(Formatting.AQUA);
		public static final Style tertiary=simpleText.withColor(Formatting.YELLOW);
		public static final Style inactive=simpleText.withColor(Formatting.GRAY);
		public static final Style enabled=simpleText.withColor(Formatting.GREEN);
		public static final Style disabled=simpleText.withColor(Formatting.RED);
	}

	public static final int defaultCommandLevel=2;
	public static final int defaultConfigEditLevel=4;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		ModCommandManager.registerCommands();
	}
}