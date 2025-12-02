package io.yukkuric.hexop.actions

import at.petrak.hexcasting.api.PatternRegistry
import at.petrak.hexcasting.api.spell.Action
import at.petrak.hexcasting.api.spell.math.HexDir
import at.petrak.hexcasting.api.spell.math.HexPattern
import io.yukkuric.hexop.HexOverpowered.opModLoc
import io.yukkuric.hexop.actions.mind_env.*

class HexOPActions {
    companion object {
        init {
            wrap("yjsp_media", "eaddaddaeaeaddaddaeaeaddaddae", HexDir.NORTH_EAST, OpChargeMedia, true)
            wrap("get_personal_media", "qqaqqea", HexDir.EAST, OpGetAttr.GetMana)
            wrap("get_personal_media_max", "qqaqqqd", HexDir.EAST, OpGetAttr.GetMaxMana)
            // mind env
            wrap("mind_stack/push", "waawweeeeedd", HexDir.SOUTH_WEST, OpMindStackEdit.PUSH)
            wrap("mind_stack/pop", "wqaqwweeeee", HexDir.SOUTH_WEST, OpMindStackEdit.POP)
            wrap("mind_stack/size", "waawweeeeewaa", HexDir.SOUTH_WEST, OpMindStackSize)
            wrap("mind_patterns", "waawweeeeaaeaeaeaeaw", HexDir.SOUTH_WEST, OpMindPatterns)
            wrap("mind_env/schedule", "waawedaqqqqdeaqq", HexDir.SOUTH_WEST, OpScheduleCall)
            wrap("mind_env/running_code", "wqaqwweeeeeqdeaqq", HexDir.SOUTH_WEST, OpScheduledCode)
        }

        private fun wrap(name: String, signature: String, dir: HexDir, action: Action, isGreat: Boolean = false) {
            val pattern = HexPattern.fromAngles(signature, dir)
            val key = opModLoc(name)
            PatternRegistry.mapPattern(pattern, key, action, isGreat);
        }

        @JvmStatic
        fun keepAlive() {
        }
    }
}