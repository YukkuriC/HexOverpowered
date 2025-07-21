package io.yukkuric.hexop.actions.mind_env

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.iota.PatternIota
import at.petrak.hexcasting.xplat.IXplatAbstractions
import io.yukkuric.hexop.actions.mind_env.OpMindStackEdit.Companion.commonCheckMindEnv

object OpMindPatterns : ConstMediaAction {
    override val argc = 0
    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        commonCheckMindEnv(env)
        val patterns = IXplatAbstractions.INSTANCE.getPatternsSavedInUi(env.caster)
        return listOf(ListIota(patterns.map { rp -> PatternIota(rp.pattern) }))
    }
}