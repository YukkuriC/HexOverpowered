package io.yukkuric.hexop.actions.mind_env

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.iota.PatternIota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster
import at.petrak.hexcasting.xplat.IXplatAbstractions

object OpMindPatterns : ConstMediaAction {
    override val argc = 0
    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        if (env.caster == null) throw MishapBadCaster()
        val patterns = IXplatAbstractions.INSTANCE.getPatternsSavedInUi(env.caster)
        return listOf(ListIota(patterns.map { rp -> PatternIota(rp.pattern) }))
    }
}