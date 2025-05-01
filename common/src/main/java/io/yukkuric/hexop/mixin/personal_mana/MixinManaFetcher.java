package io.yukkuric.hexop.mixin.personal_mana;

import at.petrak.hexcasting.api.addldata.ADMediaHolder;
import at.petrak.hexcasting.api.misc.DiscoveryHandlers;
import at.petrak.hexcasting.api.spell.casting.CastingHarness;
import io.yukkuric.hexop.HexOPConfig;
import io.yukkuric.hexop.personal_mana.PersonalManaHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(DiscoveryHandlers.class)
public class MixinManaFetcher {
    @Inject(method = "collectMediaHolders", at = @At("RETURN"), remap = false)
    private static void insertMine(CastingHarness harness, CallbackInfoReturnable<List<ADMediaHolder>> cir) {
        if (!HexOPConfig.EnablesPersonalMediaPool()) return;
        cir.getReturnValue().add(0, PersonalManaHolder.get(harness.getCtx().getCaster()));
    }
}
