package io.yukkuric.hexop.fabric;

import io.yukkuric.hexop.HexOverpowered;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public final class HexOverpoweredFabric extends HexOverpowered implements ModInitializer {
    @Override
    public void onInitialize() {
        // TODO CCA
    }

    @Override
    protected boolean isModLoaded(String id) {
        return FabricLoader.getInstance().isModLoaded(id);
    }
}
