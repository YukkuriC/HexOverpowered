package io.yukkuric.hexop.mixin;

import at.petrak.hexcasting.api.casting.iota.*;
import io.yukkuric.hexop.HexOPConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.ref.WeakReference;
import java.util.UUID;
import java.util.function.Supplier;

@Mixin(EntityIota.class)
public abstract class SuperPlayerIota extends Iota {
    @Shadow
    @Final
    private UUID entityId;
    @Shadow
    private @Nullable WeakReference<Entity> cachedEntity;
    protected SuperPlayerIota(@NotNull Supplier<IotaType<? extends Iota>> type) {
        super(type);
    }
    @Inject(method = "getOrFindEntity", at = @At("RETURN"), cancellable = true)
    void hook(ServerLevel level, CallbackInfoReturnable<Entity> cir) {
        if (!HexOPConfig.TrueNameCrossDimension() || cir.getReturnValue() != null) return;
        var players = level.getServer().getPlayerList();
        var uuid = entityId;
        var tryPlayer = players.getPlayer(uuid);
        if (tryPlayer != null) {
            cachedEntity = new WeakReference<>(tryPlayer);
            cir.setReturnValue(tryPlayer);
        }
    }
}
