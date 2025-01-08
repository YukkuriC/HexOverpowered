package io.yukkuric.hexop.mixin.hexal;

import at.petrak.hexcasting.api.casting.eval.env.PlayerBasedMishapEnv;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(PlayerBasedMishapEnv.class)
public class PleaseDontYeet {
    @Overwrite
    public void yeetHeldItemsTowards(Vec3 pos) { // nope
    }
}
