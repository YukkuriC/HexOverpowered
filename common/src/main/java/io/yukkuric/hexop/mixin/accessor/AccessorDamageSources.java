package io.yukkuric.hexop.mixin.accessor;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DamageSources.class)
public interface AccessorDamageSources {
    @Invoker
    DamageSource callSource(ResourceKey<DamageType> resourceKey, @Nullable Entity entity);
}
