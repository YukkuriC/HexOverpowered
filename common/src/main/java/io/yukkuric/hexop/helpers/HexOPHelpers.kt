package io.yukkuric.hexop.helpers

import at.petrak.hexcasting.api.misc.HexDamageSources
import at.petrak.hexcasting.api.spell.mishaps.Mishap
import io.yukkuric.hexop.HexOPConfig
import net.minecraft.world.entity.LivingEntity

fun AttackToTargetHealth(target: LivingEntity, newHealth: Float, caster: LivingEntity?) {
    Mishap.trulyHurt(
        target,
        HexDamageSources.overcastDamageFrom(caster),
        target.health - newHealth
    )
    if (HexOPConfig.TrulyHurtLevel() < 1) return
    if (target.health != newHealth) target.health = newHealth
}