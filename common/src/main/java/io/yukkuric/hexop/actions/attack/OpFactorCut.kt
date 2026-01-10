package io.yukkuric.hexop.actions.attack

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.OperationResult
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.api.casting.getInt
import at.petrak.hexcasting.api.casting.getLivingEntityButNotArmorStand
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapDisallowedSpell
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughMedia
import io.yukkuric.hexop.HexOPConfig
import io.yukkuric.hexop.helpers.AttackToTargetHealth
import io.yukkuric.hexop.helpers.PrimeChecker

object OpFactorCut : ConstMediaAction {
    override val argc
        get() = argcPreCheck
    override val mediaCost: Long
        get() = mediaCostResult.toLong()

    private var argcPreCheck = 1
    private var mediaCostResult = 0
    override fun operate(
        env: CastingEnvironment,
        image: CastingImage,
        continuation: SpellContinuation
    ): OperationResult {
        if (!HexOPConfig.EnablesFactorCutSpell()) throw MishapDisallowedSpell()
        if (image.stack.isEmpty()) throw MishapNotEnoughArgs(1, 0)
        val first = image.stack.last()
        if (first is EntityIota) argcPreCheck = 1
        else if (first is DoubleIota) argcPreCheck = 2
        else throw MishapInvalidIota.of(first, 0, "entity_or_int")
        return super.operate(env, image, continuation)
    }

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
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

        // check first, execute later like spell actions
        if (env.extractMedia(mediaCost, true) > 0) throw MishapNotEnoughMedia(mediaCost)
        if (newHealth != healthAsInt)
            AttackToTargetHealth(target, newHealth.toFloat(), env.castingEntity)
        return listOf(DoubleIota(newHealth.toDouble()))
    }
}