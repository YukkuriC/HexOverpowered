package io.yukkuric.hexop.mixin.accessor;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CastingEnvironment.class)
public interface AccessorCastingEnvironment {
    @Accessor
    @Mutable
    void setWorld(ServerLevel world);
}
