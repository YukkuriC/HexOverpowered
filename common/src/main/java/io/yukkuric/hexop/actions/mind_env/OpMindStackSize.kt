package io.yukkuric.hexop.actions.mind_env

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster
import at.petrak.hexcasting.xplat.IXplatAbstractions

object OpMindStackSize : ConstMediaAction {
    override val argc = 0
    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        if (env.caster == null) throw MishapBadCaster()
        val stack = IXplatAbstractions.INSTANCE.getStaffcastVM(env.caster, env.castingHand).image.stack
        return listOf(DoubleIota(stack.size.toDouble()))
    }
}