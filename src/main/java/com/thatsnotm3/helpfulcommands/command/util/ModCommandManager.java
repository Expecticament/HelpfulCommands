package com.thatsnotm3.helpfulcommands.command.util;

import com.mojang.brigadier.context.CommandContext;
import com.thatsnotm3.helpfulcommands.HelpfulCommands;
import com.thatsnotm3.helpfulcommands.command.abilities.*;
import com.thatsnotm3.helpfulcommands.command.teleportation.*;
import com.thatsnotm3.helpfulcommands.command.time.*;
import com.thatsnotm3.helpfulcommands.util.ConfigManager;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import com.thatsnotm3.helpfulcommands.command.*;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.*;

public class ModCommandManager{

    public enum ModCommandCategory { Main, Abilities, Entities, Time, Teleportation, Utility, World, Uncategorized }
    public static Map<ModCommandCategory, LinkedList<ModCommand>> commandListByCategory=new LinkedHashMap<>();

    public static class ModCommand {
        public String name="command";
        public ModCommandCategory category=ModCommandCategory.Uncategorized;
        public int defaultRequiredLevel=HelpfulCommands.defaultCommandLevel;
        Runnable register;
    }
    public static final List<ModCommand> commands=new ArrayList<>(){{
        add(new ModCommand(){{
            name="hc";
            category=ModCommandCategory.Main;
            defaultRequiredLevel=0;
            register=()-> { CMD_hc.init(this); CommandRegistrationCallback.EVENT.register(CMD_hc::registerCommand); };
        }});
        add(new ModCommand(){{
            name="fly";
            category=ModCommandCategory.Abilities;
            defaultRequiredLevel=HelpfulCommands.defaultCommandLevel;
            register=()-> { CMD_fly.init(this); CommandRegistrationCallback.EVENT.register(CMD_fly::registerCommand); };
        }});
        add(new ModCommand(){{
            name="god";
            category=ModCommandCategory.Abilities;
            defaultRequiredLevel=HelpfulCommands.defaultCommandLevel;
            register=()-> { CMD_god.init(this); CommandRegistrationCallback.EVENT.register(CMD_god::registerCommand); };
        }});
        add(new ModCommand(){{
            name="back";
            category=ModCommandCategory.Teleportation;
            defaultRequiredLevel=HelpfulCommands.defaultCommandLevel;
            register=()-> { CMD_back.init(this); CommandRegistrationCallback.EVENT.register(CMD_back::registerCommand); };
        }});
        add(new ModCommand(){{
            name="dimension";
            category=ModCommandCategory.Teleportation;
            defaultRequiredLevel=HelpfulCommands.defaultCommandLevel;
            register=()-> { CMD_dimension.init(this); CommandRegistrationCallback.EVENT.register(CMD_dimension::registerCommand); };
        }});
        add(new ModCommand(){{
            name="home";
            category=ModCommandCategory.Teleportation;
            defaultRequiredLevel=HelpfulCommands.defaultCommandLevel;
            register=()-> { CMD_home.init(this); CommandRegistrationCallback.EVENT.register(CMD_home::registerCommand); };
        }});
        add(new ModCommand(){{
            name="jump";
            category=ModCommandCategory.Teleportation;
            defaultRequiredLevel=HelpfulCommands.defaultCommandLevel;
            register=()-> { CMD_jump.init(this); CommandRegistrationCallback.EVENT.register(CMD_jump::registerCommand); };
        }});

        add(new ModCommand(){{
            name="spawn";
            category=ModCommandCategory.Teleportation;
            defaultRequiredLevel=HelpfulCommands.defaultCommandLevel;
            register=()-> { CMD_spawn.init(this); CommandRegistrationCallback.EVENT.register(CMD_spawn::registerCommand); };
        }});
        add(new ModCommand(){{
            name="day";
            category=ModCommandCategory.Time;
            defaultRequiredLevel=HelpfulCommands.defaultCommandLevel;
            register=()-> { CMD_day.init(this); CommandRegistrationCallback.EVENT.register(CMD_day::registerCommand); };
        }});
        add(new ModCommand(){{
            name="night";
            category=ModCommandCategory.Time;
            defaultRequiredLevel=HelpfulCommands.defaultCommandLevel;
            register=()-> { CMD_night.init(this); CommandRegistrationCallback.EVENT.register(CMD_night::registerCommand); };
        }});

        add(new ModCommand(){{
            name="explosion";
            category=ModCommandCategory.World;
            defaultRequiredLevel=HelpfulCommands.defaultCommandLevel;
            register=()-> { CMD_explosion.init(this); CommandRegistrationCallback.EVENT.register(CMD_explosion::registerCommand); };
        }});
        add(new ModCommand(){{
            name="killitems";
            category=ModCommandCategory.World;
            defaultRequiredLevel=HelpfulCommands.defaultCommandLevel;
            register=()-> { CMD_killitems.init(this); CommandRegistrationCallback.EVENT.register(CMD_killitems::registerCommand); };
        }});
        add(new ModCommand(){{
            name="lightning";
            category=ModCommandCategory.World;
            defaultRequiredLevel=HelpfulCommands.defaultCommandLevel;
            register=()-> { CMD_lightning.init(this); CommandRegistrationCallback.EVENT.register(CMD_lightning::registerCommand); };
        }});
        add(new ModCommand(){{
            name="extinguish";
            category=ModCommandCategory.Entities;
            defaultRequiredLevel=HelpfulCommands.defaultCommandLevel;
            register=()-> { CMD_extinguish.init(this); CommandRegistrationCallback.EVENT.register(CMD_extinguish::registerCommand); };
        }});
        add(new ModCommand(){{
            name="feed";
            category=ModCommandCategory.Entities;
            defaultRequiredLevel=HelpfulCommands.defaultCommandLevel;
            register=()-> { CMD_feed.init(this); CommandRegistrationCallback.EVENT.register(CMD_feed::registerCommand); };
        }});
        add(new ModCommand(){{
            name="gm";
            category=ModCommandCategory.Entities;
            defaultRequiredLevel=HelpfulCommands.defaultCommandLevel;
            register=()-> { CMD_gm.init(this); CommandRegistrationCallback.EVENT.register(CMD_gm::registerCommand); };
        }});
        add(new ModCommand(){{
            name="heal";
            category=ModCommandCategory.Entities;
            defaultRequiredLevel=HelpfulCommands.defaultCommandLevel;
            register=()-> { CMD_heal.init(this); CommandRegistrationCallback.EVENT.register(CMD_heal::registerCommand); };
        }});
        add(new ModCommand(){{
            name="ignite";
            category=ModCommandCategory.Entities;
            defaultRequiredLevel=HelpfulCommands.defaultCommandLevel;
            register=()-> { CMD_ignite.init(this); CommandRegistrationCallback.EVENT.register(CMD_ignite::registerCommand); };
        }});
        add(new ModCommand(){{
            name="rename";
            category=ModCommandCategory.Utility;
            defaultRequiredLevel=HelpfulCommands.defaultCommandLevel;
            register=()-> { CMD_rename.init(this); CommandRegistrationCallback.EVENT.register(CMD_rename::registerCommand); };
        }});
    }};

