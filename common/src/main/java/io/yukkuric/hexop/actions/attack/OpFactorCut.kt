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
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
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
        if (image.stack.isEmpty()) throw MishapNotEnoughArgs(1, 0)
        val first = image.stack[0]
        if (first is EntityIota) argcPreCheck = 1
        else if (first is DoubleIota) argcPreCheck = 2
        else throw MishapInvalidIota.of(first, 0, "entity_or_int")
        return super.operate(env, image, continuation)
    }

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val entityIdx = args.size - 1
        val target = args.getLivingEntityButNotArmorStand(args.size - 1)
        val healthAsInt = target.health.toInt()

        // query health
        if (entityIdx == 0) {
            return listOf(DoubleIota(healthAsInt.toDouble()))
        }

        // health cut
        val factor = args.getInt(0)
        var newHealth: Int = healthAsInt
        if (healthAsInt <= 1) {
            newHealth = 0
            mediaCostResult = 0
        } else if (factor > 1 && healthAsInt % factor == 0) {
            newHealth = healthAsInt / factor
            val isPrime = PrimeChecker.isPrime(factor)
            // TODO prime factor cost less
        } else {
            newHealth--
            // TODO cost for attack 1
        }
        AttackToTargetHealth(target, newHealth.toFloat(), env.castingEntity)
        return listOf()
    }
}