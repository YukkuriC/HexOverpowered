package io.yukkuric.hexop.actions

import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.OperationResult
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import org.apache.commons.lang3.function.TriFunction

open class BaseSelectAction(
    val predicate: TriFunction<CastingEnvironment, CastingImage, SpellContinuation, Boolean>,
    val testTrue: Action,
    val testFalse: Action
) : Action {
    override fun operate(
        env: CastingEnvironment,
        image: CastingImage,
        continuation: SpellContinuation
    ): OperationResult {
        if (predicate.apply(env, image, continuation)) return testTrue.operate(env, image, continuation)
        return testFalse.operate(env, image, continuation)
    }
}