package io.yukkuric.hexop.mixin.hexal;

import io.yukkuric.hexop.hexal.CachedNexusInventory;
import io.yukkuric.hexop.hexal.MoteChestContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import ram.talia.hexal.common.blocks.entity.BlockEntityMediafiedStorage;

@Mixin(BlockEntityMediafiedStorage.class)
public abstract class MixinMoteContainer extends BlockEntity
        implements Container, CachedNexusInventory.Control {
    public MixinMoteContainer(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }
    final CachedNexusInventory API = new CachedNexusInventory((BlockEntityMediafiedStorage) (Object) this);

    int cachedSlot = -1;
    ItemStack cachedItem = null;

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
        var res = API.getStackInSlot(slot);
        cachedSlot = slot;
        cachedItem = res;
        return res;
    }

    @Override
    public ItemStack removeItem(int slot, int count) {
        cachedItem = null; // invalidate cached
        return API.extractItem(slot, count, false);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        cachedItem = null; // invalidate cached
        return API.extractItem(slot, Integer.MAX_VALUE, false);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        cachedItem = null; // invalidate cached
        API.setStackInSlot(slot, stack);
    }

    @Override
    public int getMaxStackSize() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (cachedItem == null) return;
        API.setStackInSlot(cachedSlot, cachedItem);
        cachedItem = null;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        API.clearContent();
        cachedItem = null; // invalidate cached
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
