package io.yukkuric.hexop.mixin;

import at.petrak.hexcasting.api.casting.eval.env.PlayerBasedMishapEnv;
import io.yukkuric.hexop.HexOPConfig;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerBasedMishapEnv.class)
public class PleaseDontYeet {
    @Inject(method = "yeetHeldItemsTowards", at = @At("HEAD"), cancellable = true)
    public void noYeet(Vec3 targetPos, CallbackInfo ci) {
        if (HexOPConfig.EnablesMishapNoYeet()) ci.cancel();
    }
}
