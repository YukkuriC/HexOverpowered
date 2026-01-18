package io.yukkuric.hexop.helpers.attack

import at.petrak.hexcasting.api.spell.mishaps.Mishap
import io.yukkuric.hexop.HexOPConfig
import io.yukkuric.hexop.helpers.GetOvercastSource
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.DoubleTag
import net.minecraft.nbt.FloatTag
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.vehicle.AbstractMinecart
import net.minecraft.world.entity.vehicle.Boat

object EntityHealthAccessors : IEntityHealthAccessor<Entity> {
    private const val CONTACT_ME = "contact the author for further TrulyHurt support :3"

    val SUBS = mutableListOf<IEntityHealthAccessor<*>>(
        LIVING,
        MINECART_BOAT,
    )
    private lateinit var _selected: IEntityHealthAccessor<*>
    private var _target: Entity? = null

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
        return _selected.getHealthT(target)
    }

    @Synchronized
    override fun setHealth(target: Entity, newHealth: Float, caster: LivingEntity?) {
        if (!validate(target)) return
        return _selected.setHealthT(target, newHealth, caster)
    }

    private fun sendContactMessage(caster: LivingEntity?, message: String) {
        caster?.sendSystemMessage(Component.literal("$message; $CONTACT_ME"))
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
            if (target.health <= newHealth || HexOPConfig.TrulyHurtLevel() < 1) return

            // lv.1: setHealth
            target.health = newHealth
            if (target.health <= newHealth || HexOPConfig.TrulyHurtLevel() < 2) return

            // lv.2: simple replace nbt
            val nbt = CompoundTag()
            target.save(nbt)
            for (key in ArrayList(nbt.allKeys)) {
                val sub = nbt.get(key)
                val tagHealth = when (sub) {
                    is DoubleTag -> sub.asDouble.toFloat()
                    is FloatTag -> sub.asFloat
                    else -> Float.NaN
                }
                if (tagHealth == target.health) {
                    if (sub is DoubleTag) nbt.putDouble(key, newHealth.toDouble())
                    else nbt.putFloat(key, newHealth)
                }
            }
            target.readAdditionalSaveData(nbt)
            if (target.health <= newHealth || HexOPConfig.TrulyHurtLevel() < 3) return

            // lv.3: coming soon
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
            var deltaDamage = (getHealthT(target) - newHealth) / DMG_SCALE
            if (newHealth <= 0) deltaDamage += EXTRA_DMG_FOR_BREAK
            target.hurt(GetOvercastSource(target, caster), deltaDamage)
            if (getHealthT(target) <= newHealth || HexOPConfig.TrulyHurtLevel() < 1) return
            sendContactMessage(
                caster,
                "Vehicle entity ${target}(class=${target.javaClass.name}) immune to overcast damage or has different breaking mechanics",
            )
        }
    }
}