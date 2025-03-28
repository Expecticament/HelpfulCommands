package com.expecticament.helpfulcommands.command.entities;

import com.expecticament.helpfulcommands.HelpfulCommands;
import com.expecticament.helpfulcommands.command.IHelpfulCommandsCommand;
import com.expecticament.helpfulcommands.command.ModCommandManager;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;


public class CMD_invsee implements IHelpfulCommandsCommand {

    public static ModCommandManager.ModCommand cmd;

    public static void init(ModCommandManager.ModCommand newData){
        cmd = newData;
    }

    public static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment){
        dispatcher.register(CommandManager.literal(cmd.name)
                .then(CommandManager.argument("target", EntityArgumentType.player())
                        .executes(ctx -> openInventory(ctx, EntityArgumentType.getPlayer(ctx, "target")))
                )
                .requires(src-> ModCommandManager.canUseCommand(src, cmd))
        );
    }

    private static int openInventory(CommandContext<ServerCommandSource> ctx, ServerPlayerEntity target) {
        ServerCommandSource src = ctx.getSource();

        if(!src.isExecutedByPlayer()){
            src.sendError(Text.translatable("error.inGameOnly"));
            return -1;
        }

        ServerPlayerEntity plr = src.getPlayer();

        if(plr == target) {
            src.sendError(Text.translatable("error.cantUseOnYourself"));
            return -1;
        }

        plr.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, inv, player) -> new InvseeScreenHandler(syncId, inv, target), Text.translatable("commands.invsee.screenTitle", Text.literal(target.getName().getString()))));

        return Command.SINGLE_SUCCESS;
    }

    private static class InvseeScreenHandler extends ScreenHandler {

        public InvseeScreenHandler(int syncId, PlayerInventory sourceInventory, PlayerEntity target) {
            super(ScreenHandlerType.GENERIC_9X5, syncId);

            PlayerInventory targetInventory = target.getInventory();
            this.addPlayerSlots(targetInventory, 0, 0);
            for(int i = 0; i < 2; i++) {
                this.addSlot(new EmptySlot(new PlayerInventory(target, null), 0, 0, 0));
            }
            for(int i = 0; i < 5; i++) {
                this.addSlot(new Slot(targetInventory, 36 + i, 0, 0));
            }
            for(int i = 0; i < 2; i++) {
                this.addSlot(new EmptySlot(new PlayerInventory(target, null), 0, 0, 0));
            }
            this.addPlayerSlots(sourceInventory, 0, 0);
        }

        @Override
        public ItemStack quickMove(PlayerEntity player, int slotIndex) {
            ItemStack itemStack = ItemStack.EMPTY;
            Slot slot = this.slots.get(slotIndex);

            if (slot.hasStack()) {
                ItemStack stackInSlot = slot.getStack();
                itemStack = stackInSlot.copy();

                int targetInventoryStart = 0;
                int targetInventoryEnd = 36;
                int sourceInventoryStart = 36;
                int sourceInventoryEnd = this.slots.size();

                if (slotIndex >= targetInventoryStart && slotIndex < targetInventoryEnd) {
                    // Shift-click from target's inventory to source's inventory
                    if (!this.insertItem(stackInSlot, sourceInventoryStart, sourceInventoryEnd, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotIndex >= sourceInventoryStart && slotIndex < sourceInventoryEnd) {
                    // Shift-click from source's inventory to target's inventory
                    if (!this.insertItem(stackInSlot, targetInventoryStart, targetInventoryEnd, false)) {
                        return ItemStack.EMPTY;
                    }
                }

                if (stackInSlot.isEmpty()) {
                    slot.setStack(ItemStack.EMPTY);
                } else {
                    slot.markDirty();
                }
            }

            return itemStack;
        }

        @Override
        public boolean canUse(PlayerEntity player) {
            return true;
        }
    }

    public static class EmptySlot extends Slot {

        public EmptySlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);

            ItemStack itemStack = new ItemStack(Items.BARRIER);
            itemStack.set(DataComponentTypes.CUSTOM_NAME, Text.translatable("commands.invsee.emptySlotName").setStyle(HelpfulCommands.style.disabled));

            setStack(itemStack);
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            return false;
        }
        @Override
        public boolean canTakeItems(PlayerEntity playerEntity) {
            return false;
        }
    }
}