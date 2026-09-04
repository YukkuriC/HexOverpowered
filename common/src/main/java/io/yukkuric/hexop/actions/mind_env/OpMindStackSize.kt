package io.yukkuric.hexop.actions.mind_env

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.xplat.IXplatAbstractions
import io.yukkuric.hexop.actions.mind_env.OpMindStackEdit.Companion.commonCheckMindEnv
import io.yukkuric.yclib_kt.legacy.caster

object OpMindStackSize : ConstMediaAction {
    override val argc = 0
    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        commonCheckMindEnv(env)
        val stack = IXplatAbstractions.INSTANCE.getStaffcastVM(env.caster, env.castingHand).image.stack
        return listOf(DoubleIota(stack.size.toDouble()))
    }
}