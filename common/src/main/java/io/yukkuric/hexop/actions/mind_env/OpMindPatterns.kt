package io.yukkuric.hexop.actions.mind_env

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.ListIota
import at.petrak.hexcasting.api.spell.iota.PatternIota
import at.petrak.hexcasting.xplat.IXplatAbstractions
import io.yukkuric.hexop.actions.mind_env.OpMindStackEdit.Companion.commonCheckMindEnv

object OpMindPatterns : ConstMediaAction {
    override val argc = 0
    override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
        commonCheckMindEnv(ctx)
        val patterns = IXplatAbstractions.INSTANCE.getPatterns(ctx.caster)
        return listOf(ListIota(patterns.map { rp -> PatternIota(rp.pattern) }))
    }
}