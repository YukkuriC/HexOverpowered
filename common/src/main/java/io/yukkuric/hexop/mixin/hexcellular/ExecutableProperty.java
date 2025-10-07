package io.yukkuric.hexop.mixin.hexcellular;

import at.petrak.hexcasting.api.casting.SpellList;
import at.petrak.hexcasting.api.casting.eval.CastResult;
import at.petrak.hexcasting.api.casting.eval.ResolvedPatternType;
import at.petrak.hexcasting.api.casting.eval.vm.*;
import at.petrak.hexcasting.api.casting.iota.*;
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds;
import io.yukkuric.hexop.HexOPConfig;
import miyucomics.hexcellular.PropertyIota;
import miyucomics.hexcellular.StateStorage;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.*;

import java.util.List;

@Mixin(PropertyIota.class)
public abstract class ExecutableProperty extends Iota {
    @Shadow(remap = false)
    @Final
    private String name;

    protected ExecutableProperty(@NotNull IotaType<?> type, @NotNull Object payload) {
        super(type, payload);
    }

    public boolean executable() {
        return HexOPConfig.ExecutablePropertyIota();
    }

    public @NotNull CastResult execute(CastingVM vm, ServerLevel world, SpellContinuation continuation) {
        if (!HexOPConfig.ExecutablePropertyIota()) return super.execute(vm, world, continuation);

        // read content
        var data = StateStorage.Companion.getProperty(world, name);
        var spell = data instanceof ListIota list ? list.getList() : new SpellList.LList(List.of(data));

        // add to cont.
        var newCont = continuation.pushFrame(FrameFinishEval.INSTANCE).pushFrame(new FrameEvaluate(spell, true));

        // return
        return new CastResult(this, newCont, vm.getImage().withUsedOp(), List.of(), ResolvedPatternType.EVALUATED, HexEvalSounds.NOTHING);
    }
}
