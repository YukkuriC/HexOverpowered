package io.yukkuric.hexop;

import net.minecraft.resources.ResourceLocation;

public abstract class HexOverpowered {
    public static final String MOD_ID = "hexoverpowered";

    protected static ResourceLocation ID_NEXUS_INVENTORY = new ResourceLocation("hexop:nexus_inv");
    protected static ResourceLocation ID_MEKASUIT_MEDIA_POOL = new ResourceLocation("hexop:mekasuit_media");

    protected abstract boolean isModLoaded(String id);
}
