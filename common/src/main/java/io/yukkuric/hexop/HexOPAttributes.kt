package io.yukkuric.hexop

import io.yukkuric.hexop.HexOverpowered.MOD_ID
import io.yukkuric.hexop.HexOverpowered.opModLoc
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.level.Level
import java.util.concurrent.ConcurrentHashMap

class HexOPAttributes {
    companion object {
        @JvmStatic
        val PERSONAL_MEDIA: Holder<Attribute>
            get() = getHolder(_personal_media)
        @JvmStatic
        val PERSONAL_MEDIA_MAX: Holder<Attribute>
            get() = getHolder(_personal_media_max)
        @JvmStatic
        val PERSONAL_MEDIA_REGEN: Holder<Attribute>
            get() = getHolder(_personal_media_regen)
        @JvmStatic
        val INIT_MEDIA_MARKER = -1919810.0

        private val MAP = HashMap<ResourceLocation, Attribute>()
        private val MAP_HOLDER = ConcurrentHashMap<Attribute, Holder<Attribute>>()
        private var _personal_media = make("personal_media") { INIT_MEDIA_MARKER }
        private var _personal_media_max = make("personal_media_max") { HexOPConfig.PersonalMediaMax().toDouble() }
        private var _personal_media_regen =
            make("personal_media_regen") { HexOPConfig.PersonalMediaRegenStep().toDouble() }

        private fun make(id: String, getDefault: () -> Double): Attribute {
            val attr = DynamicAttr("$MOD_ID.attr.$id", getDefault).setSyncable(true)
            MAP[opModLoc(id)] = attr
            return attr
        }

        @JvmStatic
        fun getAll(): MutableCollection<Attribute> {
            return MAP.values
        }

        @JvmStatic
        fun getHolder(attr: Attribute) = MAP_HOLDER.getOrPut(attr) { BuiltInRegistries.ATTRIBUTE.wrapAsHolder(attr) }
        @JvmStatic
        fun getAllHolders(): List<Holder<Attribute>> {
            return MAP.values.map(::getHolder)
        }

        @JvmStatic
        fun registerSelf(handler: java.util.function.BiConsumer<ResourceLocation, Attribute>) {
            for (pair in MAP.entries) handler.accept(pair.key, pair.value)
        }

        @JvmStatic
        fun applyDefaultValues(entity: Entity, level: Level) {
            if (level.isClientSide || entity !is ServerPlayer) return
            val attrMap = entity.attributes
            for (attr in getAllHolders()) {
                attrMap.getInstance(attr)?.baseValue = attr.value().defaultValue
            }
        }
    }

    class DynamicAttr(id: String, private val getDefault: () -> Double) : Attribute(id, 114514.0) {
        override fun getDefaultValue() = try {
            getDefault()
        } catch (e: Throwable) {
            super.getDefaultValue()
        }
    }
}