    public static void registerCommands() {
        for(ModCommandCategory i : ModCommandCategory.values()) commandListByCategory.put(i,new LinkedList<>());
        for(ModCommand i : commands){
            i.register.run();
            LinkedList<ModCommand> list=commandListByCategory.get(i.category);
            list.add(i);
            commandListByCategory.put(i.category,list);
        }

        HelpfulCommands.LOGGER.info("H   H  CCCCC  | ");
        HelpfulCommands.LOGGER.info("H   H  C      |  Thank you for using");
        HelpfulCommands.LOGGER.info("HHHHH  C      |  "+HelpfulCommands.modName);
        HelpfulCommands.LOGGER.info("H   H  C      |  "+HelpfulCommands.modVersion);
        HelpfulCommands.LOGGER.info("H   H  CCCCC  | ");
        HelpfulCommands.LOGGER.info("<- Finished command registration ->");
        int count=0;
        for(Map.Entry<ModCommandCategory,LinkedList<ModCommand>> i : commandListByCategory.entrySet()){
            String character="|-";
            if(count==0) character="/-";
            if(count==commandListByCategory.size()-1) character="\\-";
            String str=character+i.getKey().name()+": ";
            for(ModCommand j : i.getValue()){
                str+="/"+j.name+", ";
            }
            if(i.getValue().isEmpty()){
                str+="(none)";
            } else str=str.substring(0, str.length()-2);
            HelpfulCommands.LOGGER.info(str);
            ++count;
        }
    }

    public static Boolean checkBeforeExecuting(CommandContext<ServerCommandSource> ctx, String cmdName){
        ServerCommandSource src=ctx.getSource();
        Map<String, ConfigManager.ModCommandProperties> cmdProperties=ConfigManager.loadConfig(src.getServer()).commandProperties;
        if(!cmdProperties.get(cmdName).enabled){
            src.sendError(Text.translatable("error.commandDisabled",Text.literal("/"+cmdName).setStyle(HelpfulCommands.style.primary)));
            return false;
        }
        return true;
    }
}