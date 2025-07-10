package io.yukkuric.hexop.actions

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.env.CircleCastEnv
import at.petrak.hexcasting.api.casting.eval.env.PackagedItemCastEnv
import at.petrak.hexcasting.api.casting.eval.env.PlayerBasedCastEnv
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.item.MediaHolderItem
import at.petrak.hexcasting.api.misc.MediaConstants
import io.yukkuric.hexop.HexOPConfig
import io.yukkuric.hexop.HexOverpowered.IsModLoaded
import io.yukkuric.hexop.actions.OpChargeMedia.OpChargeForReal.rechargePersonalMedia
import io.yukkuric.hexop.personal_mana.PersonalManaHolder
import net.minecraft.server.level.ServerPlayer
import ram.talia.hexal.api.casting.eval.env.WispCastEnv

object OpChargeMedia : BaseSelectAction(
    { _, _, _ -> HexOPConfig.EnablesChargeMediaAction() },
    OpChargeForReal,
    OpChargeFake,
) {
    private val hasHexal = lazy { IsModLoaded("hexal") }
    private const val MEDIA_TARGET = 114514 * MediaConstants.DUST_UNIT

    object OpChargeForReal : ConstMediaAction {
        override val argc = 0

        private fun rechargePersonalMedia(caster: ServerPlayer?) {
            val holder = PersonalManaHolder.get(caster) ?: return
            holder.media = MEDIA_TARGET.coerceAtMost(holder.maxMedia)
        }

        override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
            if (env is PlayerBasedCastEnv) rechargePersonalMedia(env.caster)

            if (env is PackagedItemCastEnv) {
                val stack = env.caster?.getItemInHand(env.castingHand)
                val item = stack?.item
                if (item is MediaHolderItem) {
                    item.setMedia(stack, MEDIA_TARGET)
                }
            } else if (env is CircleCastEnv) {
                env.impetus?.media = MEDIA_TARGET
            } else if (hasHexal.value && env is WispCastEnv) {
                env.wisp.media = MEDIA_TARGET
            }
            return listOf()
        }
    }

    object OpChargeFake : ConstMediaAction {
        override val argc = 0

        override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
            // TODO: play sound
            return listOf()
        }
    }
}