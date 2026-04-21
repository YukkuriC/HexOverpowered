package io.yukkuric.hexop.forge.hexal;

import io.yukkuric.hexop.interop.hexal.CachedNexusInventory;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import ram.talia.hexal.common.blocks.entity.BlockEntityMediafiedStorage;

public class NexusItemCap extends CachedNexusInventory implements IItemHandlerModifiable {
    public NexusItemCap(BlockEntityMediafiedStorage be) {
        super(be);
    }
}
