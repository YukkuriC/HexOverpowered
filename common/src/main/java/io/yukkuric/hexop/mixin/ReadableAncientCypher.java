package io.yukkuric.hexop.mixin;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.item.IotaHolderItem;
import at.petrak.hexcasting.common.items.magic.ItemAncientCypher;
import at.petrak.hexcasting.common.items.magic.ItemArtifact;
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemAncientCypher.class)
public class ReadableAncientCypher implements IotaHolderItem {
    @Override
    public @Nullable CompoundTag readIotaTag(ItemStack stack) {
        if (!stack.hasTag()) return null;
        var spellList = stack.getTag().get(ItemArtifact.TAG_PROGRAM);
        if (spellList == null) return null;
        var ret = new CompoundTag();
        ret.put(HexIotaTypes.KEY_DATA, spellList);
        ret.putString(HexIotaTypes.KEY_TYPE, "hexcasting:list");
        return ret;
    }
    @Override
    public boolean writeable(ItemStack itemStack) {
        return false;
    }
    @Override
    public boolean canWrite(ItemStack itemStack, @Nullable Iota iota) {
        return false;
    }
    @Override
    public void writeDatum(ItemStack itemStack, @Nullable Iota iota) {
        // nope
    }
}
