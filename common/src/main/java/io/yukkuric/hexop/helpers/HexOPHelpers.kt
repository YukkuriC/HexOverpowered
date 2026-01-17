package io.yukkuric.hexop.helpers

import at.petrak.hexcasting.api.misc.FrozenColorizer
import at.petrak.hexcasting.api.misc.HexDamageSources
import at.petrak.hexcasting.api.spell.mishaps.MishapEvalTooDeep
import io.yukkuric.hexop.mixin.accessor.AccessorMishap
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.DyeColor

fun GetOvercastSource(target: Entity, caster: LivingEntity?) =
    HexDamageSources.overcastDamageFrom(caster)

// Mishap.dyeColor(color: DyeColor)
var mishapForPick: AccessorMishap? = null
fun GetPigment(color: DyeColor): FrozenColorizer {
    if (mishapForPick == null) mishapForPick = MishapEvalTooDeep() as AccessorMishap
    return mishapForPick!!.callDyeColor(color)
}