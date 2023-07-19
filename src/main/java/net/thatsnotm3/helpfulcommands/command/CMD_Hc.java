package net.thatsnotm3.helpfulcommands.command;

import java.io.IOException;
import java.util.HashMap;
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
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment){
        LiteralArgumentBuilder<ServerCommandSource> commands=CommandManager.literal("commands");
        for(String i : ModCommandManager.commands){
            LiteralArgumentBuilder<ServerCommandSource> literalCommand=CommandManager.literal(i)
                    .then(CommandManager.literal("toggle")
                            .executes(ctx->toggleCommand(ctx,i))
                            .then(CommandManager.argument("value", BoolArgumentType.bool())
                            .executes(ctx -> toggleCommand(ctx, i, BoolArgumentType.getBool(ctx, "value"))))
                    )
                    .then(CommandManager.literal("opLevel")
                        .then(CommandManager.argument("value", IntegerArgumentType.integer(0, 4))
                        .executes(ctx->changeCommandOpLevel(ctx, i, IntegerArgumentType.getInteger(ctx, "value"))))
                    );
            commands.then(literalCommand);
        }
        commands.executes(CMD_Hc::cmdList);
        
        dispatcher.register(CommandManager.literal("hc")
            .then(CommandManager.literal("info").executes(CMD_Hc::info))
            .then(commands)
            .then(CommandManager.literal("config")
                
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

        if(!player.hasPermissionLevel(3)){
            player.sendMessage(Text.literal("\u00A7cYou can't toggle commands: Insufficient Privileges!"));
            return 1;
        }

        ConfigManager.ModConfig cfg=ConfigManager.loadConfig(player.getServer());
        Map<String,ConfigManager.ModCommandProperties> cmdProperties=cfg.commandProperties;
        ConfigManager.ModCommandProperties cmdInQuestion=cmdProperties.getOrDefault(cmd,new ConfigManager.ModCommandProperties());
        cmdInQuestion.enabled=state;
        cmdProperties.put(cmd,cmdInQuestion);
        cfg.commandProperties=cmdProperties;
        ConfigManager.saveConfig(cfg,player.getServer());

        MutableText msg;
        if(state){
            Style textStyle=Style.EMPTY.withFormatting(Formatting.GREEN);
            Style clickableCommandStyle=Style.EMPTY
                    .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,"/"+cmd))
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
        MutableText buttonGitHub=Text.literal("")
                .append(Text.literal("GitHub"))
                .setStyle(Style.EMPTY
                        .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/ThatsNotM3/HelpfulCommands"))
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.translatable("tooltip.highlight.hc.info.button.gitHub")))
                        .withFormatting(buttonFormatting)
                )
                ;
        MutableText buttons=Text.literal("")
                .append(Text.literal("[ "))
                .append(buttonCommandList)
                .append(Text.literal(" • "))
                .append(buttonGitHub)
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
                descCE=new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,"/"+i);
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
        ConfigManager.ModCommandProperties cmdProperties=ConfigManager.loadConfig(server).commandProperties.getOrDefault(cmd,new ConfigManager.ModCommandProperties());
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
        return "2";
    }
}