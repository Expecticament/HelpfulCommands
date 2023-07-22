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
import net.thatsnotm3.helpfulcommands.util.IEntityDataSaver;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;

public class CMD_Home{

    static final String cmdName="home";

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment){
        dispatcher.register(CommandManager.literal(cmdName)
            .then(CommandManager.literal("set").executes(CMD_Home::setHome))
            .then(CommandManager.literal("get").executes(CMD_Home::getHome))
            .then(CommandManager.literal("tp").executes(CMD_Home::returnHome))
            .executes(CMD_Home::returnHome)
        );
    }

    public static int returnHome(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException{
        IEntityDataSaver playerData=(IEntityDataSaver)ctx.getSource().getPlayer();
        ServerPlayerEntity player=ctx.getSource().getPlayer();

        if(!ModCommandManager.RunChecks(cmdName,player)) return -1;

        int[] homePos=playerData.getPersistentData().getIntArray("homePosition");
        String dimensionName=playerData.getPersistentData().getString("homeDimension");
        if(homePos.length!=0){
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
            player.teleport(dimension,homePos[0],homePos[1],homePos[2],player.getYaw(),player.getPitch());
            MutableText msg=Text.translatable("message.command.home.teleported").setStyle(Style.EMPTY
                .withFormatting(Formatting.GREEN)
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("x: "+homePos[0]+"\ny: "+homePos[1]+"\nz: "+homePos[2])))
                .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,"/tp "+homePos[0]+" "+homePos[1]+" "+homePos[2]))
            );
            player.sendMessage(msg);
        } else{
            sendPosNotSetMessage(player);
            return -1;
        }

        return 1;
    }

    public static int setHome(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException{
        IEntityDataSaver playerData=(IEntityDataSaver)ctx.getSource().getPlayer();
        ServerPlayerEntity player=ctx.getSource().getPlayer();

        if(!ModCommandManager.RunChecks("home",player)) return -1;

        BlockPos playerPos=player.getBlockPos();
        playerData.getPersistentData().putIntArray("homePosition",new int[]{playerPos.getX(),playerPos.getY(),playerPos.getZ()});
        RegistryKey<World> dimensionKey=player.method_48926().getRegistryKey();
        String dimensionName=dimensionKey.getValue().toString();
        playerData.getPersistentData().putString("homeDimension", dimensionName);
        MutableText msg=Text.translatable("message.command.home.positionSet").setStyle(Style.EMPTY
            .withFormatting(Formatting.GREEN)
            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("x: "+playerPos.getX()+"\ny: "+playerPos.getY()+"\nz: "+playerPos.getZ())))
            .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,"/tp "+playerPos.getX()+" "+playerPos.getY()+" "+playerPos.getZ()))
        );
        player.sendMessage(msg);

        return 1;
    }

    public static int getHome(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException{
        IEntityDataSaver playerData=(IEntityDataSaver)ctx.getSource().getPlayer();
        ServerPlayerEntity player=ctx.getSource().getPlayer();

        if(!ModCommandManager.RunChecks("home",player)) return -1;

        int[] homePos=playerData.getPersistentData().getIntArray("homePosition");
        String dimensionName=playerData.getPersistentData().getString("homeDimension");
        Style buttonStyle=Style.EMPTY
            .withFormatting(Formatting.AQUA)
            .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/home"))
            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("tooltip.highlight.home.get.teleportButton")))
        ;
        if(homePos.length!=0){
            MutableText msg=Text.literal("")
                .append(Text.literal("\u00AB ").formatted(Formatting.AQUA,Formatting.BOLD))
                .append(Text.translatable("message.command.home.get.title").formatted(Formatting.AQUA,Formatting.BOLD))
                .append(Text.literal(" \u00BB").formatted(Formatting.AQUA,Formatting.BOLD))
                .append(Text.literal("\nx: "))
                .append(Text.literal(Integer.toString(homePos[0])).formatted(Formatting.GOLD))
                .append(Text.literal("\ny: "))
                .append(Text.literal(Integer.toString(homePos[1])).formatted(Formatting.GOLD))
                .append(Text.literal("\nz: "))
                .append(Text.literal(Integer.toString(homePos[2])).formatted(Formatting.GOLD))
                .append(Text.literal("\n"))
                .append(Text.translatable("text.dimension",Text.literal(dimensionName).formatted(Formatting.GOLD)))
                .append(Text.literal("\n\n[").setStyle(buttonStyle))
                .append(Text.translatable("text.teleport").setStyle(buttonStyle))
                .append(Text.literal("]").setStyle(buttonStyle))
            ;
            player.sendMessage(msg);
        } else{
            sendPosNotSetMessage(player);
            return -1;
        }

        return 1;
    }

    static void sendPosNotSetMessage(ServerPlayerEntity player){
        MutableText msg=Text.translatable("message.command.home.notSet",Text.literal("/home set").setStyle(Style.EMPTY
            .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/home set"))
            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.translatable("tooltip.highlight.clickToExecute")))
            .withFormatting(Formatting.GOLD)
        )).formatted(Formatting.RED);
        player.sendMessage(msg);
    }
}