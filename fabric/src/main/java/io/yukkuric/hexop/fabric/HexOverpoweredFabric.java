package io.yukkuric.hexop.fabric;

import io.yukkuric.hexop.HexOPAttributes;
import io.yukkuric.hexop.HexOverpowered;
import io.yukkuric.hexop.actions.HexOPActions;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public final class HexOverpoweredFabric extends HexOverpowered implements ModInitializer {
    @Override
    public void onInitialize() {
        HexOPActions.registerActions();
    }

    @Override
    protected boolean isModLoaded(String id) {
        return FabricLoader.getInstance().isModLoaded(id);
    }

    public static void initSelfHook() {
    }

    // dirty work
    static {
        HexOPConfigFabric.setup();
        HexOPAttributes.registerSelf();
    }
}
