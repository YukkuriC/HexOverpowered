package io.yukkuric.hexop.mixin.accessor;

import at.petrak.hexcasting.api.casting.mishaps.Mishap;
import at.petrak.hexcasting.api.pigment.FrozenPigment;
import net.minecraft.world.item.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Mishap.class)
public interface AccessorMishap {
    @Invoker
    FrozenPigment callDyeColor(DyeColor color);
}
