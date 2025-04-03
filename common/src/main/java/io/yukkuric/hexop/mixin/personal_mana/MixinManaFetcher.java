package io.yukkuric.hexop.mixin.personal_mana;

import at.petrak.hexcasting.api.addldata.ADMediaHolder;
import at.petrak.hexcasting.api.utils.MediaHelper;
import io.yukkuric.hexop.HexOPConfig;
import io.yukkuric.hexop.personal_mana.PersonalManaHolder;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(MediaHelper.class)
public class MixinManaFetcher {
    @Inject(method = "scanPlayerForMediaStuff", at = @At("RETURN"), remap = false)
    private static void insertMine(ServerPlayer player, CallbackInfoReturnable<List<ADMediaHolder>> cir) {
        if (HexOPConfig.DisablesPersonalMediaPool()) return;
        cir.getReturnValue().add(0, PersonalManaHolder.get(player));
    }
}
