package io.yukkuric.hexop.helpers.attack

import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity

interface IEntityHealthAccessor<T : Entity> {
    fun validate(target: Entity): Boolean
    fun getHealth(target: T): Float
    fun setHealth(target: T, newHealth: Float, caster: LivingEntity?)
    fun getHealthT(target: Entity) = getHealth(target as T)
    fun setHealthT(target: Entity, newHealth: Float, caster: LivingEntity?) =
        setHealth(target as T, newHealth, caster)
}