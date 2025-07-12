package io.yukkuric.hexop.actions

import at.petrak.hexcasting.api.spell.Action
import at.petrak.hexcasting.api.spell.OperationResult
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.casting.eval.SpellContinuation
import at.petrak.hexcasting.api.spell.iota.Iota
import java.util.function.BiFunction

open class BaseSelectAction(
    val predicate: BiFunction<CastingContext, SpellContinuation, Boolean>,
    val testTrue: Action,
    val testFalse: Action
) : Action {
    override fun operate(
        continuation: SpellContinuation, stack: MutableList<Iota>, ravenmind: Iota?, ctx: CastingContext
    ): OperationResult {
        if (predicate.apply(ctx, continuation)) return testTrue.operate(continuation, stack, ravenmind, ctx)
        return testFalse.operate(continuation, stack, ravenmind, ctx)
    }
}