package io.yukkuric.hexop.forge.mekanism;

import at.petrak.hexcasting.api.addldata.ADMediaHolder;
import mekanism.api.energy.IEnergyContainer;
import mekanism.api.math.FloatingLong;
import mekanism.common.util.StorageUtils;
import net.minecraft.world.item.ItemStack;

public class MekasuitMediaHolder implements ADMediaHolder {
    final IEnergyContainer source;

    public MekasuitMediaHolder(ItemStack target) {
        source = StorageUtils.getEnergyContainer(target, 0);
    }

    @Override
    public int getMedia() {
        return source.getEnergy().intValue();
    }

    @Override
    public int getMaxMedia() {
        return source.getMaxEnergy().intValue();
    }

    @Override
    public void setMedia(int newVal) {
        source.setEnergy(FloatingLong.create(newVal));
    }

    @Override
    public boolean canRecharge() {
        return false;
    }

    @Override
    public boolean canProvide() {
        return true;
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
