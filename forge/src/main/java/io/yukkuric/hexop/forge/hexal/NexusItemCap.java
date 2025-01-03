package io.yukkuric.hexop.forge.hexal;

import io.yukkuric.hexop.hexal.CachedNexusInventory;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ram.talia.hexal.common.blocks.entity.BlockEntityMediafiedStorage;

public class NexusItemCap extends CachedNexusInventory implements IItemHandler {
    public NexusItemCap(BlockEntityMediafiedStorage be) {
        super(be);
    }

    public static class Provider implements ICapabilityProvider {
        final LazyOptional<NexusItemCap> getter;

        public Provider(BlockEntityMediafiedStorage be) {
            getter = LazyOptional.of(() -> new NexusItemCap(be));
        }

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction arg) {
            if (capability != ForgeCapabilities.ITEM_HANDLER) return LazyOptional.empty();
            return getter.cast();
        }
    }
}
