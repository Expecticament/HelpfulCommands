package com.expecticament.helpfulcommands.command.main;

import com.expecticament.helpfulcommands.command.IHelpfulCommandsCommand;
import com.expecticament.helpfulcommands.command.ModCommandManager;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.expecticament.helpfulcommands.HelpfulCommands;
import com.expecticament.helpfulcommands.util.ConfigManager;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

public class CMD_hc implements IHelpfulCommandsCommand {

    public static ModCommandManager.ModCommand cmd;

    public static void init(ModCommandManager.ModCommand newData){
        cmd=newData;
    }
    public static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment){
        LiteralArgumentBuilder<ServerCommandSource> cmdManagement=CommandManager.literal("manageCommand");
        for(ModCommandManager.ModCommand i : ModCommandManager.commands){
            if(i.category==ModCommandManager.ModCommandCategory.Main) continue;
            LiteralArgumentBuilder<ServerCommandSource> literalCommand=CommandManager.literal(i.name)
                    .then(CommandManager.literal("toggleEnabled")
                            .executes(ctx-> toggleCommandState(ctx,i))
                            .then(CommandManager.argument("state", BoolArgumentType.bool())
                                    .executes(ctx -> toggleCommandState(ctx, i, BoolArgumentType.getBool(ctx, "state"))))
                    )
                    .then(CommandManager.literal("togglePublic")
                            .executes(ctx-> toggleCommandPublicState(ctx,i))
                            .then(CommandManager.argument("state", BoolArgumentType.bool())
                                    .executes(ctx -> toggleCommandPublicState(ctx, i, BoolArgumentType.getBool(ctx, "state"))))
                    );
            cmdManagement.then(literalCommand);
        }
        cmdManagement.requires(Permissions.require(HelpfulCommands.modID+".config.manageCommand",HelpfulCommands.defaultConfigEditLevel));

        LiteralArgumentBuilder<ServerCommandSource> configFieldsGet=CommandManager.literal("get");
        for(Map.Entry<String, ConfigManager.ModConfigFieldEntry> i : ConfigManager.defaultConfigFieldEntries.entrySet()){
            configFieldsGet
                    .then(CommandManager.literal(i.getKey())
                            .executes(ctx->printConfigValue(ctx,i.getKey()))
                    );
        }
        configFieldsGet.requires(Permissions.require(HelpfulCommands.modID+".config.get",HelpfulCommands.defaultConfigEditLevel));
        LiteralArgumentBuilder<ServerCommandSource> configFieldsSet=CommandManager.literal("set");
        for(Map.Entry<String, ConfigManager.ModConfigFieldEntry> i : ConfigManager.defaultConfigFieldEntries.entrySet()){
            configFieldsSet
                    .then(CommandManager.literal(i.getKey())
                            .then(i.getValue().configCommandArgument
                                    .executes(ctx->{
                                        i.getValue().context=ctx;
                                        try {
                                            editConfigEntry(ctx,i.getKey(),i.getValue().getValue.call());
                                        } catch (Exception e) {
                                            ctx.getSource().sendError(Text.literal("Error"));
                                        }
                                        return Command.SINGLE_SUCCESS;
                                    })
                            )
                    );
        }
        configFieldsSet.requires(Permissions.require(HelpfulCommands.modID+".config.set",HelpfulCommands.defaultConfigEditLevel));

        dispatcher.register(CommandManager.literal(cmd.name)
                .then(CommandManager.literal("about")
                        .executes(CMD_hc::printModInfo)
                )
                .then(CommandManager.literal("commandList")
                        .executes(CMD_hc::printCommandList)
                )
                .then(CommandManager.literal("config")
                        .then(cmdManagement)
                        .then(configFieldsSet)
                        .then(configFieldsGet)
                        .executes(CMD_hc::printModConfig)
                        .requires(Permissions.require(HelpfulCommands.modID+".config",HelpfulCommands.defaultConfigEditLevel))
                )
                .executes(CMD_hc::printModInfo)
//                .requires(Permissions.require(HelpfulCommands.modID+".command."+cmd.category.toString().toLowerCase()+"."+cmd.name,cmd.defaultRequiredLevel))
        );
    }

    private static MutableText getHeader(String translationKey,boolean isPlayer){
        MutableText ret=Text.empty().setStyle(HelpfulCommands.style.primary);
        if(isPlayer) ret.append(Text.literal("« ").formatted(Formatting.BOLD));
        else ret.append(Text.literal("<- "));
        ret
                .append(Text.literal(HelpfulCommands.modName).formatted(Formatting.BOLD))
                .append(Text.literal(" || ").setStyle(HelpfulCommands.style.secondary))
                .append(Text.translatable(translationKey).setStyle(HelpfulCommands.style.secondary))
        ;
        if(isPlayer) ret.append(Text.literal(" »").formatted(Formatting.BOLD));
        else ret.append(Text.literal(" ->"));
        ret.append("\n");

        return ret;
    }

    private static int printModInfo(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException{
        ServerCommandSource source=ctx.getSource();

        MutableText mainBlock=Text.empty().setStyle(HelpfulCommands.style.simpleText);
        mainBlock
                .append(Text.literal(" "))
                .append(Text.translatable("mod.description"))
                .append(Text.literal("\n "))
                .append(Text.translatable("mod.version",HelpfulCommands.modVersion,"Expected Predicament").setStyle(HelpfulCommands.style.inactive))
                .append(Text.literal("\n\n"))
        ;

        Style buttonStyle=HelpfulCommands.style.secondary;
        MutableText buttonCommandList=Text.empty()
                .append(Text.translatable("commandList.title"))
                .setStyle(buttonStyle
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/hc commandList"))
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.translatable("about.commandList.tooltip")))
                );
        MutableText buttonConfig=Text.empty()
                .append(Text.translatable("config.title"))
                .setStyle(buttonStyle
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/hc config"))
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.translatable("about.config.tooltip")))
                );
        MutableText buttonDocumentation=Text.translatable("about.documentation").setStyle(buttonStyle
                .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://expecticament.github.io/HelpfulCommands/"))
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.translatable("about.documentation.tooltip")))
        );
        MutableText buttonGitHub=Text.literal("GitHub").setStyle(buttonStyle
                .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/Expecticament/HelpfulCommands"))
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.translatable("about.github.tooltip")))
        );
        MutableText buttonModrinth=Text.literal("Modrinth").setStyle(buttonStyle
                .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://modrinth.com/mod/helpfulcommands"))
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.translatable("about.modrinth.tooltip")))
        );
        MutableText buttonDiscord=Text.literal("Discord").setStyle(buttonStyle
                .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/RHd8P5hps4"))
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.translatable("about.discord.tooltip")))
        );
        MutableText buttons=Text.empty();
        if(ctx.getSource().isExecutedByPlayer()) {
            buttons
                    .append(Text.literal("〚"))
                    .append(buttonCommandList)
                    .append(Text.literal(" • "))
                    .append(buttonConfig)
                    .append(Text.literal("〛"))
                    .append(Text.literal("\n〚"))
                    .append(buttonDocumentation)
                    .append(Text.literal(" • "))
                    .append(buttonGitHub)
                    .append(Text.literal(" • "))
                    .append(buttonModrinth)
                    .append(Text.literal(" • "))
                    .append(buttonDiscord)
                    .append(Text.literal("〛"))
            ;
        } else{
            buttons
                    .append(buttonCommandList)
                    .append(Text.literal(": /hc commandList\n"))
                    .append(buttonConfig)
                    .append(Text.literal(": /hc config\n"))
                    .append(buttonGitHub)
                    .append(Text.literal(": https://github.com/Expecticament/HelpfulCommands \n"))
                    .append(buttonModrinth)
                    .append(Text.literal(": https://modrinth.com/mod/helpfulcommands \n"))
            ;
        }

        MutableText msg=Text.empty()
                .append(Text.literal("\n"))
                .append(getHeader("about.title",ctx.getSource().isExecutedByPlayer()))
                .append(mainBlock)
                .append(buttons)
        ;
        source.sendMessage(msg);

        return Command.SINGLE_SUCCESS;
    }

    private static int printCommandList(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException{
        ServerCommandSource source=ctx.getSource();

        boolean executedByPlayer=ctx.getSource().isExecutedByPlayer();

        MutableText header=Text.empty().setStyle(HelpfulCommands.style.simpleText);
        if(executedByPlayer) header.append(Text.literal("[• "));
        boolean hasPerms=Permissions.check(source,HelpfulCommands.modID+".config.manageCommand",HelpfulCommands.defaultConfigEditLevel); // Determine whether we should print "Enabled/Disabled"(true) or "You can/can't use"(false)
        if(hasPerms){
            MutableText enabled=Text.translatable("commandList.command.enabled").setStyle(HelpfulCommands.style.enabled);
            MutableText disabled=Text.translatable("commandList.command.disabled").setStyle(HelpfulCommands.style.disabled);
            if(executedByPlayer){
                header
                        .append(enabled)
                        .append(Text.literal("/"))
                        .append(disabled)
                ;
            } else{
                header
                        .append(Text.literal("[+]"))
                        .append(enabled)
                        .append(" / [-]")
                        .append(disabled)
                ;
            }
        } else{
            header
                    .append(Text.translatable("commandList.command.usage.you"))
                    .append(Text.literal(" "))
                    .append(Text.translatable("commandList.command.usage.can").setStyle(HelpfulCommands.style.enabled))
                    .append(Text.literal("/"))
                    .append(Text.translatable("commandList.command.usage.cant").setStyle(HelpfulCommands.style.disabled))
                    .append(Text.literal(" "))
                    .append(Text.translatable("commandList.command.usage.use"))
            ;
        }
        if(executedByPlayer) header.append(Text.literal(" •]"));

        MutableText commandList=Text.empty();
        Style charsStyle=HelpfulCommands.style.tertiary;

        ConfigManager.ModConfig cfg=ConfigManager.loadConfig(ctx.getSource().getServer());

        for(Map.Entry<ModCommandManager.ModCommandCategory, LinkedList<ModCommandManager.ModCommand>> i : ModCommandManager.commandListByCategory.entrySet()){
            if(i.getValue().isEmpty()) continue;
            commandList
                    .append(Text.literal("\n"))
                    .append(Text.literal("┏").setStyle(charsStyle))
                    .append(Text.translatable("commands.category."+i.getKey().name().toLowerCase()).setStyle(HelpfulCommands.style.tertiary))
            ;

            for(ModCommandManager.ModCommand j : i.getValue()){
                Style descriptionStyle=HelpfulCommands.style.inactive;
                if(hasPerms) descriptionStyle=getSuggestCommandStyle(j.name).withColor(HelpfulCommands.style.inactive.getColor());

                String chars="┠";
                if(j==i.getValue().getLast()) chars="┗";
                commandList
                        .append(Text.literal("\n"+chars+"› ").setStyle(charsStyle))
                        .append(Text.literal(j.name).setStyle(getCommandNameStyle(j,source,cfg,hasPerms)))
                        .append(Text.literal(": ").append(Text.translatable("commands."+j.name+".description").setStyle(descriptionStyle)))
                ;
            }
        }

        MutableText msg=Text.empty()
                .append(Text.literal("\n"))
                .append(getHeader("commandList.title",ctx.getSource().isExecutedByPlayer()))
                .append(header)
                .append(Text.literal("\n"))
                .append(commandList)
        ;
        source.sendMessage(msg);

        return Command.SINGLE_SUCCESS;
    }

    private static Style getCommandNameStyle(ModCommandManager.ModCommand command, ServerCommandSource src, ConfigManager.ModConfig cfg, boolean hasConfigPerms){
        Style ret;

        boolean commandState=true;
        if(command.category!=ModCommandManager.ModCommandCategory.Main){
            commandState=cfg.commands.getOrDefault(command.name,new ConfigManager.ModConfigCommandEntry()).isEnabled;
        }

        if(hasConfigPerms){
            ret = commandState ? HelpfulCommands.style.enabled : HelpfulCommands.style.disabled;
            if(command.category!=ModCommandManager.ModCommandCategory.Main){
                ret=ret
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.translatable("tooltips.clickToToggleCommand",Text.literal("/"+command.name).setStyle(HelpfulCommands.style.tertiary))))
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/hc config manageCommand "+command.name+" toggleEnabled"));
            }
        } else{
            Style suggestCommand=getSuggestCommandStyle(command.name);
            ret=suggestCommand.withColor(HelpfulCommands.style.enabled.getColor());
            MutableText txt=ModCommandManager.getCantUseCommandReason(src,command);
            if(txt!=null){
                ret=HelpfulCommands.style.error.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,txt));
            }
        }
        return ret;
    }

    private static Style getSuggestCommandStyle(String commandName){
        return Style.EMPTY
                .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,"/"+commandName+" "))
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.translatable("tooltips.clickToSuggestCommand",Text.literal("/"+commandName).setStyle(HelpfulCommands.style.tertiary))))
        ;
    }

    private static int printModConfig(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException{

        return Command.SINGLE_SUCCESS;
    }

    private static int editConfigEntry(CommandContext<ServerCommandSource> ctx, String entry, Object value) throws CommandSyntaxException{
        ServerCommandSource src=ctx.getSource();

        ConfigManager.ModConfig cfg=ConfigManager.loadConfig(src.getServer());
        if(!cfg.fields.containsKey(entry)){
            src.sendError(Text.translatable("commands.hc.config.setValue.error.unknownValue",Text.literal(entry).setStyle(HelpfulCommands.style.primary)));
            return -1;
        }
        cfg.fields.put(entry,value);
        ConfigManager.saveConfig(cfg,src.getServer());

        src.sendFeedback(()->Text.translatable("commands.hc.config.setValue.success",Text.literal(entry).setStyle(HelpfulCommands.style.primary),Text.literal(String.valueOf(value)).setStyle(HelpfulCommands.style.primary)).setStyle(HelpfulCommands.style.success),true);

        ModCommandManager.sendCommandTreeToEveryone(src);

        return Command.SINGLE_SUCCESS;
    }
    private static int printConfigValue(CommandContext<ServerCommandSource> ctx, String entry){
        ServerCommandSource src=ctx.getSource();

        ConfigManager.ModConfig cfg=ConfigManager.loadConfig(src.getServer());
        if(!cfg.fields.containsKey(entry)){
            src.sendError(Text.translatable("commands.hc.config.getValue.error.unknownValue",Text.literal(entry).setStyle(HelpfulCommands.style.primary)));
            return -1;
        }

        src.sendMessage(Text.translatable("commands.hc.config.getValue.success",Text.literal(entry).setStyle(HelpfulCommands.style.primary),Text.literal(String.valueOf(cfg.fields.get(entry))).setStyle(HelpfulCommands.style.primary)).setStyle(HelpfulCommands.style.secondary));

        return Command.SINGLE_SUCCESS;
    }

    private static int toggleCommandState(CommandContext<ServerCommandSource> ctx, ModCommandManager.ModCommand cmd){
        ServerCommandSource src=ctx.getSource();

        ConfigManager.ModConfig cfg=ConfigManager.loadConfig(src.getServer());
        return toggleCommandState(ctx,cmd,!cfg.commands.getOrDefault(cmd.name,new ConfigManager.ModConfigCommandEntry()).isEnabled);
    }
    private static int toggleCommandState(CommandContext<ServerCommandSource> ctx, ModCommandManager.ModCommand cmd, Boolean value){
        ServerCommandSource src=ctx.getSource();

        ConfigManager.ModConfig cfg=ConfigManager.loadConfig(src.getServer());
        if(cmd==null){
            src.sendError(Text.translatable("commands.hc.config.manageCommand.toggleEnabled.error.unknownCommand"));
            return -1;
        }

        cfg.commands.get(cmd.name).isEnabled=value;
        ConfigManager.saveConfig(cfg,src.getServer());

        Style messageStyle=value ? HelpfulCommands.style.enabled : HelpfulCommands.style.disabled;
        Style commandNameStyle=value ? HelpfulCommands.style.primary.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.translatable("tooltips.clickToSuggestThisCommand"))).withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,"/"+cmd.name)) : HelpfulCommands.style.primary;
        src.sendFeedback(()->Text.translatable("commands.hc.config.manageCommand.toggleEnabled.success."+String.valueOf(value).toLowerCase(),Text.literal("/"+cmd.name).setStyle(commandNameStyle)).setStyle(messageStyle),true);

        ModCommandManager.sendCommandTreeToEveryone(src);

        return Command.SINGLE_SUCCESS;
    }

    private static int toggleCommandPublicState(CommandContext<ServerCommandSource> ctx, ModCommandManager.ModCommand cmd){
        ServerCommandSource src=ctx.getSource();

        ConfigManager.ModConfig cfg=ConfigManager.loadConfig(src.getServer());
        return toggleCommandPublicState(ctx,cmd,!cfg.commands.getOrDefault(cmd.name,new ConfigManager.ModConfigCommandEntry()).isPublic);
    }
    private static int toggleCommandPublicState(CommandContext<ServerCommandSource> ctx, ModCommandManager.ModCommand cmd, Boolean value){
        ServerCommandSource src=ctx.getSource();

        ConfigManager.ModConfig cfg=ConfigManager.loadConfig(src.getServer());
        if(cmd==null){
            src.sendError(Text.translatable("commands.hc.config.manageCommand.togglePublic.error.unknownCommand"));
            return -1;
        }

        cfg.commands.get(cmd.name).isPublic=value;
        ConfigManager.saveConfig(cfg,src.getServer());

        Style messageStyle=HelpfulCommands.style.success;
        Style commandNameStyle=HelpfulCommands.style.primary.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.translatable("tooltips.clickToSuggestThisCommand"))).withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,"/"+cmd.name));
        src.sendFeedback(()->Text.translatable("commands.hc.config.manageCommand.togglePublic.success."+String.valueOf(value).toLowerCase(),Text.literal("/"+cmd.name).setStyle(commandNameStyle)).setStyle(messageStyle),true);

        ModCommandManager.sendCommandTreeToEveryone(src);

        return Command.SINGLE_SUCCESS;
    }
}