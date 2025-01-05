package io.yukkuric.hexop.mixin.hexal;

import io.yukkuric.hexop.hexal.CachedNexusInventory;
import io.yukkuric.hexop.hexal.MoteChestContainer;
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
        return API.getSlots();
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
        API.setStackInSlot(slot, stack);
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
    public CachedNexusInventory getAPI() {
        return API;
    }

    MoteChestContainer cachedChest;

    @Override
    public MoteChestContainer wrapForChest() {
        if (cachedChest == null) cachedChest = new MoteChestContainer(this, API);
        return cachedChest;
    }
}
