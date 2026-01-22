package io.yukkuric.hexop.forge

import io.yukkuric.hexop.HexOPXPlat
import net.minecraftforge.fml.ModList

class HexOPXPlatForge : HexOPXPlat() {
    override fun isModLoaded(id: String?): Boolean {
        return ModList.get().isLoaded(id)
    }
}