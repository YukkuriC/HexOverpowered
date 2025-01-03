package io.yukkuric.hexop.hexal;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import ram.talia.hexal.api.config.HexalConfig;
import ram.talia.hexal.api.mediafieditems.ItemRecord;
import ram.talia.hexal.common.blocks.entity.BlockEntityMediafiedStorage;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class CachedNexusInventory implements AutoCloseable {
    protected final BlockEntityMediafiedStorage source;
    protected final Map<Integer, ItemRecord> srcMap;
    static final Field fIdx;
    protected final int maxTypes;

    protected int cachedCurIdx = -1;
    protected List<Integer> cachedKeys = List.of();

    static {
        try {
            fIdx = BlockEntityMediafiedStorage.class.getDeclaredField("currentItemIndex");
            fIdx.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public CachedNexusInventory(BlockEntityMediafiedStorage be) {
        source = be;
        srcMap = be.getStoredItems();
        maxTypes = HexalConfig.getServer().getMaxRecordsInMediafiedStorage();
    }

    // ========== General API ==========

    protected int getIdx() {
        try {
            return (int) fIdx.get(source);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    protected void refreshCache() {
        var newIdx = getIdx();
        if (newIdx == cachedCurIdx) return;
        cachedCurIdx = newIdx;
        cachedKeys = srcMap.keySet().stream().toList();
    }

    protected ItemRecord get(int slot) {
        refreshCache();
        if (slot >= cachedKeys.size()) return null;
        var idx = cachedKeys.get(slot);
        return srcMap.getOrDefault(idx, null);
    }

    protected ItemStack makeItem(ItemRecord record) {
        var res = new ItemStack(record.getItem(), (int) record.getCount());
        var tag = record.getTag();
        if (tag != null) res.setTag(tag);
        return res;
    }

    protected boolean matches(ItemRecord record, ItemStack stack) {
        if (record.getItem() != stack.getItem()) return false;
        var tag = record.getTag();
        var newTag = stack.getTag();
        if (tag == null) return newTag == null;
        return tag.equals(newTag);
    }

    // ========== Nexus Anim Tracker ==========

    boolean oldEmpty;

    protected CachedNexusInventory checkEmptyChange() {
        oldEmpty = srcMap.isEmpty();
        return this;
    }

    @Override
    public void close() {
        if (oldEmpty != srcMap.isEmpty()) source.sync();
    }

    // ========== Forge IItemHandler API ==========

    public int getSlots() {
        refreshCache();
        return Math.min(1 + cachedKeys.size(), maxTypes);
    }

    public @NotNull ItemStack getStackInSlot(int slot) {
        if (slot >= maxTypes) return ItemStack.EMPTY;
        var record = get(slot);
        if (record == null) return ItemStack.EMPTY;
        return makeItem(record);
    }

    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (slot >= maxTypes) return stack;
        var record = get(slot);
        if (record == null) {
            try (var self = checkEmptyChange()) {
                if (!simulate) {
                    if (slot < cachedKeys.size()) { // reuse idx
                        srcMap.put(cachedKeys.get(slot), new ItemRecord(stack));
                    } else source.assignItem(new ItemRecord(stack));
                }
                return ItemStack.EMPTY;
            }
        }
        if (!matches(record, stack)) return stack;
        if (!simulate) {
            record.addCount(stack.getCount());
        }
        return ItemStack.EMPTY;
    }

    public @NotNull ItemStack extractItem(int slot, int count, boolean simulate) {
        if (slot >= maxTypes) return ItemStack.EMPTY;
        var record = get(slot);
        if (record == null) return ItemStack.EMPTY;
        count = (int) Math.min(count, record.getCount());
        if (count <= 0) return ItemStack.EMPTY;
        try (var self = checkEmptyChange()) {
            var item = makeItem(record);
            item.setCount(count);
            if (!simulate) {
                record.addCount(-count);
                if (record.getCount() <= 0) srcMap.remove(cachedKeys.get(slot));
            }
            return item;
        }
    }

    public int getSlotLimit(int slot) {
        if (slot >= maxTypes) return 0;
        var record = get(slot);
        if (record == null) return 64;
        return (int) Math.min(Integer.MAX_VALUE, record.getCount() + 64);
    }

    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        if (slot >= maxTypes) return false;
        var record = get(slot);
        return record == null || matches(record, stack);
    }
}