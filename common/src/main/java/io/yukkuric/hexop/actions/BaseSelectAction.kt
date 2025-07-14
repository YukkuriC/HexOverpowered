package io.yukkuric.hexop.actions

import at.petrak.hexcasting.api.spell.Action
import at.petrak.hexcasting.api.spell.OperationResult
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.casting.eval.SpellContinuation
import at.petrak.hexcasting.api.spell.iota.Iota

open class BaseSelectAction(
    val predicate: QuadFunction<SpellContinuation, MutableList<Iota>, Iota?, CastingContext, Boolean>,
    val testTrue: Action,
    val testFalse: Action
) : Action {
    override fun operate(
        continuation: SpellContinuation, stack: MutableList<Iota>, ravenmind: Iota?, ctx: CastingContext
    ): OperationResult {
        if (predicate.apply(continuation, stack, ravenmind, ctx))
            return testTrue.operate(continuation, stack, ravenmind, ctx)
        return testFalse.operate(continuation, stack, ravenmind, ctx)
    }

    fun interface QuadFunction<P1, P2, P3, P4, R> {
        fun apply(p1: P1, p2: P2, p3: P3, p4: P4): R
    }
}