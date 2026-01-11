package io.yukkuric.hexop.helpers

import at.petrak.hexcasting.api.misc.FrozenColorizer
import at.petrak.hexcasting.api.misc.HexDamageSources
import at.petrak.hexcasting.api.spell.mishaps.Mishap
import at.petrak.hexcasting.api.spell.mishaps.MishapEvalTooDeep
import io.yukkuric.hexop.HexOPConfig
import io.yukkuric.hexop.mixin.accessor.AccessorMishap
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.DyeColor

fun AttackToTargetHealth(target: LivingEntity, newHealth: Float, caster: LivingEntity?) {
    Mishap.trulyHurt(
        target,
        HexDamageSources.overcastDamageFrom(caster),
        target.health - newHealth
    )
    if (HexOPConfig.TrulyHurtLevel() < 1) return
    if (target.health != newHealth) target.health = newHealth
}

// Mishap.dyeColor(color: DyeColor)
var mishapForPick: AccessorMishap? = null
fun GetPigment(color: DyeColor): FrozenColorizer {
    if (mishapForPick == null) mishapForPick = MishapEvalTooDeep() as AccessorMishap
    return mishapForPick!!.callDyeColor(color)
}