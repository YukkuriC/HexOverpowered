package io.yukkuric.hexop.mixin;

import at.petrak.hexcasting.api.casting.iota.ListIota;
import at.petrak.hexcasting.common.items.magic.ItemMediaHolder;
import at.petrak.hexcasting.common.items.magic.ItemPackagedHex;
import at.petrak.hexcasting.common.lib.HexDataComponents;
import io.yukkuric.hexop.HexOPConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(ItemPackagedHex.class)
public abstract class VisiblePackagedHex extends ItemMediaHolder {
    private static Class<?> ANCIENT_CYPHER_CLASS;

    static {
        try {
            ANCIENT_CYPHER_CLASS = Class.forName("at.petrak.hexcasting.common.items.magic.ItemAncientCypher");
        } catch (Throwable e) {
            ANCIENT_CYPHER_CLASS = null;
        }
    }

    @Shadow
    public abstract boolean hasHex(ItemStack stack);

    public VisiblePackagedHex(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext tooltipContext, List<Component> pTooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(pStack, tooltipContext, pTooltipComponents, tooltipFlag);
        if (!hasHex(pStack) || !HexOPConfig.RevealsHexInsideCastingItems()) return;
        if (ANCIENT_CYPHER_CLASS != null && ANCIENT_CYPHER_CLASS.isInstance(this)) return;
        pTooltipComponents.add(Component.translatable("hexcasting.spelldata.onitem", new ListIota(pStack.get(HexDataComponents.PATTERNS)).display()));
    }
}
