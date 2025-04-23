package io.yukkuric.hexop.personal_mana

import at.petrak.hexcasting.api.addldata.ADMediaHolder
import io.yukkuric.hexop.HexOPAttributes
import net.minecraft.world.entity.player.Player
import java.util.WeakHashMap

class PersonalManaHolder private constructor(val player: Player) : ADMediaHolder {
    override fun getMedia() = player.getAttributeBaseValue(HexOPAttributes.PERSONAL_MEDIA).toLong()
    override fun getMaxMedia() = player.getAttributeBaseValue(HexOPAttributes.PERSONAL_MEDIA_MAX).toLong()
    override fun setMedia(value: Long) {
        player.getAttribute(HexOPAttributes.PERSONAL_MEDIA)?.baseValue = value.coerceAtLeast(0).toDouble()
    }

    override fun canRecharge() = false
    override fun canProvide() = true
    override fun getConsumptionPriority() = Int.MAX_VALUE
    override fun canConstructBattery() = true // try it lol

    companion object {
        private val MAP = WeakHashMap<Player, PersonalManaHolder>()

        @JvmStatic
        fun get(player: Player?): PersonalManaHolder? = if (player == null) {
            null
        } else {
            MAP.computeIfAbsent(player, ::PersonalManaHolder)
        }
    }
}