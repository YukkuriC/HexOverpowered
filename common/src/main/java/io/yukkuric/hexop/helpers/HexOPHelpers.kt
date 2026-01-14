package io.yukkuric.hexop.helpers

import at.petrak.hexcasting.api.casting.mishaps.Mishap
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster
import at.petrak.hexcasting.api.pigment.FrozenPigment
import at.petrak.hexcasting.common.lib.HexDamageTypes
import io.yukkuric.hexop.HexOPConfig
import io.yukkuric.hexop.mixin.accessor.AccessorDamageSources
import io.yukkuric.hexop.mixin.accessor.AccessorMishap
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.DoubleTag
import net.minecraft.nbt.FloatTag
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
    if (HexOPConfig.TrulyHurtLevel() < 2) return
    if (target.health != newHealth) {
//        val dbg = Minecraft.getInstance().player!!
        val nbt = CompoundTag()
        target.save(nbt)
//        dbg.sendSystemMessage(Component.literal("target=$newHealth actual=${target.health}"))
        for (key in ArrayList(nbt.allKeys)) {
            val sub = nbt.get(key)
            val tagHealth = when {
                sub is DoubleTag -> sub.asDouble.toFloat()
                sub is FloatTag -> sub.asFloat
                else -> Float.NaN
            }
//            dbg.sendSystemMessage(Component.literal("key=$key val=$tagHealth"))
            if (tagHealth == target.health) {
                if (sub is DoubleTag) nbt.putDouble(key, newHealth.toDouble())
                else nbt.putFloat(key, newHealth)
//                dbg.sendSystemMessage(Component.literal("set $key to $newHealth"))
            }
        }
        target.load(nbt)
    }
}

// Mishap.dyeColor(color: DyeColor)
var mishapForPick: AccessorMishap? = null
fun GetPigment(color: DyeColor): FrozenPigment {
    if (mishapForPick == null) mishapForPick = MishapBadCaster() as AccessorMishap
    return mishapForPick!!.callDyeColor(color)
}