package io.yukkuric.hexop.actions.mind_env

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapDisallowedSpell
import at.petrak.hexcasting.api.spell.mishaps.MishapNotEnoughArgs
import at.petrak.hexcasting.xplat.IXplatAbstractions
import io.yukkuric.hexop.HexOPConfig
import java.util.function.BiFunction

enum class OpMindStackEdit(override val argc: Int, val stackOp: BiFunction<List<Iota>, MutableList<Iota>, List<Iota>>) :
    ConstMediaAction {
    PUSH(1, { args, stack ->
        stack.add(args[0])
        listOf()
    }),
    POP(0, { _, stack ->
        if (stack.isEmpty()) throw MishapNotEnoughArgs(1, 0)
        listOf(stack.removeAt(stack.size - 1))
    });

    override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
        commonCheckMindEnv(ctx)
        val image = IXplatAbstractions.INSTANCE.getHarness(ctx.caster, ctx.castingHand)
        var stack = image.stack
        val ret = stackOp.apply(args, stack)
        IXplatAbstractions.INSTANCE.setHarness(ctx.caster, image)
        return ret
    }

    companion object {
        fun commonCheckMindEnv(env: CastingContext) {
            if (!HexOPConfig.EnablesMindEnvActions()) throw MishapDisallowedSpell()
        }
    }
}