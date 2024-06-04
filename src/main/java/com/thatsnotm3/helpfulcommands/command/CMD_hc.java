package com.thatsnotm3.helpfulcommands.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.thatsnotm3.helpfulcommands.HelpfulCommands;
import com.thatsnotm3.helpfulcommands.command.util.ModCommandManager;
import com.thatsnotm3.helpfulcommands.util.ConfigManager;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;

import java.util.LinkedList;
import java.util.Map;

public class CMD_hc implements IHelpfulCommandsCommand {

    public static ModCommandManager.ModCommand cmd;

    public static void init(ModCommandManager.ModCommand newData){
        cmd=newData;
    }
    public static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment){
        dispatcher.register(CommandManager.literal(cmd.name)
                .then(CommandManager.literal("about")
                        .executes(CMD_hc::printModInfo)
                )
                .then(CommandManager.literal("commandList")
                        .executes(CMD_hc::printCommandList)
                )
                .then(CommandManager.literal("config")
                        .then(CommandManager.literal("toggleCommand")
                                .requires(Permissions.require(HelpfulCommands.modID+".config.toggleCommand",HelpfulCommands.defaultConfigEditLevel))
                        )
                        .then(CommandManager.literal("set")
                                .then(CommandManager.literal("explosionPowerLimit")
                                        .then(CommandManager.argument("value", IntegerArgumentType.integer(1))
                                                .executes(ctx->{
                                                    int newValue=IntegerArgumentType.getInteger(ctx,"value");

                                                    ConfigManager.ModConfig cfg=ConfigManager.loadConfig(ctx.getSource().getServer());
                                                    cfg.explosionPowerLimit=newValue;

                                                    configValueChanged(ctx,cfg,"explosionPowerLimit",String.valueOf(newValue));
                                                    return Command.SINGLE_SUCCESS;
                                                })
                                        )
                                )
                                .requires(Permissions.require(HelpfulCommands.modID+".config.set",HelpfulCommands.defaultConfigEditLevel))
                        )
                        .then(CommandManager.literal("get")
                                .then(CommandManager.literal("explosionPowerLimit")
                                        .executes(ctx->printConfigValue(ctx,"explosionPowerLimit"))
                                )
                                .requires(Permissions.require(HelpfulCommands.modID+".config.get",HelpfulCommands.defaultConfigEditLevel))
                        )
                        .executes(CMD_hc::printModConfig)
                        .requires(Permissions.require(HelpfulCommands.modID+".config",HelpfulCommands.defaultConfigEditLevel))
                )
                .executes(CMD_hc::printModInfo)
                .requires(Permissions.require(HelpfulCommands.modID+".command."+cmd.category.toString().toLowerCase()+"."+cmd.name,cmd.defaultRequiredLevel))
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
                .append(Text.translatable("mod.version",HelpfulCommands.modVersion,"ThatsNotM3").setStyle(HelpfulCommands.style.inactive))
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
        MutableText buttonWiki=Text.translatable("about.wiki").setStyle(buttonStyle
                .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://thatsnotm3.github.io/HelpfulCommands/"))
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.translatable("about.wiki.tooltip")))
        );
        MutableText buttonGitHub=Text.literal("GitHub").setStyle(buttonStyle
                .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/ThatsNotM3/HelpfulCommands"))
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.translatable("about.github.tooltip")))
        );
        MutableText buttonModrinth=Text.literal("Modrinth").setStyle(buttonStyle
                .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://modrinth.com/mod/helpfulcommands"))
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.translatable("about.modrinth.tooltip")))
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
                    .append(buttonWiki)
                    .append(Text.literal(" • "))
                    .append(buttonGitHub)
                    .append(Text.literal(" • "))
                    .append(buttonModrinth)
                    .append(Text.literal(" • "))
            ;
        } else{
            buttons
                    .append(buttonCommandList)
                    .append(Text.literal(": /hc commandList\n"))
                    .append(buttonConfig)
                    .append(Text.literal(": /hc config\n"))
                    .append(buttonGitHub)
                    .append(Text.literal(": https://github.com/ThatsNotM3/HelpfulCommands \n"))
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
        boolean hasPerms=Permissions.check(source,HelpfulCommands.modID+".config.toggleCommand",HelpfulCommands.defaultConfigEditLevel); // Determine whether we should print "Enabled/Disabled"(true) or "You can/can't use"(false)
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
                        .append(Text.literal(j.name).setStyle(getCommandNameStyle(j,source,hasPerms)))
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

    private static Style getCommandNameStyle(ModCommandManager.ModCommand command, ServerCommandSource src, boolean hasPerms){
        Style ret;

        Style suggestCommand=getSuggestCommandStyle(command.name);

        if(hasPerms){
            ret=HelpfulCommands.style.enabled;
        } else{
            ret=Permissions.check(src,HelpfulCommands.modID+".command."+command.category.toString().toLowerCase()+"."+command.name, command.defaultRequiredLevel) ? suggestCommand.withColor(HelpfulCommands.style.enabled.getColor())
                    : HelpfulCommands.style.disabled.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.translatable("error.notAllowed",Text.literal("/"+command.name).setStyle(HelpfulCommands.style.tertiary)).setStyle(HelpfulCommands.style.error)));
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

    private static int configValueChanged(CommandContext<ServerCommandSource> ctx, ConfigManager.ModConfig cfg, String name, String newValue) throws CommandSyntaxException{
        ServerCommandSource src=ctx.getSource();

        ConfigManager.saveConfig(cfg,src.getServer());
        src.sendFeedback(()->Text.translatable("commands.hc.config.setValue",Text.literal(name).setStyle(HelpfulCommands.style.primary),Text.literal(newValue).setStyle(HelpfulCommands.style.primary)).setStyle(HelpfulCommands.style.success),true);

        return Command.SINGLE_SUCCESS;
    }
    private static int printConfigValue(CommandContext<ServerCommandSource> ctx, String key){
        ServerCommandSource src=ctx.getSource();

        String value="(unknown)";
        ConfigManager.ModConfig cfg=ConfigManager.loadConfig(src.getServer());

        switch(key){
            case "explosionPowerLimit":
                value=String.valueOf(cfg.explosionPowerLimit);
        }

        src.sendMessage(Text.translatable("commands.hc.config.getValue",Text.literal(key).setStyle(HelpfulCommands.style.primary),Text.literal(value).setStyle(HelpfulCommands.style.primary)).setStyle(HelpfulCommands.style.secondary));

        return Command.SINGLE_SUCCESS;
    }
}