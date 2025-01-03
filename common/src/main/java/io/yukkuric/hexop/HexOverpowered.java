package io.yukkuric.hexop;

import net.minecraft.resources.ResourceLocation;

public abstract class HexOverpowered {
    public static final String MOD_ID = "hexop";

    protected static ResourceLocation ID_NEXUS_INVENTORY = new ResourceLocation("hexop:nexus_inv");

    protected abstract boolean isModLoaded(String id);
}
