package io.yukkuric.hexop.personal_mana

import at.petrak.hexcasting.api.HexAPI
import at.petrak.hexcasting.api.addldata.ADMediaHolder
import io.yukkuric.hexop.HexOPAttributes
import io.yukkuric.hexop.HexOPConfig
import net.minecraft.advancements.Advancement
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import java.util.*


class PersonalManaHolder(val player: Player) : ADMediaHolder {
    override fun getMedia(): Long {
        tryContinue()
        return player.getAttributeBaseValue(HexOPAttributes.PERSONAL_MEDIA).toLong()
    }

    override fun getMaxMedia() = player.getAttributeValue(HexOPAttributes.PERSONAL_MEDIA_MAX).toLong()
    override fun setMedia(value: Long) {
        val target = value.coerceAtLeast(0).toDouble()
        player.getAttribute(HexOPAttributes.PERSONAL_MEDIA)?.baseValue = target
        CachedPlayerMediaMap[player.uuid] = target
    }

    fun tryContinue() {
        var attr = player.getAttribute(HexOPAttributes.PERSONAL_MEDIA) ?: return
        if (attr.baseValue != HexOPAttributes.INIT_MEDIA_MARKER) return
        attr.baseValue = CachedPlayerMediaMap[player.uuid] ?: 0.0
    }

    private var triggersEvent = true
    override fun insertMedia(amount: Long, simulate: Boolean): Long {
        val ret = super.insertMedia(amount, simulate)
        if (triggersEvent && !simulate) {
            tryTriggerEvent {
                PersonalManaEvents.OnInsert(PersonalManaEvents.EventBody(this, amount, ret))
            }
        }
        return ret
    }

    override fun withdrawMedia(cost: Long, simulate: Boolean): Long {
        val ret = super.withdrawMedia(cost, simulate)
        if (triggersEvent && !simulate) {
            tryTriggerEvent {
                PersonalManaEvents.OnExtract(PersonalManaEvents.EventBody(this, cost, ret))
            }
        }
        return ret
    }

    private fun tryTriggerEvent(action: Runnable) {
        if (!HexOPConfig.FiresPersonalMediaEvents()) return
        triggersEvent = false
        try {
            action.run()
        } catch (e: Throwable) {
            player.sendSystemMessage(Component.literal("[PersonalMedia] error: $e"))
            PersonalManaEvents.resetAll()
        }
        triggersEvent = true
    }

    fun getMediaRegenStep() = player.getAttributeValue(HexOPAttributes.PERSONAL_MEDIA_REGEN).toLong()

    override fun canRecharge() = false
    override fun canProvide() = true
    override fun getConsumptionPriority() = Int.MAX_VALUE
    override fun canConstructBattery() = true // try it lol

    companion object {
        private val CachedPlayerMediaMap = HashMap<UUID, Double>()
        private var cachedAdvancement: Advancement? = null

        @JvmStatic
        fun get(player: Player?): PersonalManaHolder? =
            if (player == null) null
            else (player as PlayerHolder).getPersonalMediaHolder()

        @JvmStatic
        fun enablesManaForPlayer(player: Player): Boolean {
            if (!HexOPConfig.EnablesPersonalMediaPool()) return false
            if (player !is ServerPlayer) return false
            if (!HexOPConfig.PersonalMediaAfterEnlightened()) return true
            // https://github.com/FallingColors/HexMod/blob/main/Common/src/main/java/at/petrak/hexcasting/api/casting/eval/CastingEnvironment.java#L232
            if (cachedAdvancement == null) cachedAdvancement =
                player.server.advancements.getAdvancement(HexAPI.modLoc("enlightenment")) ?: return false
            return player.advancements.getOrStartProgress(cachedAdvancement).isDone
        }
    }

    interface PlayerHolder {
        fun getPersonalMediaHolder(): PersonalManaHolder
    }
}