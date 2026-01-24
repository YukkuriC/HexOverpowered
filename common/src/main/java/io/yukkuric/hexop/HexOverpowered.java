package io.yukkuric.hexop;

import at.petrak.hexcasting.api.misc.MediaConstants;
import io.yukkuric.hexop.actions.HexOPActions;
import com.mojang.logging.LogUtils;
import io.yukkuric.hexop.interop.hexparse.HexParseInteropEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;

public class HexOverpowered {
    public HexOverpowered() {
        INSTANCE = this;
        HexOPActions.keepAlive();
        // common init
        if (IsModLoaded("hexparse")) HexParseInteropEntry.init();
    }

    public static final String MOD_ID = "hexoverpowered";
    public static HexOverpowered INSTANCE;
    public static final Logger LOGGER = LogUtils.getLogger();

    protected static ResourceLocation ID_NEXUS_INVENTORY = new ResourceLocation("hexop:nexus_inv");
    protected static ResourceLocation ID_MEKASUIT_MEDIA_POOL = new ResourceLocation("hexop:mekasuit_media");

    public static ResourceLocation opModLoc(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public static boolean IsModLoaded(String id) {
        return HexOPXPlat.INSTANCE.isModLoaded(id);
    }
    public static boolean IsFakePlayer(Player target) {
        // whitelist-ed
        return target.getClass() != ServerPlayer.class;
    }

    public interface DEFAULTS {
        int MANA_MAX = 10 * (int) MediaConstants.CRYSTAL_UNIT;
        int MANA_REGEN = (int) MediaConstants.SHARD_UNIT;
        int MANA_REGEN_INTERVAL = 20;
    }
}
