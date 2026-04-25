package io.yukkuric.hexop.mixin;

import io.yukkuric.hexop.mixin_interface.MobSkipper;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Mob.class)
public abstract class MixinMob extends LivingEntity implements MobSkipper {
    @Shadow
    public abstract void removeFreeWill();
    protected MixinMob(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }
    @Override
    public void skip(DamageSource dmgSrc) {
        if (isRemoved()) return;
        super.setHealth(0);
        super.die(dmgSrc);
        removeFreeWill();
        super.remove(RemovalReason.KILLED);
    }
}
