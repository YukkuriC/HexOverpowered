package io.yukkuric.hexop.actions

import at.petrak.hexcasting.api.PatternRegistry
import at.petrak.hexcasting.api.spell.Action
import at.petrak.hexcasting.api.spell.math.HexDir
import at.petrak.hexcasting.api.spell.math.HexPattern
import io.yukkuric.hexop.HexOverpowered.opModLoc

class HexOPActions {
    companion object {
        init {
            wrap("yjsp_media", "eaddaddaeaeaddaddaeaeaddaddae", HexDir.NORTH_EAST, OpChargeMedia)
        }

        private fun wrap(name: String, signature: String, dir: HexDir, action: Action, isGreat: Boolean = true) {
            val pattern = HexPattern.fromAngles(signature, dir)
            val key = opModLoc(name)
            PatternRegistry.mapPattern(pattern, key, action, isGreat);
        }

        @JvmStatic
        fun keepAlive() {
        }
    }
}