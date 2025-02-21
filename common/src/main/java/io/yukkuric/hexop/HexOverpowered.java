package io.yukkuric.hexop;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;

public abstract class HexOverpowered {
    public HexOverpowered() {
        INSTANCE = this;
    }

    public static final String MOD_ID = "hexoverpowered";
    public static HexOverpowered INSTANCE;
    public static MinecraftServer GRAB_SERVER;

    protected static ResourceLocation ID_NEXUS_INVENTORY = new ResourceLocation("hexop:nexus_inv");
    protected static ResourceLocation ID_MEKASUIT_MEDIA_POOL = new ResourceLocation("hexop:mekasuit_media");

    protected abstract boolean isModLoaded(String id);

    public static ResourceLocation opModLoc(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public static boolean IsModLoaded(String id) {
        return INSTANCE.isModLoaded(id);
    }
}
