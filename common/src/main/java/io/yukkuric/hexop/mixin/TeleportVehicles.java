package io.yukkuric.hexop.mixin;

import at.petrak.hexcasting.common.casting.actions.spells.great.OpTeleport;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.yukkuric.hexop.HexOPConfig;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(OpTeleport.class)
public class TeleportVehicles {
    @WrapOperation(method = "execute", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;canChangeDimensions()Z"), require = 0)
    boolean doTeleport(Entity instance, Operation<Boolean> original) {
        if (HexOPConfig.EnablesTeleportVehicles()) return true;
        return original.call(instance);
    }
}
