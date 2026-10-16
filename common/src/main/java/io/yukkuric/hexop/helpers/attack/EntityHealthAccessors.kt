package io.yukkuric.hexop.helpers.attack

import io.yukkuric.hexop.HexOPConfig
import io.yukkuric.hexop.helpers.GetOvercastSource
import io.yukkuric.hexop.mixin.accessor.AccessorLivingEntity
import io.yukkuric.hexop.mixin_interface.MobSkipper
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.nbt.*
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.*
import net.minecraft.world.entity.vehicle.AbstractMinecart
import net.minecraft.world.entity.vehicle.Boat
import net.minecraft.world.level.gameevent.GameEvent

object EntityHealthAccessors : IEntityHealthAccessor<Entity> {
    private const val CONTACT_ME = "contact the author for further TrulyHurt support :3"

    @JvmStatic
    fun AddSub(sub: IEntityHealthAccessor<*>) = SUBS.add(sub)
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
        override fun getHealth(target: LivingEntity) =
            if (target.isRemoved) 0f else target.health

        override fun setHealth(target: LivingEntity, newHealth: Float, caster: LivingEntity?) {
            val dmgSrc = GetOvercastSource(target, caster)
            val usedLevel = setHealthInner(target, newHealth, dmgSrc)
            if (usedLevel > 0 && target.isDeadOrDying && !(target as AccessorLivingEntity).dead) {
                if (caster is ServerPlayer) CriteriaTriggers.PLAYER_KILLED_ENTITY.trigger(caster, target, dmgSrc)
                target.gameEvent(GameEvent.ENTITY_DIE)
                (target as AccessorLivingEntity).callDropAllDeathLoot(dmgSrc)
                target.level.broadcastEntityEvent(target, 3)
                target.pose = Pose.DYING
            }
        }

        private fun setHealthInner(
            target: LivingEntity,
            newHealth: Float,
            dmgSrc: DamageSource
        ): Int {
            val configLevel = HexOPConfig.TrulyHurtLevel()
            fun checkPass(limitLvl: Int): Boolean {
                if (target.health <= newHealth) return true
                if (target.isRemoved) return true
                return configLevel <= limitLvl
            }

            target.invulnerableTime = 0
            target.hurt(dmgSrc, target.health - newHealth)
            if (checkPass(0)) return 0

            // lv.1: setHealth
            target.health = newHealth
            if (checkPass(1)) return 1

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
            if (checkPass(2)) return 2

            // lv.3: force skipped
            (target as? MobSkipper)?.let {
                it.skip(dmgSrc)
                return -3 // don't dupe handle killing here
            }
            if (checkPass(3)) return 3

            // lv.4: coming soon
            return 999
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