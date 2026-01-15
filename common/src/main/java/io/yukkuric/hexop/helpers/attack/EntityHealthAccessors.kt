package io.yukkuric.hexop.helpers.attack

import at.petrak.hexcasting.api.casting.mishaps.Mishap
import io.yukkuric.hexop.HexOPConfig
import io.yukkuric.hexop.helpers.GetOvercastSource
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.DoubleTag
import net.minecraft.nbt.FloatTag
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.vehicle.AbstractMinecart
import net.minecraft.world.entity.vehicle.Boat

object EntityHealthAccessors : IEntityHealthAccessor<Entity> {
    val SUBS = mutableListOf<IEntityHealthAccessor<*>>(
        LIVING,
        MINECART_BOAT,
    )
    private lateinit var _selected: IEntityHealthAccessor<*>
    private lateinit var _target: Entity

    override fun validate(target: Entity): Boolean {
        if (target == _target) return true
        for (sub in SUBS) if (sub.validate(target)) {
            _selected = sub
            _target = target
            return true
        }
        return false
    }

    @Synchronized
    override fun getHealth(target: Entity): Float {
        if (!validate(target)) return Float.NaN
        return _selected.getHealth(_target)
    }

    @Synchronized
    override fun setHealth(target: Entity, newHealth: Float, caster: LivingEntity?) {
        if (!validate(target)) return
        return _selected.setHealth(target, newHealth, caster)
    }

    // ==================== sub entities ====================
    object LIVING : IEntityHealthAccessor<LivingEntity> {
        override fun validate(target: Entity) = target is LivingEntity
        override fun getHealth(target: LivingEntity) = target.health
        override fun setHealth(target: LivingEntity, newHealth: Float, caster: LivingEntity?) {
            Mishap.trulyHurt(
                target,
                GetOvercastSource(target, caster),
                target.health - newHealth
            )
            if (target.health == newHealth || HexOPConfig.TrulyHurtLevel() < 1) return
            target.health = newHealth
            if (target.health == newHealth || HexOPConfig.TrulyHurtLevel() < 2) return
            val nbt = CompoundTag()
            target.save(nbt)
            for (key in ArrayList(nbt.allKeys)) {
                val sub = nbt.get(key)
                val tagHealth = when {
                    sub is DoubleTag -> sub.asDouble.toFloat()
                    sub is FloatTag -> sub.asFloat
                    else -> Float.NaN
                }
                if (tagHealth == target.health) {
                    if (sub is DoubleTag) nbt.putDouble(key, newHealth.toDouble())
                    else nbt.putFloat(key, newHealth)
                }
            }
            target.load(nbt)
        }
    }

    object MINECART_BOAT : IEntityHealthAccessor<Entity> {
        private const val MAX_DMG = 40f
        private const val DMG_SCALE = 10f
        private const val EXTRA_DMG_FOR_BREAK = 0.1f
        override fun validate(target: Entity) = target is AbstractMinecart || target is Boat
        override fun getHealth(target: Entity) = MAX_DMG - when {
            target is AbstractMinecart -> target.damage
            else -> (target as Boat).damage
        }

        override fun setHealth(target: Entity, newHealth: Float, caster: LivingEntity?) {
            val deltaDamage = (getHealth(target) - newHealth) / DMG_SCALE + EXTRA_DMG_FOR_BREAK
            target.hurt(GetOvercastSource(target, caster), deltaDamage)
            if (getHealth(target) <= newHealth || HexOPConfig.TrulyHurtLevel() < 1) return
            throw RuntimeException("Vehicle entity ${target}(class=${target.javaClass.name}) immune to overcast damage; contact the author for further TrulyHurt support :3")
        }
    }
}