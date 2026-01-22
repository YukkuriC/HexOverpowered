package io.yukkuric.hexop.fabric

import io.yukkuric.hexop.HexOPXPlat
import net.fabricmc.loader.api.FabricLoader

class HexOPXPlatFabric : HexOPXPlat() {
    override fun isModLoaded(id: String?): Boolean {
        return FabricLoader.getInstance().isModLoaded(id)
    }
}