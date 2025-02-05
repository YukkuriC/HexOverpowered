package io.yukkuric.hexop.actions

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.env.CircleCastEnv
import at.petrak.hexcasting.api.casting.eval.env.PackagedItemCastEnv
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.item.MediaHolderItem
import at.petrak.hexcasting.api.misc.MediaConstants
import io.yukkuric.hexop.HexOverpowered.IsModLoaded
import ram.talia.hexal.api.casting.eval.env.WispCastEnv

object OpChargeMedia : ConstMediaAction {
    private val hasHexal = lazy { IsModLoaded("hexal") }
    private val MEDIA_TARGET = 114514 * MediaConstants.DUST_UNIT

    override val argc = 0

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
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