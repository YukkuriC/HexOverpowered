package io.yukkuric.hexop.helpers

import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster
import at.petrak.hexcasting.api.pigment.FrozenPigment
import at.petrak.hexcasting.common.lib.HexDamageTypes
import io.yukkuric.hexop.mixin.accessor.AccessorDamageSources
import io.yukkuric.hexop.mixin.accessor.AccessorMishap
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.DyeColor

fun GetOvercastSource(target: Entity, caster: LivingEntity?) =
    (target.damageSources() as AccessorDamageSources).callSource(HexDamageTypes.OVERCAST, caster)

// Mishap.dyeColor(color: DyeColor)
var mishapForPick: AccessorMishap? = null
fun GetPigment(color: DyeColor): FrozenPigment {
    if (mishapForPick == null) mishapForPick = MishapBadCaster() as AccessorMishap
    return mishapForPick!!.callDyeColor(color)
}