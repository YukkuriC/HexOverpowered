package io.yukkuric.hexop.ext

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.env.PlayerBasedCastEnv
import at.petrak.hexcasting.api.casting.eval.sideeffects.OperatorSideEffect
import net.minecraft.network.chat.Component

class SilencedCastingEnv(val master: PlayerBasedCastEnv) :
    PlayerBasedCastEnv(master.caster, master.castingHand) {
    override fun extractMediaEnvironment(amount: Long, simulate: Boolean) = master.extractMedia(amount, simulate)
    override fun getCastingHand() = master.castingHand
    override fun getPigment() = master.pigment

    override fun printMessage(message: Component?) {}
    override fun sendMishapMsgToPlayer(mishap: OperatorSideEffect.DoMishap?) {}

    companion object {
        fun from(env: CastingEnvironment): CastingEnvironment {
            if (env is SilencedCastingEnv) return env
            if (env is PlayerBasedCastEnv) return SilencedCastingEnv(env)
            return env
        }
    }
}