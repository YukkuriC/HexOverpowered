package io.yukkuric.hexop.helpers

import at.petrak.hexcasting.api.casting.mishaps.Mishap
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster
import at.petrak.hexcasting.api.pigment.FrozenPigment
import at.petrak.hexcasting.common.lib.HexDamageTypes
import io.yukkuric.hexop.HexOPConfig
import io.yukkuric.hexop.mixin.accessor.AccessorDamageSources
import io.yukkuric.hexop.mixin.accessor.AccessorMishap
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.DyeColor

fun AttackToTargetHealth(target: LivingEntity, newHealth: Float, caster: LivingEntity?) {
    Mishap.trulyHurt(
        target,
        (target.damageSources() as AccessorDamageSources).callSource(HexDamageTypes.OVERCAST, caster),
        target.health - newHealth
    )
    if (HexOPConfig.TrulyHurtLevel() < 1) return
    if (target.health != newHealth) target.health = newHealth
}

// Mishap.dyeColor(color: DyeColor)
var mishapForPick: AccessorMishap? = null
fun GetPigment(color: DyeColor): FrozenPigment {
    if (mishapForPick == null) mishapForPick = MishapBadCaster() as AccessorMishap
    return mishapForPick!!.callDyeColor(color)
}