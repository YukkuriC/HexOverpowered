package io.yukkuric.hexop.helpers

import at.petrak.hexcasting.api.casting.mishaps.Mishap
import at.petrak.hexcasting.common.lib.HexDamageTypes
import io.yukkuric.hexop.HexOPConfig
import io.yukkuric.hexop.mixin.accessor.AccessorDamageSources
import net.minecraft.world.entity.LivingEntity

fun AttackToTargetHealth(target: LivingEntity, newHealth: Float, caster: LivingEntity?) {
    Mishap.trulyHurt(
        target,
        (target.damageSources() as AccessorDamageSources).callSource(HexDamageTypes.OVERCAST, caster),
        target.health - newHealth
    )
    if (HexOPConfig.TrulyHurtLevel() < 1) return
    if (target.health != newHealth) target.health = newHealth
}