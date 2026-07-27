package io.yukkuric.hexop.interop.hexparse

import at.petrak.hexcasting.api.HexAPI
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.common.items.magic.ItemArtifact
import at.petrak.hexcasting.common.lib.HexDataComponents
import io.yukkuric.hexop.HexOPConfig
import io.yukkuric.hexop.HexOverpowered
import io.yukkuric.hexparse.api.HexParseAPI
import net.minecraft.world.item.ItemStack

object HexParseInteropEntry {
    private fun initInner() {
        val listIotaKey = HexAPI.modLoc("list").toString()
        HexParseAPI.CreateItemIOMethod(
            ItemArtifact::class.java,
            { stack: ItemStack, tag: Iota ->
                val list = if (tag is ListIota) tag.list.toList() else listOf(tag)
                stack.set(HexDataComponents.HEX_HOLDER_PATTERNS.get(), list)
                if (!stack.has(HexDataComponents.MEDIA_MAX.get()))
                    stack.set(HexDataComponents.MEDIA_MAX.get(), 640_0000L)
            },
            { stack: ItemStack ->
                val spellList = stack.get(HexDataComponents.HEX_HOLDER_PATTERNS.get()) ?: return@CreateItemIOMethod null
                return@CreateItemIOMethod ListIota(spellList)
            },
            114514,
            { _, _ -> HexOPConfig.EnablesArtifactIO() }
        )
    }

    @JvmStatic
    fun init() {
        try {
            initInner()
        } catch (e: Throwable) {
            HexOverpowered.LOGGER.error("HexParse interop load failed: ${e.stackTraceToString()}")
        }
    }
}