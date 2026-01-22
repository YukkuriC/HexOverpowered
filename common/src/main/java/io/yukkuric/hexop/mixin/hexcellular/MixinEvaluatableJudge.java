package io.yukkuric.hexop.mixin.hexcellular;

import at.petrak.hexcasting.api.spell.OperatorUtils;
import at.petrak.hexcasting.api.spell.SpellList;
import at.petrak.hexcasting.api.spell.iota.Iota;
import at.petrak.hexcasting.api.spell.math.HexPattern;
import com.mojang.datafixers.util.Either;
import io.yukkuric.hexop.HexOPConfig;
import miyucomics.hexcellular.PropertyIota;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(OperatorUtils.class)
public class MixinEvaluatableJudge {
    @Inject(method = "evaluatable", at = @At("HEAD"), cancellable = true, remap = false)
    private static void PropertyEvaluatable(Iota datum, int reverseIdx, CallbackInfoReturnable<Either<HexPattern, SpellList>> cir) {
        if (!HexOPConfig.ExecutablePropertyIota()) return;
        if (!(datum instanceof PropertyIota prop)) return;
        cir.setReturnValue(Either.right(new SpellList.LList(List.of(prop))));
    }
}
