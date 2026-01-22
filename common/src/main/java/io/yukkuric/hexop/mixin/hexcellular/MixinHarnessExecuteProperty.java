package io.yukkuric.hexop.mixin.hexcellular;

import at.petrak.hexcasting.api.spell.SpellList;
import at.petrak.hexcasting.api.spell.casting.CastingHarness;
import at.petrak.hexcasting.api.spell.casting.ResolvedPatternType;
import at.petrak.hexcasting.api.spell.casting.eval.*;
import at.petrak.hexcasting.api.spell.casting.sideeffects.OperatorSideEffect;
import at.petrak.hexcasting.api.spell.iota.Iota;
import at.petrak.hexcasting.api.spell.iota.ListIota;
import at.petrak.hexcasting.api.spell.mishaps.MishapUnescapedValue;
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds;
import io.yukkuric.hexop.HexOPConfig;
import miyucomics.hexcellular.PropertyIota;
import miyucomics.hexcellular.StateStorage;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;


@Mixin(CastingHarness.class)
public class MixinHarnessExecuteProperty {
    @Inject(method = "getUpdate", at = @At("RETURN"), remap = false, cancellable = true)
    void hookExecutePattern(Iota iota, ServerLevel world, SpellContinuation continuation, CallbackInfoReturnable<CastingHarness.CastResult> cir) {
        if (!HexOPConfig.ExecutablePropertyIota()) return;
        if (!(iota instanceof PropertyIota prop)) return;
        var oldResult = cir.getReturnValue();
        // not working if no mishap
        var oldEffects = oldResult.getSideEffects();
        if (oldEffects.size() != 1 || !(oldEffects.get(0) instanceof OperatorSideEffect.DoMishap mishaper) || !(mishaper.getMishap() instanceof MishapUnescapedValue))
            return;

        // read content
        var data = StateStorage.Companion.getProperty(world, prop.getName());
        var spell = data instanceof ListIota list ? list.getList() : new SpellList.LList(List.of(data));

        // add to cont.
        var newCont = continuation.pushFrame(FrameFinishEval.INSTANCE).pushFrame(new FrameEvaluate(spell, true));

        // return
        cir.setReturnValue(new CastingHarness.CastResult(
                newCont,
                oldResult.getNewData(),
                ResolvedPatternType.EVALUATED,
                List.of(),
                HexEvalSounds.NOTHING
        ));
    }
}
