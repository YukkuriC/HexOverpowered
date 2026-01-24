package io.yukkuric.hexop.forge.interop.mekanism;

import at.petrak.hexcasting.api.utils.MediaHelper;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import mekanism.common.item.gear.ItemMekaSuitArmor;
import net.minecraft.network.chat.*;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import java.text.DecimalFormat;

import static at.petrak.hexcasting.common.items.magic.ItemMediaHolder.HEX_COLOR;

public class MekTooltip {
    private static final DecimalFormat PERCENTAGE = new DecimalFormat("####");
    private static final DecimalFormat DUST_AMOUNT = new DecimalFormat("###,###.##");

    public static void handleMekasuitTooltip(ItemTooltipEvent e) {
        var stack = e.getItemStack();
        if (!(stack.getItem() instanceof ItemMekaSuitArmor)) return;
        var mediaHolder = IXplatAbstractions.INSTANCE.findMediaHolder(stack);
        if (mediaHolder == null) return;

        int maxMedia = mediaHolder.getMaxMedia();
        if (maxMedia > 0L) {
            int media = mediaHolder.getMedia();
            float fullness = (float) media / (float) maxMedia;
            TextColor color = TextColor.fromRgb(MediaHelper.mediaBarColor(media, maxMedia));
            MutableComponent mediamount = Component.literal(DUST_AMOUNT.format((float) media / 10000.0F));
            MutableComponent percentFull = Component.literal(PERCENTAGE.format((double) (100.0F * fullness)) + "%");
            MutableComponent maxCapacity = Component.translatable("hexcasting.tooltip.media", DUST_AMOUNT.format(((float) maxMedia / 10000.0F)));
            mediamount.withStyle((style) -> style.withColor(HEX_COLOR));
            maxCapacity.withStyle((style) -> style.withColor(HEX_COLOR));
            percentFull.withStyle((style) -> style.withColor(color));
            e.getToolTip().add(Component.translatable("hexcasting.tooltip.media_amount.advanced", mediamount, maxCapacity, percentFull));
        }
    }
}
