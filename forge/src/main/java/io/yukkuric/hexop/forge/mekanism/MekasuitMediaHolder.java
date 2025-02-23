package io.yukkuric.hexop.forge.mekanism;

import at.petrak.hexcasting.api.addldata.ADMediaHolder;
import io.yukkuric.hexop.forge.HexOPConfigForge;
import mekanism.api.energy.IEnergyContainer;
import mekanism.api.math.FloatingLong;
import mekanism.common.util.StorageUtils;
import net.minecraft.world.item.ItemStack;

public class MekasuitMediaHolder implements ADMediaHolder {
    final IEnergyContainer source;

    public MekasuitMediaHolder(ItemStack target) {
        source = StorageUtils.getEnergyContainer(target, 0);
    }

    int Joule2Media(FloatingLong raw) {
        var FE = raw.multiply(0.4); // j -> fe
        return FE.multiply(HexOPConfigForge.MekasuitConversionRatio()).intValue();
    }

    @Override
    public int getMedia() {
        return Joule2Media(source.getEnergy());
    }

    @Override
    public int getMaxMedia() {
        return Joule2Media(source.getMaxEnergy());
    }

    @Override
    public void setMedia(int newVal) {
        var rate = HexOPConfigForge.MekasuitConversionRatio();
        if (rate <= 0) return;
        newVal = Math.max(0, newVal);
        var FE = FloatingLong.create(newVal / rate);
        source.setEnergy(FE.multiply(2.5)); // fe -> j
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
