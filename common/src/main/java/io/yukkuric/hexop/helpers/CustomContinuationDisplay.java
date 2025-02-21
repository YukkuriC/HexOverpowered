package io.yukkuric.hexop.helpers;

import at.petrak.hexcasting.api.casting.SpellList;
import at.petrak.hexcasting.api.casting.eval.vm.*;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.ListIota;
import io.yukkuric.hexflow.vm.FrameRecoverStack;
import io.yukkuric.hexflow.vm.FrameReduce;
import io.yukkuric.hexop.HexOverpowered;
import net.minecraft.network.chat.Component;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;

public class CustomContinuationDisplay {
    private static final Map<Class<?>, Function<ContinuationFrame, Component>> MAP = new HashMap<>();

    public static <T extends ContinuationFrame> void addDisplay(Class<T> cls, Function<T, Component> func) {
        MAP.put(cls, (Function<ContinuationFrame, Component>) func);
    }

    public static <T extends ContinuationFrame> void addDisplay(Class<T> cls, String literal) {
        MAP.put(cls, (x) -> Component.literal(literal));
    }

    public static Component getDisplay(ContinuationFrame frame) {
        var cls = frame.getClass();
        if (!MAP.containsKey(cls)) return Component.literal(cls.getSimpleName());
        return MAP.get(cls).apply(frame);
    }

    static final ListIota DUMMY_DISPLAY = new ListIota(List.of());
    static final Field LIST_INNER;

    public static Component showList(SpellList target) {
        try {
            LIST_INNER.set(DUMMY_DISPLAY, target);
        } catch (Throwable e) {
            return Component.literal(e.toString());
        }
        return DUMMY_DISPLAY.display();
    }

    static {
        Field tmp = null;
        try {
            tmp = Iota.class.getDeclaredField("payload");
            tmp.setAccessible(true);
        } catch (Throwable e) {
        } finally {
            LIST_INNER = tmp;
        }

        addDisplay(FrameEvaluate.class, (frame) -> Component.translatable("hexop.display.frame.eval", showList(frame.getList())));
        addDisplay(FrameForEach.class, (frame) -> Component.translatable("hexop.display.frame.foreach", frame.getBaseStack(), frame.getCode(), frame.getData(), frame.getAcc()));
        addDisplay(FrameFinishEval.class, "Finish");

        if (HexOverpowered.IsModLoaded("hexflow")) {
            addDisplay(FrameReduce.class, (frame) -> Component.translatable("hexop.display.frame.reduce", showList(frame.getCode()), showList(frame.getData())));
            addDisplay(FrameRecoverStack.class, (frame) -> Component.translatable("hexop.display.frame.recover_stack", showList(new SpellList.LList(frame.getMyStack()))));
        }
    }
}
