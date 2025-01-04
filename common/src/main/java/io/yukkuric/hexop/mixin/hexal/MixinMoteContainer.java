package io.yukkuric.hexop.mixin.hexal;

import io.yukkuric.hexop.hexal.CachedNexusInventory;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import ram.talia.hexal.common.blocks.entity.BlockEntityMediafiedStorage;

@Mixin(BlockEntityMediafiedStorage.class)
public class MixinMoteContainer implements Container, CachedNexusInventory.Control {
    final CachedNexusInventory API = new CachedNexusInventory((BlockEntityMediafiedStorage) (Object) this);

    @Override
    public int getContainerSize() {
        var raw = API.getSlots();
        raw = (int) (Math.ceil(raw / (float) 9) * 9);
        return Math.min(54, Math.max(9, raw));
    }

    @Override
    public boolean isEmpty() {
        return API.isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        return API.getStackInSlot(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int count) {
        return API.extractItem(slot, count, false);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return API.extractItem(slot, Integer.MAX_VALUE, false);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        removeItemNoUpdate(slot);
        if (!stack.isEmpty()) API.insertItem(slot, stack, false);
    }

    @Override
    public int getMaxStackSize() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void setChanged() { // nope
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        API.clearContent();
    }

    @Override
    public void doRefresh() {
        API.doForceRefresh();
    }
}
