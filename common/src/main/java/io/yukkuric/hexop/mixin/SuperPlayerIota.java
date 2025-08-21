package io.yukkuric.hexop.mixin;

import at.petrak.hexcasting.api.spell.iota.EntityIota;
import at.petrak.hexcasting.api.spell.iota.IotaType;
import com.llamalad7.mixinextras.sugar.Local;
import io.yukkuric.hexop.HexOPConfig;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = {"at.petrak.hexcasting.api.spell.iota.EntityIota$1"})
public abstract class SuperPlayerIota extends IotaType<EntityIota> {
    @Inject(method = "deserialize(Lnet/minecraft/nbt/Tag;Lnet/minecraft/server/level/ServerLevel;)Lat/petrak/hexcasting/api/spell/iota/EntityIota;", at = @At("RETURN"), cancellable = true)
    void hook(Tag tag, ServerLevel world, CallbackInfoReturnable<EntityIota> cir, @Local(ordinal = 1) Tag uuidTag) {
        if (uuidTag == null || !HexOPConfig.TrueNameCrossDimension() || cir.getReturnValue() != null) return;
        var players = world.getServer().getPlayerList();
        var uuid = NbtUtils.loadUUID(uuidTag);
        var tryPlayer = players.getPlayer(uuid);
        if (tryPlayer != null) cir.setReturnValue(new EntityIota(tryPlayer));
    }
}
