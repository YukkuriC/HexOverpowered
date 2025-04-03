package io.yukkuric.hexop.mixin.personal_mana;

import com.mojang.authlib.GameProfile;
import io.yukkuric.hexop.HexOPConfig;
import io.yukkuric.hexop.personal_mana.PersonalManaHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class MixinManaRegen extends Player {
    public MixinManaRegen(Level level, BlockPos blockPos, float f, GameProfile gameProfile) {
        super(level, blockPos, f, gameProfile);
    }
    private PersonalManaHolder holder;

    @Inject(method = "tick", at = @At("HEAD"))
    void regenTick(CallbackInfo ci) {
        if (HexOPConfig.DisablesPersonalMediaPool() || tickCount % HexOPConfig.PersonalMediaRegenInterval() > 0) return;
        if (holder == null) holder = PersonalManaHolder.get(this);
        holder.insertMedia(HexOPConfig.PersonalMediaRegenStep(), false);
    }
}
