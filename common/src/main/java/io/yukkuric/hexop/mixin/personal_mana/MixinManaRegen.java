package io.yukkuric.hexop.mixin.personal_mana;

import com.mojang.authlib.GameProfile;
import io.yukkuric.hexop.HexOPConfig;
import io.yukkuric.hexop.HexOverpowered;
import io.yukkuric.hexop.personal_mana.PersonalManaHolder;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class MixinManaRegen extends Player {
    @Shadow
    @Final
    public MinecraftServer server;
    @Shadow
    public abstract PlayerAdvancements getAdvancements();
    private Advancement enlightenCheck;

    public MixinManaRegen(Level level, BlockPos blockPos, float f, GameProfile gameProfile) {
        super(level, blockPos, f, gameProfile);
    }
    private PersonalManaHolder holder;

    @Inject(method = "tick", at = @At("HEAD"))
    void regenTick(CallbackInfo ci) {
        var checkStep = Math.max(1, HexOPConfig.PersonalMediaRegenInterval());
        if (HexOPConfig.FakePlayerDontRegenMedia() && HexOverpowered.IsFakePlayer(this)) return;
        if (!HexOPConfig.EnablesPersonalMediaPool() || tickCount % checkStep > 0) return;

        // check enlighten
        if (enlightenCheck == null)
            enlightenCheck = server.getAdvancements().getAdvancement(ResourceLocation.tryParse("hexcasting:enlightenment"));
        if (!getAdvancements().getOrStartProgress(enlightenCheck).isDone()) return;

        if (holder == null) holder = PersonalManaHolder.get(this);
        holder.insertMedia(holder.getMediaRegenStep(), false);
    }
}
