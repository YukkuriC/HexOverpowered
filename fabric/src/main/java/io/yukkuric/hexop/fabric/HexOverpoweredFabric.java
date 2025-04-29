package io.yukkuric.hexop.fabric;

import io.yukkuric.hexop.HexOPAttributes;
import io.yukkuric.hexop.HexOverpowered;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.entity.player.Player;

public final class HexOverpoweredFabric extends HexOverpowered implements ModInitializer {
    @Override
    public void onInitialize() {
        HexOPConfigFabric.setup();
    }

    @Override
    protected boolean isModLoaded(String id) {
        return FabricLoader.getInstance().isModLoaded(id);
    }

    @Override
    public boolean isFakePlayer(Player target) {
//        return target instanceof FakePlayer;
        // wait wtf fabric 1.19 has no FakePlayer?
        return false;
    }

    public static void initSelfHook() {
    }

    // dirty work
    static {
        HexOPConfigFabric.setup();
        HexOPAttributes.registerSelf();
    }
}
