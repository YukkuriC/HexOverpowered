package io.yukkuric.hexop.fabric.mixin;

import io.yukkuric.hexop.HexOPAttributes;
import io.yukkuric.hexop.fabric.HexOverpoweredFabric;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class MixinHexOPFabricPlayer {
    @Inject(at = @At("RETURN"), method = "createAttributes")
    private static void hex$addAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        HexOverpoweredFabric.initSelfHook();
        var out = cir.getReturnValue();
        for (var attr : HexOPAttributes.getAll())
            out.add(attr);
    }
}
