package io.yukkuric.hexop.actions.mind_env

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.DoubleIota
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.xplat.IXplatAbstractions
import io.yukkuric.hexop.actions.mind_env.OpMindStackEdit.Companion.commonCheckMindEnv

object OpMindStackSize : ConstMediaAction {
    override val argc = 0
    override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
        commonCheckMindEnv(ctx)
        val stack = IXplatAbstractions.INSTANCE.getHarness(ctx.caster, ctx.castingHand).stack
        return listOf(DoubleIota(stack.size.toDouble()))
    }
}