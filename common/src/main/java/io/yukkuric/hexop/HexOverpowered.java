package io.yukkuric.hexop;

import at.petrak.hexcasting.api.misc.MediaConstants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public abstract class HexOverpowered {
    public HexOverpowered() {
        INSTANCE = this;
    }

    public static final String MOD_ID = "hexoverpowered";
    public static HexOverpowered INSTANCE;

    protected static ResourceLocation ID_NEXUS_INVENTORY = new ResourceLocation("hexop:nexus_inv");
    protected static ResourceLocation ID_MEKASUIT_MEDIA_POOL = new ResourceLocation("hexop:mekasuit_media");

    protected abstract boolean isModLoaded(String id);
    protected abstract boolean isFakePlayer(Player target);

    public static ResourceLocation opModLoc(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public static boolean IsModLoaded(String id) {
        return INSTANCE.isModLoaded(id);
    }
    public static boolean IsFakePlayer(Player target) {
        return INSTANCE.isFakePlayer(target);
    }

    public interface DEFAULTS {
        int MANA_MAX = 10 * (int) MediaConstants.CRYSTAL_UNIT;
        int MANA_REGEN = (int) MediaConstants.SHARD_UNIT;
        int MANA_REGEN_INTERVAL = 20;
    }
}
