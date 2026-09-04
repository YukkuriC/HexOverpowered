package io.yukkuric.hexop.fabric

import io.yukkuric.hexop.HexOPXPlat
import io.yukkuric.hexop.helpers.attack.EntityHealthAccessors
import io.yukkuric.hexop.helpers.attack.IEntityHealthAccessor
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.boss.EnderDragonPart

class HexOPXPlatFabric : HexOPXPlat() {

    companion object {
        object ENDER_CRAGON : IEntityHealthAccessor<EnderDragonPart> {
            override fun validate(target: Entity) = target is EnderDragonPart
            override fun setHealth(target: EnderDragonPart, newHealth: Float, caster: LivingEntity?) =
                EntityHealthAccessors.setHealth(target.parentMob, newHealth, caster)

            override fun getHealth(target: EnderDragonPart) = EntityHealthAccessors.getHealth(target.parentMob)
        }
        
        @JvmStatic
        fun init() {
            EntityHealthAccessors.AddSub(ENDER_CRAGON)
        }
    }
}