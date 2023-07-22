package net.thatsnotm3.helpfulcommands.command;

import java.util.Map;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.thatsnotm3.helpfulcommands.HelpfulCommands;
import net.thatsnotm3.helpfulcommands.util.ConfigManager;

public class CMD_Hc{

    static final String cmdName="hc";

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment){
        LiteralArgumentBuilder<ServerCommandSource> commands=CommandManager.literal("commands");
        for(String i : ModCommandManager.commands){
            LiteralArgumentBuilder<ServerCommandSource> literalCommand=CommandManager.literal(i)
                    .then(CommandManager.literal("toggle")
                            .executes(ctx->toggleCommand(ctx,i))
                            .then(CommandManager.argument("state", BoolArgumentType.bool())
                            .executes(ctx -> toggleCommand(ctx, i, BoolArgumentType.getBool(ctx, "state"))))
                    )
                    .then(CommandManager.literal("opLevel")
                        .then(CommandManager.argument("value", IntegerArgumentType.integer(0, 4))
                        .executes(ctx->changeCommandOpLevel(ctx, i, IntegerArgumentType.getInteger(ctx, "value"))))
                    );
            commands.then(literalCommand);
        }
        commands.executes(CMD_Hc::cmdList);
        
        dispatcher.register(CommandManager.literal(cmdName)
            .then(CommandManager.literal("info").executes(CMD_Hc::info))
            .then(commands)
            .then(CommandManager.literal("config")
                .executes(ctx->printConfig(ctx))
                .then(CommandManager.literal("configOPLevel").executes(ctx->printConfigValue("configOPLevel",ctx.getSource().getPlayer())).then(CommandManager.argument("value",IntegerArgumentType.integer(0,4)).executes(ctx->changeConfigValue("configOPLevel",IntegerArgumentType.getInteger(ctx,"value"),ctx.getSource().getPlayer()))))
                .then(CommandManager.literal("explosionPowerLimit").executes(ctx->printConfigValue("explosionPowerLimit",ctx.getSource().getPlayer())).then(CommandManager.argument("value",IntegerArgumentType.integer(0)).executes(ctx->changeConfigValue("explosionPowerLimit",IntegerArgumentType.getInteger(ctx,"value"),ctx.getSource().getPlayer()))))
            )
            .executes(CMD_Hc::info)
        );
    }

    static int toggleCommand(CommandContext<ServerCommandSource> ctx,String cmd) throws CommandSyntaxException{
        toggleCommand(ctx,cmd,!getCommandState(cmd,ctx.getSource().getServer()));
        return 1;
    }
    public static int toggleCommand(CommandContext<ServerCommandSource> ctx,String cmd,Boolean state) throws CommandSyntaxException{
        ServerPlayerEntity player=ctx.getSource().getPlayer();

        ConfigManager.ModConfig cfg=ConfigManager.loadConfig(player.getServer());

        if(!player.hasPermissionLevel(cfg.configOPLevel)){
            ModCommandManager.sendConfigEditingInsufficientPrivilegesMessage(player);
            return -1;
        }

        Map<String, ConfigManager.ModCommandProperties> cmdProperties = cfg.commandProperties;
        ConfigManager.ModCommandProperties cmdInQuestion = cmdProperties.getOrDefault(cmd, new ConfigManager.ModCommandProperties());
        cmdInQuestion.enabled = state;
        cmdProperties.put(cmd, cmdInQuestion);
        cfg.commandProperties = cmdProperties;
        ConfigManager.saveConfig(cfg, player.getServer());

        MutableText msg;
        if(state){
            Style textStyle=Style.EMPTY.withFormatting(Formatting.GREEN);
            Style clickableCommandStyle=Style.EMPTY
                    .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,"/"+cmd+" "))
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.translatable("tooltip.highlight.hc.commands.suggest",cmd)))
                    .withFormatting(Formatting.GOLD)
                    ;
            msg=Text.translatable("message.command.enabled",Text.literal("/"+cmd).setStyle(clickableCommandStyle)).setStyle(textStyle);
        } else{
            Style textStyle=Style.EMPTY.withFormatting(Formatting.RED);
            Style commandStyle=Style.EMPTY.withFormatting(Formatting.GOLD);
            msg=Text.translatable("message.command.disabled",Text.literal("/"+cmd).setStyle(commandStyle)).setStyle(textStyle);
        }
        player.sendMessage(msg);

        return 1;
    }
    public static int changeCommandOpLevel(CommandContext<ServerCommandSource> ctx,String cmd,int level){
        ServerPlayerEntity player=ctx.getSource().getPlayer();

        ConfigManager.ModConfig cfg=ConfigManager.loadConfig(player.getServer());

        if(!player.hasPermissionLevel(cfg.configOPLevel)){
            ModCommandManager.sendConfigEditingInsufficientPrivilegesMessage(player);
            return -1;
        }

        ConfigManager.ModCommandProperties cmdInQuestion=cfg.commandProperties.getOrDefault(cmd,new ConfigManager.ModCommandProperties());

        if(cmdInQuestion.opLevel==level) return 1;
        cmdInQuestion.opLevel=level;
        cfg.commandProperties.put(cmd,cmdInQuestion);
        ConfigManager.saveConfig(cfg,player.getServer());

        ClickEvent ce=new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,"/"+cmd+" ");
        HoverEvent he=new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.translatable("tooltip.highlight.hc.commands.suggest",Text.literal(cmd)));

        if(!cfg.commandProperties.getOrDefault(cmd, new ConfigManager.ModCommandProperties()).enabled){
            ce=null;
            he=new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.translatable("tooltip.highlight.hc.commands.disabled",Text.literal(cmd)).formatted(Formatting.RED));
        }
        
        Style clickable=Style.EMPTY
            .withFormatting(Formatting.GOLD)
            .withClickEvent(ce)
            .withHoverEvent(he)
        ;
        MutableText msg=Text.translatable("message.command.opLevelChanged",Text.literal(Integer.toString(level)).formatted(Formatting.GOLD),Text.literal("/"+cmd).setStyle(clickable)).formatted(Formatting.GREEN);
        if(level<=0){
            MutableText warning=Text.translatable("message.command.opLevelChanged.everyoneCanUse", Text.literal("/"+cmd).setStyle(clickable)).formatted(Formatting.RED);
            msg
                .append(Text.literal("\n"))
                .append(warning)
            ;
        }

        player.sendMessage(msg);
        return 1;
    }

    public static int info(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException{
        ServerPlayerEntity player=ctx.getSource().getPlayer();

        player.sendMessage(getTitleMessage("message.command.hc.info"));
        Text modDescription=Text.literal(" ").append(Text.literal(FabricLoader.getInstance().getModContainer(HelpfulCommands.modID).get().getMetadata().getDescription()));
        MutableText modVersion=Text.literal(" ").append(Text.translatable("message.command.hc.info.modVersion",FabricLoader.getInstance().getModContainer(HelpfulCommands.modID).get().getMetadata().getVersion(),Text.literal("ThatsNotM3")).setStyle(Style.EMPTY.withFormatting(Formatting.GRAY)));
        Formatting buttonFormatting=Formatting.AQUA;
        MutableText buttonCommandList=Text.literal("")
            .append(Text.translatable("message.command.hc.commandList"))
            .setStyle(Style.EMPTY
                    .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/hc commands"))
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.translatable("tooltip.highlight.hc.info.button.commandList")))
                    .withFormatting(buttonFormatting)
            )
        ;
        MutableText buttonConfig=Text.literal("")
            .append(Text.translatable("message.command.hc.config"))
            .setStyle(Style.EMPTY
                    .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/hc config"))
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.translatable("tooltip.highlight.hc.info.button.config")))
                    .withFormatting(buttonFormatting)
            )
        ;
        MutableText buttonGitHub=Text.literal("GitHub").setStyle(Style.EMPTY
            .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/ThatsNotM3/HelpfulCommands"))
            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.translatable("tooltip.highlight.hc.info.button.gitHub")))
            .withFormatting(buttonFormatting))
        ;
        MutableText buttonModrinth=Text.literal("Modrinth").setStyle(Style.EMPTY
            .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://modrinth.com/mod/helpfulcommands/"))
            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.translatable("tooltip.highlight.hc.info.button.modrinth")))
            .withFormatting(buttonFormatting))
        ;
        MutableText buttonCurseforge=Text.literal("CurseForge").setStyle(Style.EMPTY
            .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.curseforge.com/minecraft/mc-mods/helpful-commands"))
            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.translatable("tooltip.highlight.hc.info.button.curseForge")))
            .withFormatting(buttonFormatting))
        ;
        MutableText buttons=Text.literal("")
            .append(Text.literal("[ "))
            .append(buttonCommandList)
            .append(Text.literal(" • "))
            .append(buttonConfig)
            .append(Text.literal(" ]"))
            .append(Text.literal("\n[ "))
            .append(buttonGitHub)
            .append(Text.literal(" • "))
            .append(buttonModrinth)
            .append(Text.literal(" • "))
            .append(buttonCurseforge)
            .append(Text.literal(" ]"))
        ;
        MutableText msg=Text.literal("")
            .append(modDescription)
            .append(Text.literal("\n"))
            .append(modVersion)
            .append(Text.literal("\n\n"))
            .append(buttons)
        ;
        player.sendMessage(msg);
        return 1;
    }

    public static int cmdList(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException{
        ServerPlayerEntity player=ctx.getSource().getPlayer();
        MinecraftServer server=player.getServer();
        
        player.sendMessage(getTitleMessage("message.command.hc.commandList"));

        Style opLevelStyle=Style.EMPTY.withFormatting(Formatting.YELLOW);
        MutableText header=Text.literal("")
                .append(Text.literal("(").setStyle(opLevelStyle))
                .append(Text.translatable("message.command.hc.commandList.header.opLevel").setStyle(opLevelStyle))
                .append(Text.translatable("").setStyle(opLevelStyle))
                .append(Text.literal(")").setStyle(opLevelStyle))
                .append(Text.literal(" "))
                .append(Text.translatable("message.command.hc.commandList.header.enabled").setStyle(Style.EMPTY.withFormatting(Formatting.GREEN)))
                .append(Text.literal("/"))
                .append(Text.translatable("message.command.hc.commandList.header.disabled").setStyle(Style.EMPTY.withFormatting(Formatting.RED)))
                ;
        player.sendMessage(header);

        MutableText msg=Text.literal("");
        for(String i : ModCommandManager.commands){
            MutableText permissionLevel=Text.literal("("+getCommandOpLevel(i,server)+") ").setStyle(Style.EMPTY
                    .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,"/hc commands "+i+" opLevel "))
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.translatable("tooltip.highlight.hc.commands.opLevel",i)))
                    .withFormatting(Formatting.YELLOW)
            );
            MutableText commandItself=Text.literal("/"+i).setStyle(getCommandStyle(i,server));
            ClickEvent descCE;
            HoverEvent descHE;
            if(getCommandState(i,server)){
                descCE=new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,"/"+i+" ");
                descHE=new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.translatable("tooltip.highlight.hc.commands.suggest",i));
            } else{
                descCE=null;
                descHE=new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.translatable("tooltip.highlight.hc.commands.disabled",i).formatted(Formatting.RED));
            }
            Style descStyle=Style.EMPTY
                    .withClickEvent(descCE)
                    .withHoverEvent(descHE)
                    .withFormatting(Formatting.GRAY)
                    ;
            MutableText commandDescription=Text.translatable("command.description."+i).setStyle(descStyle);
            msg
                    .append("\n")
                    .append(permissionLevel)
                    .append(commandItself)
                    .append(Text.literal(": ").setStyle(descStyle))
                    .append(commandDescription)
            ;
        }
        player.sendMessage(msg);

        return 1;
    }
    public static int printConfig(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException{
        ServerPlayerEntity player=ctx.getSource().getPlayer();
        MinecraftServer server=player.getServer();

        ConfigManager.ModConfig cfg=ConfigManager.loadConfig(server);
        
        player.sendMessage(getTitleMessage("message.command.hc.config"));
        
        HoverEvent he=new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("tooltip.highlight.clickToEditConfigValue"));

        MutableText msg=Text.literal("\n")
            .append(Text.literal("configOPLevel").formatted(Formatting.YELLOW))
            .append(Text.literal(": ").formatted(Formatting.GRAY))
            .append(Text.literal(Integer.toString(cfg.configOPLevel)).setStyle(Style.EMPTY
                .withFormatting(Formatting.AQUA)
                .withHoverEvent(he)
                .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,"/hc config "+"configOPLevel "))
            ))
            .append("\n ")
            .append(Text.translatable("modconfig.configOPLevel.description").formatted(Formatting.GRAY))
            .append("\n")
            .append(Text.literal("explosionPowerLimit").formatted(Formatting.YELLOW))
            .append(Text.literal(": ").formatted(Formatting.GRAY))
            .append(Text.literal(Integer.toString(cfg.explosionPowerLimit)).setStyle(Style.EMPTY
                .withFormatting(Formatting.AQUA)
                .withHoverEvent(he)
                .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,"/hc config "+"explosionPowerLimit "))
            ))
            .append("\n ")
            .append(Text.translatable("modconfig.explosionPowerLimit.description").formatted(Formatting.GRAY))
        ;
        player.sendMessage(msg);

        return 1;
    }
    static MutableText getTitleMessage(String translationKey){
        return Text.literal("")
                .setStyle(Style.EMPTY
                        .withBold(true)
                        .withFormatting(Formatting.GREEN))
                .append(Text.literal("\u00AB "))
                .append(Text.translatable(translationKey))
                .append(Text.literal(" || Helpful Commands").setStyle(Style.EMPTY
                        .withFormatting(Formatting.GOLD)
                        .withBold(false)
                ))
                .append(Text.literal(" \u00BB"));
    }

    static boolean getCommandState(String cmd, MinecraftServer server){
        ConfigManager.ModCommandProperties cmdProperties=new ConfigManager.ModCommandProperties();
        try {
            cmdProperties = ConfigManager.loadConfig(server).commandProperties.getOrDefault(cmd,new ConfigManager.ModCommandProperties());
        } catch(Exception e){ HelpfulCommands.LOGGER.error("Error reading config file!",e); }
        return cmdProperties.enabled;
    }

    static Style getCommandStyle(String cmd,MinecraftServer server){
        Formatting color;
        if(getCommandState(cmd,server)) color=Formatting.GREEN;
        else color=Formatting.RED;
        return Style.EMPTY
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.translatable("tooltip.highlight.hc.commands.toggle",cmd)))
                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/hc commands "+cmd+" toggle"))
                .withFormatting(color);
    }

    static String getCommandOpLevel(String cmd,MinecraftServer server){
        ConfigManager.ModConfig cfg=ConfigManager.loadConfig(server);
        return Integer.toString(cfg.commandProperties.getOrDefault(cmd,new ConfigManager.ModCommandProperties()).opLevel);
    }

    static int changeConfigValue(String field, Object value,ServerPlayerEntity player){
        ConfigManager.ModConfig cfg=ConfigManager.loadConfig(player.getServer());

        MutableText configOPLevelWarning=null;

        int playerOPLevel=ModCommandManager.getPlayerPermissionLevel(player);
        int cfgOPLevel=cfg.configOPLevel;

        if(playerOPLevel<cfgOPLevel){
            player.sendMessage(Text.translatable("message.error.config.insufficientPrivileges",Text.literal(Integer.toString(cfgOPLevel)).formatted(Formatting.GOLD),Text.literal(Integer.toString(playerOPLevel)).formatted(Formatting.GOLD)).formatted(Formatting.RED));
            return -1;
        }

        switch(field){
            case "configOPLevel":
                int specifiedOPLevel=(int) value;
                if(specifiedOPLevel>playerOPLevel){
                    player.sendMessage(Text.translatable("message.error.config.configOPLevel.insufficientOPLevel",Text.literal(Integer.toString(playerOPLevel)).formatted(Formatting.GOLD),Text.literal(Integer.toString(specifiedOPLevel)).formatted(Formatting.GOLD)).formatted(Formatting.RED));
                    return -1;
                }
                cfg.configOPLevel=(int) value;
                if(specifiedOPLevel<=0) configOPLevelWarning=Text.translatable("message.command.hc.config.changedValue.configOPLevel.everyoneCanEdit").formatted(Formatting.RED);
                break;
            case "explosionPowerLimit":
                cfg.explosionPowerLimit=(int) value;
                break;
        }

        ConfigManager.saveConfig(cfg,player.getServer());

        player.sendMessage(Text.translatable("message.command.hc.config.changedValue",Text.literal(field).formatted(Formatting.GOLD),Text.literal(value.toString()).formatted(Formatting.GOLD)).formatted(Formatting.GREEN));
        if(configOPLevelWarning!=null) player.sendMessage(configOPLevelWarning);
        return 1;
    }

    static int printConfigValue(String field,ServerPlayerEntity player){
        ConfigManager.ModConfig cfg=ConfigManager.loadConfig(player.getServer());
        String val="UNKNOWN";
        switch(field){
            case "configOPLevel":
                val=Integer.toString(cfg.configOPLevel);
                break;
            case "explosionPowerLimit":
                val=Integer.toString(cfg.explosionPowerLimit);
                break;
        }

        ConfigManager.saveConfig(cfg,player.getServer());

        player.sendMessage(Text.translatable("message.command.hc.config.printValue",Text.literal(field).formatted(Formatting.GOLD),Text.literal(val).setStyle(Style.EMPTY
                .withFormatting(Formatting.GOLD)
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.translatable("tooltip.highlight.clickToEditConfigValue")))
                .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,"/hc config "+field+" "))
        )).formatted(Formatting.AQUA));
        return 1;
    }
}