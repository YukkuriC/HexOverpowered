package io.yukkuric.hexop.forge

import io.yukkuric.hexop.HexOPXPlat
import io.yukkuric.hexop.helpers.attack.EntityHealthAccessors
import io.yukkuric.hexop.helpers.attack.IEntityHealthAccessor
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraftforge.entity.PartEntity
import net.minecraftforge.fml.ModList

class HexOPXPlatForge : HexOPXPlat() {
    override fun isModLoaded(id: String?): Boolean {
        return ModList.get().isLoaded(id)
    }

    companion object {
        object PART_ENTITY : IEntityHealthAccessor<PartEntity<*>> {
            override fun validate(target: Entity) = target is PartEntity<*>
            override fun setHealth(target: PartEntity<*>, newHealth: Float, caster: LivingEntity?) =
                EntityHealthAccessors.setHealth(target.parent, newHealth, caster)

            override fun getHealth(target: PartEntity<*>) = EntityHealthAccessors.getHealth(target.parent)
        }

        @JvmStatic
        fun init() {
            EntityHealthAccessors.AddSub(PART_ENTITY)
        }
    }
}