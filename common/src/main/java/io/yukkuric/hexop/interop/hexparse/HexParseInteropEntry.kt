package io.yukkuric.hexop.interop.hexparse

import at.petrak.hexcasting.api.HexAPI
import at.petrak.hexcasting.api.utils.putLong
import at.petrak.hexcasting.common.items.magic.ItemArtifact
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import io.yukkuric.hexop.HexOPConfig
import io.yukkuric.hexop.HexOverpowered
import io.yukkuric.hexparse.api.HexParseAPI
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.world.item.ItemStack

object HexParseInteropEntry {
    private fun initInner() {
        val listIotaKey = HexAPI.modLoc("list").toString()
        HexParseAPI.CreateItemIOMethod(
            ItemArtifact::class.java,
            { stack: ItemStack, tag: CompoundTag ->
                val spellList = if (tag.getString(HexIotaTypes.KEY_TYPE) == listIotaKey) {
                    tag.get(HexIotaTypes.KEY_DATA)
                } else {
                    val wrapped = ListTag()
                    wrapped.add(tag)
                    wrapped
                }
                val tag = stack.orCreateTag
                tag.put(ItemArtifact.TAG_PROGRAM, spellList)
                if (!tag.contains(ItemArtifact.TAG_MAX_MEDIA))
                    stack.putLong("hexcasting:start_media", 640_0000L)
            },
            { stack: ItemStack ->
                val spellList = stack.tag?.get(ItemArtifact.TAG_PROGRAM)
                    ?: return@CreateItemIOMethod null
                val ret = CompoundTag()
                ret.putString(HexIotaTypes.KEY_TYPE, listIotaKey)
                ret.put(HexIotaTypes.KEY_DATA, spellList)
                return@CreateItemIOMethod ret
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