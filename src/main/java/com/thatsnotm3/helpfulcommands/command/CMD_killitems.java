package com.thatsnotm3.helpfulcommands.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.thatsnotm3.helpfulcommands.HelpfulCommands;
import com.thatsnotm3.helpfulcommands.command.util.ModCommandManager;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;

import java.util.HashMap;
import java.util.Map;

public class CMD_killitems implements IHelpfulCommandsCommand{

    public static ModCommandManager.hcCommand cmd;

    public static void init(ModCommandManager.hcCommand newData){
        cmd=newData;
    }

    public static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment){
        dispatcher.register(CommandManager.literal(cmd.name)
                .executes(CMD_killitems::execute)
                .requires(Permissions.require(HelpfulCommands.modID+".command."+cmd.category.toString().toLowerCase()+"."+cmd.name,cmd.defaultRequiredLevel))
        );
    }

    private static int execute(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException{
        ServerCommandSource src=ctx.getSource();

        int boxExpansion=50;

        Map<String, Integer> entries=new HashMap<>();
        if(src.getServer().getPlayerManager().getCurrentPlayerCount()>0) {
            for (ServerPlayerEntity i : src.getServer().getPlayerManager().getPlayerList()) entries.putAll(killItems(src, i.getBoundingBox().expand(boxExpansion)));
        } else{
            entries.putAll(killItems(src,new Box(src.getWorld().getSpawnPos()).expand(boxExpansion)));
        }

        int count=0;

        String entryList="";
        for(Map.Entry<String, Integer> i : entries.entrySet()){
            entryList+=i.getValue()+"x "+i.getKey()+"\n";
            count+=i.getValue();
        }
        if(!entryList.isEmpty()) entryList=entryList.substring(0, entryList.length()-1);

        if(count>0) {
            MutableText finalCount = Text.literal(String.valueOf(count)).setStyle(HelpfulCommands.style.primary
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.literal(entryList)))
            );
            src.sendFeedback(() -> Text.translatable("commands.killitems.success", finalCount).setStyle(HelpfulCommands.style.success), true);
        } else{
            src.sendError(Text.translatable("commands.killitems.error.nothingFound").setStyle(HelpfulCommands.style.error));
        }

        return Command.SINGLE_SUCCESS;
    }

    private static Map<String, Integer> killItems(ServerCommandSource src, Box b){
        Map<String, Integer> entries=new HashMap<>();

        for(ItemEntity i : src.getWorld().getEntitiesByType(EntityType.ITEM, b.expand(75), entity->true)){
            i.kill();
            String name=i.getName().getString();
            entries.put(name,entries.getOrDefault(name,0)+1);
        }

        return entries;
    }
}