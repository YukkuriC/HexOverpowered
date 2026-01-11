package io.yukkuric.hexop.mixin.accessor;

import at.petrak.hexcasting.api.misc.FrozenColorizer;
import at.petrak.hexcasting.api.spell.mishaps.Mishap;
import net.minecraft.world.item.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Mishap.class)
public interface AccessorMishap {
    @Invoker
    FrozenColorizer callDyeColor(DyeColor color);
}
