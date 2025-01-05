package io.yukkuric.hexop.hexal;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import static io.yukkuric.hexop.hexal.CompressedChestMenu.UI_VISIBLE_SLOTS;

public class MoteChestContainer implements Container {
    final Container parent;
    final CachedNexusInventory API;

    public MoteChestContainer(Container wrapper, CachedNexusInventory api) {
        parent = wrapper;
        API = api;
    }

    boolean shouldKeepLastCellEmpty(int i) {
        return i == UI_VISIBLE_SLOTS - 1 && !API.isFull();
    }

    public int getContainerSize() {
        return UI_VISIBLE_SLOTS;
    }

    @Override
    public boolean isEmpty() {
        return parent.isEmpty();
    }

    @Override
    public ItemStack getItem(int i) {
        if (shouldKeepLastCellEmpty(i)) return ItemStack.EMPTY;
        return parent.getItem(i);
    }

    @Override
    public ItemStack removeItem(int i, int j) {
        if (shouldKeepLastCellEmpty(i)) return ItemStack.EMPTY;
        return parent.removeItem(i, j);
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        if (shouldKeepLastCellEmpty(i)) return ItemStack.EMPTY;
        return parent.removeItemNoUpdate(i);
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        if (shouldKeepLastCellEmpty(i)) {
            API.insertItem(i, itemStack, false);
            return;
        }
        parent.setItem(i, itemStack);
    }

    @Override
    public void setChanged() { // nope
    }

    @Override
    public boolean stillValid(Player player) {
        return parent.stillValid(player);
    }

    @Override
    public void clearContent() {
        parent.clearContent();
    }
}
