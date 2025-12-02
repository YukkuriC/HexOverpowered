package io.yukkuric.hexop.actions.mind_env

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.ListIota
import at.petrak.hexcasting.api.spell.iota.NullIota

object OpScheduledCode : ConstMediaAction {
    override val argc = 0

    override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
        val env = ctx
        val data = OpScheduleCall.QueryScheduledCode(env)
        return listOf(if (data == null) NullIota() else ListIota(data))
    }
}