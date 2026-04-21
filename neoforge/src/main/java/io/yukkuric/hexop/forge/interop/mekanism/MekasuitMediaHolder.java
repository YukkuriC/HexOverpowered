package io.yukkuric.hexop.forge.interop.mekanism;

import at.petrak.hexcasting.api.addldata.ADMediaHolder;
import io.yukkuric.hexop.forge.HexOPConfigForge;
import mekanism.api.energy.IEnergyContainer;
import mekanism.common.util.StorageUtils;
import net.minecraft.world.item.ItemStack;

public class MekasuitMediaHolder implements ADMediaHolder {
    final IEnergyContainer source;

    public MekasuitMediaHolder(ItemStack target) {
        source = StorageUtils.getEnergyContainer(target, 0);
    }

    long Joule2Media(long raw) {
        var FE = raw * 0.4; // j -> fe
        return (long) (FE * HexOPConfigForge.MekasuitConversionRatio());
    }

    @Override
    public long getMedia() {
        return Joule2Media(source.getEnergy());
    }

    @Override
    public long getMaxMedia() {
        return Joule2Media(source.getMaxEnergy());
    }

    @Override
    public void setMedia(long newVal) {
        var rate = HexOPConfigForge.MekasuitConversionRatio();
        if (rate <= 0) return;
        newVal = Math.max(0, newVal);
        var FE = newVal / rate;
        source.setEnergy((long) (FE * 2.5)); // fe -> j
    }

    @Override
    public boolean canRecharge() {
        return false;
    }

    @Override
    public boolean canProvide() {
        return HexOPConfigForge.MekasuitConversionRatio() > 0;
    }

    @Override
    public int getConsumptionPriority() {
        return ADMediaHolder.BATTERY_PRIORITY - 1;
    }

    @Override
    public boolean canConstructBattery() {
        return false;
    }
}
