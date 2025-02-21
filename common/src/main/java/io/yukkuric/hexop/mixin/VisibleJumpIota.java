package io.yukkuric.hexop.mixin;

import at.petrak.hexcasting.api.casting.eval.vm.ContinuationFrame;
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation;
import at.petrak.hexcasting.api.utils.HexUtils;
import io.yukkuric.hexop.HexOverpowered;
import io.yukkuric.hexop.helpers.CustomContinuationDisplay;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = {"at.petrak.hexcasting.api.casting.iota.ContinuationIota$1"})
public class VisibleJumpIota {
    @Inject(method = "display", at = @At("RETURN"), cancellable = true)
    void customJump(Tag tag, CallbackInfoReturnable<Component> cir) {
        var server = HexOverpowered.GRAB_SERVER;
        if (server == null) return;
        var world = server.overworld();
        var frameList = HexUtils.downcast(tag, CompoundTag.TYPE).getList(SpellContinuation.TAG_FRAME, Tag.TAG_COMPOUND);
        var display = Component.literal("");
        var isFirst = true;
        for (var f : frameList) {
            if (!(f instanceof CompoundTag ff)) continue;
            var frame = ContinuationFrame.fromNBT(ff, world);
            if (!isFirst) display = display.append("\n");
            display = display.append(CustomContinuationDisplay.getDisplay(frame));
            isFirst = false;
        }
        var ret = cir.getReturnValue();
        var finalDisplay = display;
        cir.setReturnValue(ret.copy().withStyle(s -> s.withHoverEvent(HoverEvent.Action.SHOW_TEXT.deserializeFromLegacy(finalDisplay))));
    }
}
