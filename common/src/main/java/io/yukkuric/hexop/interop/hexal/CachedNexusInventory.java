package io.yukkuric.hexop.interop.hexal;

import io.yukkuric.hexop.HexOPConfig;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import ram.talia.hexal.api.config.HexalConfig;
import ram.talia.hexal.api.mediafieditems.ItemRecord;
import ram.talia.hexal.common.blocks.entity.BlockEntityMediafiedStorage;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static io.yukkuric.hexop.interop.hexal.CompressedChestMenu.UI_VISIBLE_SLOTS;

public class CachedNexusInventory implements AutoCloseable {
    protected final BlockEntityMediafiedStorage source;
    protected Map<Integer, ItemRecord> srcMap;
    static final Field fIdx;
    protected final int maxTypes;

    protected int cachedCurIdx = -1;
    protected List<Integer> cachedKeys = List.of();
    protected int emptyCounter = 0;

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

    public void doForceRefresh() {
        cachedCurIdx = -1;
        refreshCache();
        source.sync();
    }

    public void refreshCache() {
        if (srcMap == null) srcMap = source.getStoredItems();
        var newIdx = getIdx();
        if (newIdx == cachedCurIdx) return;
        cachedCurIdx = newIdx;
        cachedKeys = srcMap.keySet().stream().toList();
        emptyCounter = 0;
    }

    protected ItemRecord get(int slot) {
        refreshCache();
        if (slot >= cachedKeys.size()) return null;
        var idx = cachedKeys.get(slot);
        return srcMap.getOrDefault(idx, null);
    }

    protected ItemStack makeItem(ItemRecord record) {
        return record.toStack((int) record.getCount());
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
        oldEmpty = isEmpty();
        return this;
    }

    @Override
    public void close() {
        if (oldEmpty != isEmpty()) source.sync();
        if (shouldFlushForContainer()) doForceRefresh(); // force squash empty slots
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
        if (slot >= maxTypes || stack.isEmpty() || !HexOPConfig.EnablesMoteItemHandler()) return stack;
        var record = get(slot);
        if (record == null) {
            if (!simulate) {
                try (var self = checkEmptyChange()) {
                    synchronized (srcMap) {
                        if (slot < cachedKeys.size()) { // reuse idx
                            srcMap.put(cachedKeys.get(slot), new ItemRecord(stack));
                            if (slot < UI_VISIBLE_SLOTS) emptyCounter--;
                        } else source.assignItem(new ItemRecord(stack));
                    }
                }
            }
            return ItemStack.EMPTY;
        }
        if (!matches(record, stack)) return stack;
        if (!simulate) {
            record.addCount(stack.getCount());
        }
        return ItemStack.EMPTY;
    }

    public @NotNull ItemStack extractItem(int slot, int count, boolean simulate) {
        if (slot >= maxTypes || !HexOPConfig.EnablesMoteItemHandler()) return ItemStack.EMPTY;
        var record = get(slot);
        if (record == null) return ItemStack.EMPTY;
        count = (int) Math.min(count, record.getCount());
        if (count <= 0) return ItemStack.EMPTY;
        var item = makeItem(record);
        item.setCount(count);
        if (!simulate) {
            record.addCount(-count);
            if (record.getCount() <= 0) {
                try (var self = checkEmptyChange()) {
                    synchronized (srcMap) {
                        srcMap.remove(cachedKeys.get(slot));
                        if (slot < UI_VISIBLE_SLOTS) emptyCounter++;
                    }
                }
            }
        }
        return item;
    }

    public int getSlotLimit(int slot) {
        if (slot >= maxTypes) return 0;
        var record = get(slot);
        if (record == null) return 64;
        return (int) Math.min(Integer.MAX_VALUE, record.getCount() + 64);
    }

    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        if (slot >= maxTypes || !HexOPConfig.EnablesMoteItemHandler()) return false;
        var record = get(slot);
        return record == null || matches(record, stack);
    }

    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        if (slot >= maxTypes || !HexOPConfig.EnablesMoteItemHandler()) return;
        refreshCache();
        synchronized (srcMap) {
            if (stack.isEmpty()) {
                if (slot < cachedKeys.size()) srcMap.remove(cachedKeys.get(slot));
            } else {
                var replace = new ItemRecord(stack);
                if (slot >= cachedKeys.size()) source.assignItem(replace);
                else srcMap.put(cachedKeys.get(slot), replace);
            }
        }
    }

    // ========== Container API Support ==========

    public void clearContent() {
        if (!HexOPConfig.EnablesMoteItemHandler()) return;
        try (var self = checkEmptyChange()) {
            srcMap.clear();
        }
    }

    public boolean isEmpty() {
        return srcMap.isEmpty();
    }

    public boolean isFull() {
        return srcMap.size() >= maxTypes;
    }

    public boolean shouldFlushForContainer() {
        return (emptyCounter >= UI_VISIBLE_SLOTS - 9) && (cachedKeys.size() > UI_VISIBLE_SLOTS);
    }

    // ========== Exposed to Mixin ==========

    public interface Control {
        CachedNexusInventory getAPI();

        MoteChestContainer wrapForChest();
    }
}