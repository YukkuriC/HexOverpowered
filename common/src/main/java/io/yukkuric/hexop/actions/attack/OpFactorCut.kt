package io.yukkuric.hexop.actions.attack

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.OperationResult
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.casting.eval.SpellContinuation
import at.petrak.hexcasting.api.spell.getInt
import at.petrak.hexcasting.api.spell.getLivingEntityButNotArmorStand
import at.petrak.hexcasting.api.spell.iota.DoubleIota
import at.petrak.hexcasting.api.spell.iota.EntityIota
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapDisallowedSpell
import at.petrak.hexcasting.api.spell.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.spell.mishaps.MishapNotEnoughArgs
import io.yukkuric.hexop.HexOPConfig
import io.yukkuric.hexop.helpers.AttackToTargetHealth
import io.yukkuric.hexop.helpers.PrimeChecker

object OpFactorCut : ConstMediaAction {
    override val argc
        get() = argcPreCheck
    override val mediaCost
        get() = mediaCostResult

    private var argcPreCheck = 1
    private var mediaCostResult = 0
    override fun operate(
        continuation: SpellContinuation,
        stack: MutableList<Iota>,
        ravenmind: Iota?,
        ctx: at.petrak.hexcasting.api.spell.casting.CastingContext
    ): OperationResult {
        if (!HexOPConfig.EnablesFactorCutSpell()) throw MishapDisallowedSpell()
        if (stack.isEmpty()) throw MishapNotEnoughArgs(1, 0)
        val first = stack.last()
        if (first is EntityIota) argcPreCheck = 1
        else if (first is DoubleIota) argcPreCheck = 2
        else throw MishapInvalidIota.of(first, 0, "entity_or_int")
        return super.operate(continuation, stack, ravenmind, ctx)
    }

    override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
        val env = ctx
        val target = args.getLivingEntityButNotArmorStand(0, args.size)
        val healthAsInt = target.health.toInt()

        // query health
        if (args.size == 1) {
            return listOf(DoubleIota(healthAsInt.toDouble()))
        }

        // health cut
        val factor = args.getInt(1, args.size)
        var newHealth: Int = healthAsInt
        if (healthAsInt <= 1) {
            newHealth = 0
            mediaCostResult = 0
        } else if (factor > 1 && healthAsInt % factor == 0) {
            newHealth = healthAsInt / factor
            val isPrime = PrimeChecker.isPrime(factor)
            mediaCostResult =
                if (isPrime) HexOPConfig.FactorCutPrimeCost()
                else HexOPConfig.FactorCutNonPrimeCostScale() * factor
        } else if (factor == 1) {
            newHealth--
            mediaCostResult = HexOPConfig.FactorCutFallbackCost()
        } else throw MishapInvalidIota.of(args[1], 0, "divisor")
        // random reduction
        if (HexOPConfig.FactorCutRandomMode() && Math.random() < 0.5) newHealth--

        // nope, 1.19 has no "simulate" drain
        // if (env.extractMedia(mediaCost, true) > 0) throw MishapNotEnoughMedia(mediaCost)
        if (newHealth != healthAsInt)
            AttackToTargetHealth(target, newHealth.toFloat(), env.caster)
        return listOf(DoubleIota(newHealth.toDouble()))
    }
}