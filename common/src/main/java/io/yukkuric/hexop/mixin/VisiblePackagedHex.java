package io.yukkuric.hexop.mixin;

import at.petrak.hexcasting.api.casting.iota.ListIota;
import at.petrak.hexcasting.common.items.magic.ItemMediaHolder;
import at.petrak.hexcasting.common.items.magic.ItemPackagedHex;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

import static at.petrak.hexcasting.common.items.magic.ItemPackagedHex.TAG_PROGRAM;

@Mixin(ItemPackagedHex.class)
public abstract class VisiblePackagedHex extends ItemMediaHolder {
    @Shadow
    public abstract boolean hasHex(ItemStack stack);

    public VisiblePackagedHex(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        if (!hasHex(pStack)) return;
        pTooltipComponents.add(Component.translatable("hexcasting.spelldata.onitem", ListIota.TYPE.display(pStack.getTag().getList(TAG_PROGRAM, Tag.TAG_COMPOUND))));
    }
}
