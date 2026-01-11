package io.yukkuric.hexop.actions.attack

import at.petrak.hexcasting.api.casting.ParticleSpray
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
import at.petrak.hexcasting.api.pigment.FrozenPigment
import io.yukkuric.hexop.HexOPConfig
import io.yukkuric.hexop.helpers.AttackToTargetHealth
import io.yukkuric.hexop.helpers.GetPigment
import io.yukkuric.hexop.helpers.PrimeChecker
import net.minecraft.world.item.DyeColor
import net.minecraft.world.phys.Vec3
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

object OpFactorCut : ConstMediaAction {
    override val argc
        get() = argcPreCheck
    override val mediaCost: Long
        get() = mediaCostResult.toLong()

    private var argcPreCheck = 1
    private var mediaCostResult = 0
    override fun operate(
        env: CastingEnvironment, image: CastingImage, continuation: SpellContinuation
    ): OperationResult {
        if (!HexOPConfig.EnablesFactorCutSpell()) throw MishapDisallowedSpell()
        if (image.stack.isEmpty()) throw MishapNotEnoughArgs(1, 0)
        val first = image.stack.last()
        if (first is EntityIota) argcPreCheck = 1
        else if (first is DoubleIota) argcPreCheck = 2
        else throw MishapInvalidIota.of(first, 0, "entity_or_int")
        return super.operate(env, image, continuation)
    }

    private val sprayDirections = listOf(1.5, -1.0)
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
        val sprays = mutableListOf<Pair<ParticleSpray, FrozenPigment>>()
        val srcPos = target.position()
        val centerPos = target.position().add(0.0, target.boundingBox.ysize / 2, 0.0)
        // pick random dir
        val theta = acos(2 * Math.random() - 1)
        val phi = 2 * Math.PI * Math.random()
        val deltaVec = Vec3(sin(theta) * cos(phi), sin(theta) * sin(phi), cos(theta))
        if (healthAsInt <= 1) {
            newHealth = 0
            mediaCostResult = 0
            for (scale in sprayDirections) {
                sprays.add(
                    Pair(
                        ParticleSpray(centerPos, deltaVec.scale(scale * 1.2), 0.3, 1.0, 20),
                        GetPigment(DyeColor.PURPLE)
                    )
                )
            }
            sprays.add(Pair(ParticleSpray.burst(centerPos, 1.0, 30), GetPigment(DyeColor.RED)))
        } else if (factor > 1 && healthAsInt % factor == 0) {
            newHealth = healthAsInt / factor
            val isPrime = PrimeChecker.isPrime(factor)
            mediaCostResult = if (isPrime) HexOPConfig.FactorCutPrimeCost()
            else HexOPConfig.FactorCutNonPrimeCostScale() * factor
            for (scale in sprayDirections) {
                sprays.add(
                    Pair(
                        ParticleSpray(centerPos, deltaVec.scale(scale * 2), 0.2, 0.3, 20),
                        GetPigment(DyeColor.GREEN)
                    )
                )
            }
        } else if (factor == 1) {
            newHealth--
            mediaCostResult = HexOPConfig.FactorCutFallbackCost()
            for (scale in sprayDirections) {
                sprays.add(
                    Pair(
                        ParticleSpray(centerPos, deltaVec.scale(scale), 0.3, 1.0, 10),
                        GetPigment(DyeColor.YELLOW)
                    )
                )
            }
        } else throw MishapInvalidIota.of(args[1], 0, "divisor")
        // random reduction
        if (HexOPConfig.FactorCutRandomMode() && Math.random() < 0.5) newHealth--

        // check first, execute later like spell actions
        if (env.extractMedia(mediaCost, true) > 0) throw MishapNotEnoughMedia(mediaCost)
        AttackToTargetHealth(target, newHealth.toFloat(), env.castingEntity)
        for (pair in sprays) {
            pair.first.sprayParticles(env.world, pair.second)
        }
        return listOf(DoubleIota(newHealth.toDouble()))
    }
}