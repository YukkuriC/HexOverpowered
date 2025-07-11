package io.yukkuric.hexop.personal_mana

import at.petrak.hexcasting.api.addldata.ADMediaHolder
import io.yukkuric.hexop.HexOPAttributes
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Player
import java.util.*

class PersonalManaHolder private constructor(val player: Player) : ADMediaHolder {
    override fun getMedia() = player.getAttributeBaseValue(HexOPAttributes.PERSONAL_MEDIA).toInt()
    override fun getMaxMedia() = player.getAttributeValue(HexOPAttributes.PERSONAL_MEDIA_MAX).toInt()
    override fun setMedia(value: Int) {
        player.getAttribute(HexOPAttributes.PERSONAL_MEDIA)?.baseValue = value.coerceAtLeast(0).toDouble()
    }

    var triggersEvent = true
    override fun insertMedia(amount: Int, simulate: Boolean): Int {
        val ret = super.insertMedia(amount, simulate)
        if (triggersEvent && !simulate) {
            tryTriggerEvent {
                PersonalManaEvents.OnInsert(PersonalManaEvents.EventInsert(player, amount, ret))
            }
        }
        return ret
    }

    override fun withdrawMedia(cost: Int, simulate: Boolean): Int {
        val ret = super.withdrawMedia(cost, simulate)
        if (triggersEvent && !simulate) {
            tryTriggerEvent {
                PersonalManaEvents.OnExtract(PersonalManaEvents.EventExtract(player, cost, ret))
            }
        }
        return ret
    }

    private fun tryTriggerEvent(action: Runnable) {
        triggersEvent = false
        try {
            action.run()
        } catch (e: Throwable) {
            player.sendSystemMessage(Component.literal("[PersonalMedia] error: $e"))
            PersonalManaEvents.resetAll()
        }
        triggersEvent = true
    }

    fun getMediaRegenStep() = player.getAttributeValue(HexOPAttributes.PERSONAL_MEDIA_REGEN).toInt()

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