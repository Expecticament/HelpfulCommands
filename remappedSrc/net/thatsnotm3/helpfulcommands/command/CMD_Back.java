package net.thatsnotm3.helpfulcommands.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.thatsnotm3.helpfulcommands.util.IEntityDataSaver;

public class CMD_Back{

    static final String cmdName="back";

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment){
        dispatcher.register(CommandManager.literal(cmdName)
            .then(CommandManager.literal("get").executes(CMD_Back::getPos))
            .then(CommandManager.literal("tp").executes(CMD_Back::returnToPos))
            .executes(CMD_Back::returnToPos)
        );
    }

    public static int returnToPos(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException{
        ServerPlayerEntity player=ctx.getSource().getPlayer();
        IEntityDataSaver playerData=(IEntityDataSaver) player;

        if(!ModCommandManager.RunChecks(cmdName,player)) return -1;

        int[] deathPos=playerData.getPersistentData().getIntArray("deathPosition");
        String dimensionName=playerData.getPersistentData().getString("deathDimension");
        if(deathPos.length!=0){
            // TODO: Support for modded dimensions
            ServerWorld dimension=null;
            switch(dimensionName){
                case "minecraft:overworld":
                    dimension=player.getServer().getWorld(ServerWorld.OVERWORLD);
                    break;
                case "minecraft:the_end":
                    dimension=player.getServer().getWorld(ServerWorld.END);
                    break;
                case "minecraft:the_nether":
                    dimension=player.getServer().getWorld(ServerWorld.NETHER);
                    break;
            }
            if(dimension==null){
                player.sendMessage(Text.translatable("text.unknownDimension", Text.literal(dimensionName).formatted(Formatting.GOLD)).formatted(Formatting.RED));
                return -1;
            }
            player.teleport(dimension,deathPos[0],deathPos[1],deathPos[2],player.getYaw(),player.getPitch());
            player.sendMessage(Text.translatable("message.command.back.self").setStyle(Style.EMPTY
                .withFormatting(Formatting.GREEN)
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.literal("x: "+deathPos[0]+"\ny: "+deathPos[1]+"\nz: "+deathPos[2])))
                .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,"/tp "+deathPos[0]+" "+deathPos[1]+" "+deathPos[2]))
            ));
        } else{
            player.sendMessage(Text.translatable("message.command.back.noDeathPosition").formatted(Formatting.RED));
        }

        return 1;
    }

    public static int getPos(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException{
        IEntityDataSaver playerData=(IEntityDataSaver) ctx.getSource().getPlayer();
        ServerPlayerEntity player=ctx.getSource().getPlayer();

        if(!ModCommandManager.RunChecks("back",player)) return -1;

        int[] deathPos=playerData.getPersistentData().getIntArray("deathPosition");
        String dimensionName=playerData.getPersistentData().getString("deathDimension");
        Style buttonStyle=Style.EMPTY
            .withFormatting(Formatting.AQUA)
            .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/back"))
            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("tooltip.highlight.back.get.teleportButton")))
        ;
        if(deathPos.length!=0){
            MutableText msg=Text.literal("")
                .append(Text.literal("\u00AB ").formatted(Formatting.AQUA,Formatting.BOLD))
                .append(Text.translatable("message.command.back.get.title").formatted(Formatting.AQUA,Formatting.BOLD))
                .append(Text.literal(" \u00BB").formatted(Formatting.AQUA,Formatting.BOLD))
                .append(Text.literal("\nx: "))
                .append(Text.literal(Integer.toString(deathPos[0])).formatted(Formatting.GOLD))
                .append(Text.literal("\ny: "))
                .append(Text.literal(Integer.toString(deathPos[1])).formatted(Formatting.GOLD))
                .append(Text.literal("\nz: "))
                .append(Text.literal(Integer.toString(deathPos[2])).formatted(Formatting.GOLD))
                .append(Text.literal("\n"))
                .append(Text.translatable("text.dimension",Text.literal(dimensionName).formatted(Formatting.GOLD)))
                .append(Text.literal("\n\n[").setStyle(buttonStyle))
                .append(Text.translatable("text.teleport").setStyle(buttonStyle))
                .append(Text.literal("]").setStyle(buttonStyle))
            ;
            player.sendMessage(msg);
        } else{
            player.sendMessage(Text.translatable("message.command.back.noDeathPosition").formatted(Formatting.RED));
        }

        return 1;
    }
}