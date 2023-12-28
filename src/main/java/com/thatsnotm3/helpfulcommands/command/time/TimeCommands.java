package com.thatsnotm3.helpfulcommands.command.time;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.thatsnotm3.helpfulcommands.HelpfulCommands;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

public class TimeCommands{

    static int execute(CommandContext<ServerCommandSource> ctx,int time) throws CommandSyntaxException {
        for(ServerWorld serverWorld : ctx.getSource().getServer().getWorlds()) serverWorld.setTimeOfDay(time);
        ctx.getSource().sendFeedback(() -> Text.translatable("commands.time.set", Text.literal(String.valueOf(time)).setStyle(HelpfulCommands.style.primary)).setStyle(HelpfulCommands.style.success), true);

        return Command.SINGLE_SUCCESS;
    }
}