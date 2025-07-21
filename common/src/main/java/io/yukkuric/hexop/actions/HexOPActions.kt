package io.yukkuric.hexop.actions

import at.petrak.hexcasting.api.casting.ActionRegistryEntry
import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.common.lib.hex.HexActions
import io.yukkuric.hexop.HexOverpowered.opModLoc
import io.yukkuric.hexop.actions.mind_env.OpMindPatterns
import io.yukkuric.hexop.actions.mind_env.OpMindStackEdit
import io.yukkuric.hexop.actions.mind_env.OpMindStackSize
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation

class HexOPActions {
    companion object {
        private val CACHED: MutableMap<ResourceLocation, ActionRegistryEntry> = HashMap()

        init {
            wrap("yjsp_media", "eaddaddaeaeaddaddaeaeaddaddae", HexDir.NORTH_EAST, OpChargeMedia)
            wrap("get_personal_media", "qqaqqea", HexDir.EAST, OpGetAttr.GetMana)
            wrap("get_personal_media_max", "qqaqqqd", HexDir.EAST, OpGetAttr.GetMaxMana)
            // mind env
            wrap("mind_stack/push", "waawweeeeedd", HexDir.SOUTH_WEST, OpMindStackEdit.PUSH)
            wrap("mind_stack/pop", "wqaqwweeeee", HexDir.SOUTH_WEST, OpMindStackEdit.POP)
            wrap("mind_stack/size", "waawweeeeewaa", HexDir.SOUTH_WEST, OpMindStackSize)
            wrap("mind_patterns", "waawweeeeaaeaeaeaeaw", HexDir.SOUTH_WEST, OpMindPatterns)
        }

        @JvmStatic
        fun registerActions() {
            val reg = HexActions.REGISTRY
            for ((key, value) in CACHED) Registry.register(reg, key, value)
        }

        private fun wrap(name: String, signature: String, dir: HexDir, action: Action?): ActionRegistryEntry {
            val pattern = HexPattern.fromAngles(signature, dir)
            val key = opModLoc(name)
            val entry = ActionRegistryEntry(pattern, action)
            CACHED[key] = entry
            return entry
        }
    }
}