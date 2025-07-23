package com.expecticament.helpfulcommands.command.time;

import com.expecticament.helpfulcommands.command.ModCommandManager;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.expecticament.helpfulcommands.HelpfulCommands;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

public class TimeCommands{

    static int execute(CommandContext<ServerCommandSource> ctx, ModCommandManager.ModCommand cmd, int time) throws CommandSyntaxException {
        for(ServerWorld serverWorld : ctx.getSource().getServer().getWorlds()) serverWorld.setTimeOfDay(time);

        ServerCommandSource src = ctx.getSource();

        src.sendFeedback(() -> Text.translatable("commands.time.set", Text.literal(String.valueOf(time)).setStyle(HelpfulCommands.style.primary)).setStyle(HelpfulCommands.style.success), true);

        src.getServer().sendTimeUpdatePackets();

        return Command.SINGLE_SUCCESS;
    }
}
