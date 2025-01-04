package io.yukkuric.hexop.hexal;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;

public class CompressedChestMenu extends ChestMenu {
    public static final int UI_VISIBLE_SLOTS = 54;
    final CachedNexusInventory API;

    public CompressedChestMenu(int i, Inventory inventory, Container container, CachedNexusInventory api) {
        super(MenuType.GENERIC_9x6, i, inventory, container, 6);
        this.API = api;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIdx) {
        if (slotIdx < 0) return ItemStack.EMPTY;
        if (slotIdx >= UI_VISIBLE_SLOTS) {
            var slot = getSlot(slotIdx);
            var item = slot.getItem();
            if (!API.isFull()) slot.set(API.insertItem(API.getSlots(), item, false));
            return ItemStack.EMPTY;
        } else {
            var slot = getSlot(slotIdx);
            var toMove = slot.container.getItem(slotIdx);
            var count = Math.min(toMove.getCount(), toMove.getMaxStackSize());
            toMove.setCount(count);
            if (!this.moveItemStackTo(toMove, UI_VISIBLE_SLOTS, this.slots.size(), true)) {
                return ItemStack.EMPTY;
            }
            var delta = count - toMove.getCount();
            if (delta != 0) API.extractItem(slotIdx, delta, false);
            return ItemStack.EMPTY;
        }
    }
}
