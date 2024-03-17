package com.thatsnotm3.helpfulcommands.command.util;

import com.thatsnotm3.helpfulcommands.HelpfulCommands;
import com.thatsnotm3.helpfulcommands.command.abilities.*;
import com.thatsnotm3.helpfulcommands.command.teleportation.*;
import com.thatsnotm3.helpfulcommands.command.time.*;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import com.thatsnotm3.helpfulcommands.command.*;

import java.util.*;

public class ModCommandManager{

    public enum hcCategory{ Main, Time, Abilities, Teleportation, Uncategorized }
    public static Map<hcCategory, LinkedList<hcCommand>> commandListByCategory=new LinkedHashMap<>();

    public static class hcCommand{
        public String name="command";
        public hcCategory category=hcCategory.Uncategorized;
        public int defaultRequiredLevel=HelpfulCommands.defaultCommandLevel;
        Runnable register;
    }
    public static final List<hcCommand> commands=new ArrayList<>(){{
        add(new hcCommand(){{
            name="hc";
            category=hcCategory.Main;
            defaultRequiredLevel=0;
            register=()-> { CMD_hc.init(this); CommandRegistrationCallback.EVENT.register(CMD_hc::registerCommand); };
        }});
        add(new hcCommand(){{
            name="fly";
            category=hcCategory.Abilities;
            defaultRequiredLevel=HelpfulCommands.defaultCommandLevel;
            register=()-> { CMD_fly.init(this); CommandRegistrationCallback.EVENT.register(CMD_fly::registerCommand); };
        }});
        add(new hcCommand(){{
            name="god";
            category=hcCategory.Abilities;
            defaultRequiredLevel=HelpfulCommands.defaultCommandLevel;
            register=()-> { CMD_god.init(this); CommandRegistrationCallback.EVENT.register(CMD_god::registerCommand); };
        }});
        add(new hcCommand(){{
            name="back";
            category=hcCategory.Teleportation;
            defaultRequiredLevel=HelpfulCommands.defaultCommandLevel;
            register=()-> { CMD_back.init(this); CommandRegistrationCallback.EVENT.register(CMD_back::registerCommand); };
        }});
        add(new hcCommand(){{
            name="dimension";
            category=hcCategory.Teleportation;
            defaultRequiredLevel=HelpfulCommands.defaultCommandLevel;
            register=()-> { CMD_dimension.init(this); CommandRegistrationCallback.EVENT.register(CMD_dimension::registerCommand); };
        }});
        add(new hcCommand(){{
            name="home";
            category=hcCategory.Teleportation;
            defaultRequiredLevel=HelpfulCommands.defaultCommandLevel;
            register=()-> { CMD_home.init(this); CommandRegistrationCallback.EVENT.register(CMD_home::registerCommand); };
        }});
        add(new hcCommand(){{
            name="jump";
            category=hcCategory.Teleportation;
            defaultRequiredLevel=HelpfulCommands.defaultCommandLevel;
            register=()-> { CMD_jump.init(this); CommandRegistrationCallback.EVENT.register(CMD_jump::registerCommand); };
        }});

        add(new hcCommand(){{
            name="spawn";
            category=hcCategory.Teleportation;
            defaultRequiredLevel=HelpfulCommands.defaultCommandLevel;
            register=()-> { CMD_spawn.init(this); CommandRegistrationCallback.EVENT.register(CMD_spawn::registerCommand); };
        }});
        add(new hcCommand(){{
            name="day";
            category=hcCategory.Time;
            defaultRequiredLevel=HelpfulCommands.defaultCommandLevel;
            register=()-> { CMD_day.init(this); CommandRegistrationCallback.EVENT.register(CMD_day::registerCommand); };
        }});
        add(new hcCommand(){{
            name="night";
            category=hcCategory.Time;
            defaultRequiredLevel=HelpfulCommands.defaultCommandLevel;
            register=()-> { CMD_night.init(this); CommandRegistrationCallback.EVENT.register(CMD_night::registerCommand); };
        }});

        add(new hcCommand(){{
            name="explosion";
            category=hcCategory.Uncategorized;
            defaultRequiredLevel=HelpfulCommands.defaultCommandLevel;
            register=()-> { CMD_explosion.init(this); CommandRegistrationCallback.EVENT.register(CMD_explosion::registerCommand); };
        }});
        add(new hcCommand(){{
            name="extinguish";
            category=hcCategory.Uncategorized;
            defaultRequiredLevel=HelpfulCommands.defaultCommandLevel;
            register=()-> { CMD_extinguish.init(this); CommandRegistrationCallback.EVENT.register(CMD_extinguish::registerCommand); };
        }});
        add(new hcCommand(){{
            name="feed";
            category=hcCategory.Uncategorized;
            defaultRequiredLevel=HelpfulCommands.defaultCommandLevel;
            register=()-> { CMD_feed.init(this); CommandRegistrationCallback.EVENT.register(CMD_feed::registerCommand); };
        }});
        add(new hcCommand(){{
            name="gm";
            category=hcCategory.Uncategorized;
            defaultRequiredLevel=HelpfulCommands.defaultCommandLevel;
            register=()-> { CMD_gm.init(this); CommandRegistrationCallback.EVENT.register(CMD_gm::registerCommand); };
        }});
        add(new hcCommand(){{
            name="heal";
            category=hcCategory.Uncategorized;
            defaultRequiredLevel=HelpfulCommands.defaultCommandLevel;
            register=()-> { CMD_heal.init(this); CommandRegistrationCallback.EVENT.register(CMD_heal::registerCommand); };
        }});
        add(new hcCommand(){{
            name="ignite";
            category=hcCategory.Uncategorized;
            defaultRequiredLevel=HelpfulCommands.defaultCommandLevel;
            register=()-> { CMD_ignite.init(this); CommandRegistrationCallback.EVENT.register(CMD_ignite::registerCommand); };
        }});
        add(new hcCommand(){{
            name="killitems";
            category=hcCategory.Uncategorized;
            defaultRequiredLevel=HelpfulCommands.defaultCommandLevel;
            register=()-> { CMD_killitems.init(this); CommandRegistrationCallback.EVENT.register(CMD_killitems::registerCommand); };
        }});
        add(new hcCommand(){{
            name="lightning";
            category=hcCategory.Uncategorized;
            defaultRequiredLevel=HelpfulCommands.defaultCommandLevel;
            register=()-> { CMD_lightning.init(this); CommandRegistrationCallback.EVENT.register(CMD_lightning::registerCommand); };
        }});
        add(new hcCommand(){{
            name="rename";
            category=hcCategory.Uncategorized;
            defaultRequiredLevel=HelpfulCommands.defaultCommandLevel;
            register=()-> { CMD_rename.init(this); CommandRegistrationCallback.EVENT.register(CMD_rename::registerCommand); };
        }});
    }};

    public static void registerCommands() {
        for(hcCategory i : hcCategory.values()) commandListByCategory.put(i,new LinkedList<>());
        for(hcCommand i : commands){
            i.register.run();
            LinkedList<hcCommand> list=commandListByCategory.get(i.category);
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
        for(Map.Entry<hcCategory,LinkedList<hcCommand>> i : commandListByCategory.entrySet()){
            String character="|-";
            if(count==0) character="/-";
            if(count==commandListByCategory.size()-1) character="\\-";
            String str=character+i.getKey().name()+": ";
            for(hcCommand j : i.getValue()){
                str+="/"+j.name+", ";
            }
            if(i.getValue().isEmpty()){
                str+="(none)";
            } else str=str.substring(0, str.length()-2);
            HelpfulCommands.LOGGER.info(str);
            ++count;
        }
    }
}