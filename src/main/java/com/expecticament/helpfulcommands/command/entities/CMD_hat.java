package com.expecticament.helpfulcommands.command.entities;

import com.expecticament.helpfulcommands.HelpfulCommands;
import com.expecticament.helpfulcommands.command.IHelpfulCommandsCommand;
import com.expecticament.helpfulcommands.command.ModCommandManager;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CMD_hat implements IHelpfulCommandsCommand {

    public static ModCommandManager.ModCommand cmd;

    public static void init(ModCommandManager.ModCommand newData){
        cmd = newData;
    }

    public static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment){
        dispatcher.register(CommandManager.literal(cmd.name)
                .executes(CMD_hat::execute)
                .then(CommandManager.argument("target", EntityArgumentType.players())
                        .executes(ctx -> execute(ctx, EntityArgumentType.getPlayers(ctx, "target")))
                        .then(CommandManager.argument("item", ItemStackArgumentType.itemStack(registryAccess))
                                .executes(ctx -> execute(ctx, EntityArgumentType.getPlayers(ctx, "target"), ItemStackArgumentType.getItemStackArgument(ctx, "item").createStack(1, true)))
                        )
                )
                .requires(src-> ModCommandManager.canUseCommand(src, cmd))
        );
    }

    private static int execute(CommandContext<ServerCommandSource> ctx) {
        Collection<ServerPlayerEntity> collection = new ArrayList<>();
        collection.add(ctx.getSource().getPlayer());
        return execute(ctx, collection, null);
    }

    private static int execute(CommandContext<ServerCommandSource> ctx, Collection<? extends ServerPlayerEntity> targets) {
        return execute(ctx, targets, null);
    }

    private static int execute(CommandContext<ServerCommandSource> ctx, Collection<? extends ServerPlayerEntity> targets, ItemStack itemStack) {
        ServerCommandSource src = ctx.getSource();

        ServerPlayerEntity plr = src.getPlayer();

        if(itemStack == null) {
            if(!src.isExecutedByPlayer()){
                src.sendError(Text.translatable("error.inGameOnly"));
                return -1;
            }
            ItemStack playerItemStack = plr.getMainHandStack();
            if(playerItemStack == null || playerItemStack == ItemStack.EMPTY) {
                src.sendError(Text.translatable("error.nothingInMainHand"));
                return -1;
            }
            itemStack = new ItemStack(playerItemStack.getItem());
        }

        itemStack.setCount(1);

        boolean isSelfOnly = (targets.size() == 1 && targets.contains(plr));
        boolean isAir = (itemStack.getItem() == Items.AIR);

        final String itemName = (itemStack.get(DataComponentTypes.CUSTOM_NAME) == null) ? itemStack.getName().getString() : itemStack.getName().getString();
        final Text targetAmountText = getTargetAmountText(new ArrayList<>(targets));

        for(ServerPlayerEntity i : targets) {
            i.getInventory().setStack(39, itemStack);
            i.playSoundToPlayer(SoundEvents.ITEM_ARMOR_EQUIP_GENERIC.value(), SoundCategory.PLAYERS, 1, 1);
            if(i != plr) {
                if(isAir)
                    i.sendMessage(Text.translatable("commands.hat.removed.self").setStyle(HelpfulCommands.style.tertiary));
                else {
                    i.sendMessage(Text.translatable("commands.hat.success.self", Text.literal(itemName).setStyle(HelpfulCommands.style.primary)).setStyle(HelpfulCommands.style.tertiary));
                }
            }
        }

        if(!isSelfOnly){
            if(isAir) {
                src.sendFeedback(()-> Text.translatable("commands.hat.removed.other", targetAmountText).setStyle(HelpfulCommands.style.success), true);
            } else {
                src.sendFeedback(()-> Text.translatable("commands.hat.success.other", Text.literal(itemName).setStyle(HelpfulCommands.style.primary), targetAmountText).setStyle(HelpfulCommands.style.success), true);
            }
        } else{
            if(isAir) {
                plr.sendMessage(Text.translatable("commands.hat.removed.self").setStyle(HelpfulCommands.style.success));
            }  else {
                plr.sendMessage(Text.translatable("commands.hat.success.self", Text.literal(itemName).setStyle(HelpfulCommands.style.primary)).setStyle(HelpfulCommands.style.success));
            }
        }

        return Command.SINGLE_SUCCESS;
    }

    private static Text getTargetAmountText(List<ServerPlayerEntity> targets) {
        StringBuilder names = new StringBuilder();
        for(ServerPlayerEntity i : targets) {
            names.append(i.getDisplayName().getString());
            if(i != targets.getLast()) {
                names.append("\n");
            }
        }

        return Text.literal(String.valueOf(targets.size())).setStyle(HelpfulCommands.style.primary.withHoverEvent(
                new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal(names.toString()))
        ));
    }
}
