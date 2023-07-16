package net.thatsnotm3.helpfulcommands.command;

import java.io.IOException;
import java.util.Map;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.thatsnotm3.helpfulcommands.HelpfulCommands;
import net.thatsnotm3.helpfulcommands.TranslationManager;
import net.thatsnotm3.helpfulcommands.util.ConfigUtils;
import net.fabricmc.loader.api.FabricLoader;

public class CMD_Hc{
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment){
        LiteralArgumentBuilder<ServerCommandSource> commands=CommandManager.literal("commands");
        for(String i : ModCommandManager.commands){
            LiteralArgumentBuilder<ServerCommandSource> literalCommand=CommandManager.literal(i)
                    .then(CommandManager.argument("value", BoolArgumentType.bool())
                    .executes(ctx -> toggleCommand(ctx, i, BoolArgumentType.getBool(ctx, "value"))));
            
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

    public static int toggleCommand(CommandContext<ServerCommandSource> ctx,String cmd,Boolean state) throws CommandSyntaxException{
        ServerPlayerEntity player=ctx.getSource().getPlayer();

        if(!player.hasPermissionLevel(3)){
            player.sendMessage(Text.literal("\u00A7cYou can't toggle commands: Insufficient Privileges!"));
            return 1;
        }

        Map<String, Boolean> map;
        try{
            map=ConfigUtils.loadConfig(player.getServer());
            map.put(cmd,state);
        } catch(IOException e){
            HelpfulCommands.LOGGER.error("Failed to load or create server config file", e);
            return -1;
        }
        try{
            ConfigUtils.saveConfig(map, player.getServer());
        } catch(IOException e){
            HelpfulCommands.LOGGER.error("Failed to load or create server config file", e);
            return -1;
        }

        if(state) player.sendMessage(Text.literal("\u00A7aEnabled \u00A76/"+cmd+"\u00A7a command"));
        else player.sendMessage(Text.literal("\u00A7cDisabled \u00A76/"+cmd+"\u00A7c command"));

        return 1;
    }

    public static int info(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException{
        ServerPlayerEntity player=ctx.getSource().getPlayer();

        player.sendMessage(Text.literal(
            "\u00A7a\u00A7lHelpful Commands Mod \u00A7r\u00A77|| ver: "+FabricLoader.getInstance().getModContainer("helpfulcommands").get().getMetadata().getVersion()+" || by ThatsNotM3"+"\u00A7r\n "+FabricLoader.getInstance().getModContainer("helpfulcommands").get().getMetadata().getDescription()
        ));
        player.sendMessage(Text.literal("\n\u00A7b[Command List]").setStyle(Style.EMPTY
            .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/hc commandList"))
            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.literal("Print Command List")))
        ));
        player.sendMessage(Text.literal("\u00A7b[GitHub]").setStyle(Style.EMPTY
            .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/ThatsNotM3/HelpfulCommands"))
            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.literal("Open mod's GitHub Repository")))
        ));
        return 1;
    }

    public static int cmdList(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException{
        ServerPlayerEntity player=ctx.getSource().getPlayer();
        MinecraftServer server=player.getServer();

        /*player.sendMessage(Text.literal(
            +
            "\n"+getCommandColor("abilities",server)+" Configure and read your abilities (fly, invulnerability, etc.)"+
            "\n"+getCommandColor("back",server)+" Return to where you last died"+
            "\n"+getCommandColor("day",server)+" Set the time to Day"+
            "\n"+getCommandColor("dimension",server)+" Read or switch your current dimension"+
            "\n"+getCommandColor("explosion",server)+" Create an explosion"+
            "\n"+getCommandColor("feed",server)+" Feeds and completely saturates you"+
            "\n"+getCommandColor("gm",server)+" Switch your game mode"+
            "\n"+getCommandColor("heal",server)+" Fully restores your health"+
            "\n"+getCommandColor("home",server)+" Set, get, and teleport to your Home Position"+
            "\n"+getCommandColor("jump",server)+" Teleport to your cursor position with a given distance"+
            "\n"+getCommandColor("killitems",server)+" Kill all items lying on the ground"+
            "\n"+getCommandColor("lightning",server)+" Strike a Lightning at your cursor position"+
            "\n"+getCommandColor("night",server)+" Set the time to Night"+
            "\n"+getCommandColor("spawn",server)+" Get or teleport to your or world spawn point"
        ));*/
        player.sendMessage(Text.literal("\u00A7b\u00A7lCommand List\u00A7r \u00A77|| \u00A7aEnabled \u00A7cDisabled\u00A7r"));
        for(String i : ModCommandManager.commands){
            player.sendMessage(Text.literal(
                getCommandColor(i,server)+": "+TranslationManager.getTranslation("command.description."+i)
            ).setStyle(Style.EMPTY
                .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,"/"+i))
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.literal("Click to suggest command")))
            ));
        }

        return 1;
    }

    static String getCommandColor(String cmd,MinecraftServer server){
        Map<String, Boolean> map;
        try{
            map=ConfigUtils.loadConfig(server);
        } catch(IOException e){
            HelpfulCommands.LOGGER.error("Failed to load or create server config file", e);
            return "\u00A7a/"+cmd+"\u00A77";
        }
        if(map.containsKey(cmd)) if(!map.get(cmd)) return "\u00A7c/"+cmd+"\u00A77";
        return "\u00A7a/"+cmd+"\u00A77";
    }
}