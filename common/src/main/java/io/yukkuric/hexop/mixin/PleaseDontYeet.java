package io.yukkuric.hexop.mixin;

import at.petrak.hexcasting.api.spell.casting.CastingContext;
import at.petrak.hexcasting.api.spell.mishaps.Mishap;
import io.yukkuric.hexop.HexOPConfig;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mishap.class)
public class PleaseDontYeet {
    @Inject(method = "yeetHeldItemsTowards", at = @At("HEAD"), cancellable = true)
    public void noYeet(CastingContext ctx, Vec3 targetPos, CallbackInfo ci) {
        if (HexOPConfig.EnablesMishapNoYeet()) ci.cancel();
    }

    @Inject(method = "yeetHeldItem", at = @At("HEAD"), cancellable = true)
    public void noYeet(CastingContext ctx, InteractionHand hand, CallbackInfo ci) {
        if (HexOPConfig.EnablesMishapNoYeet()) ci.cancel();
    }
}
