package io.yukkuric.hexop

import io.yukkuric.hexop.HexOverpowered.MOD_ID
import io.yukkuric.hexop.HexOverpowered.opModLoc
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.RangedAttribute

class HexOPAttributes {
    companion object {
        @JvmStatic
        val PERSONAL_MEDIA: Attribute
            get() = _personal_media
        @JvmStatic
        val PERSONAL_MEDIA_MAX: Attribute
            get() = _personal_media_max
        @JvmStatic
        val PERSONAL_MEDIA_REGEN: Attribute
            get() = _personal_media_regen

        private lateinit var _personal_media: Attribute
        private lateinit var _personal_media_max: Attribute
        private lateinit var _personal_media_regen: Attribute
        private val MAP = HashMap<ResourceLocation, Attribute>()

        private fun makeRanged(id: String, default: Double): Attribute {
            val attr = RangedAttribute("$MOD_ID.attr.$id", default, 0.0, Int.MAX_VALUE.toDouble()).setSyncable(true)
            MAP[opModLoc(id)] = attr
            return attr
        }

        @JvmStatic
        fun getAll(): MutableCollection<Attribute> {
            return MAP.values
        }

        @JvmStatic
        fun registerSelf() {
            if (!HexOPConfig.loaded()) throw RuntimeException("should init after config loaded!")
            // build
            _personal_media = makeRanged("personal_media", 0.0)
            _personal_media_max = makeRanged("personal_media_max", HexOPConfig.PersonalMediaMax().toDouble())
            _personal_media_regen = makeRanged("personal_media_regen", HexOPConfig.PersonalMediaRegenStep().toDouble())
            // register
            for (pair in MAP.entries) Registry.register(BuiltInRegistries.ATTRIBUTE, pair.key, pair.value)
        }
    }
}