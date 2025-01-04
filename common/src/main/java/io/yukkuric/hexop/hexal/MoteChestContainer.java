package io.yukkuric.hexop.hexal;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import static io.yukkuric.hexop.hexal.CompressedChestMenu.UI_VISIBLE_SLOTS;

public class MoteChestContainer implements Container {
    Container parent;

    public MoteChestContainer(Container wrapper) {
        parent = wrapper;
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
        return parent.getItem(i);
    }

    @Override
    public ItemStack removeItem(int i, int j) {
        return parent.removeItem(i, j);
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        return parent.removeItemNoUpdate(i);
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
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
