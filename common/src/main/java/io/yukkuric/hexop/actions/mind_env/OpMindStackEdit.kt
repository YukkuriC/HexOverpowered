package io.yukkuric.hexop.actions.mind_env

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster
import at.petrak.hexcasting.api.casting.mishaps.MishapDisallowedSpell
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
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

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        commonCheckMindEnv(env)
        val image = IXplatAbstractions.INSTANCE.getStaffcastVM(env.caster, env.castingHand).image
        var stack = image.stack
        if (stack !is MutableList<*>) stack = ArrayList(stack)
        val ret = stackOp.apply(args, stack as MutableList<Iota>)
        IXplatAbstractions.INSTANCE.setStaffcastImage(env.caster, image.copy(stack))
        return ret
    }

    companion object {
        fun commonCheckMindEnv(env: CastingEnvironment) {
            if (!HexOPConfig.EnablesMindEnvActions()) throw MishapDisallowedSpell()
            if (env.caster == null) throw MishapBadCaster()
        }
    }
}