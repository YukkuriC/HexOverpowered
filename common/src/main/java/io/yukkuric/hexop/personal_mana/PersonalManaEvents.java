package io.yukkuric.hexop.personal_mana;

import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PersonalManaEvents {
    private static final List<Consumer<EventBody>> onInsert = new ArrayList<>();
    private static final List<Consumer<EventBody>> onExtract = new ArrayList<>();

    public static void resetAll() {
        onInsert.clear();
        onExtract.clear();
    }
    static void OnInsert(EventBody event) {
        for (var callback : onInsert) callback.accept(event);
    }
    static void OnExtract(EventBody event) {
        for (var callback : onExtract) callback.accept(event);
    }
    public static void AddOnInsert(Consumer<EventBody> callback) {
        onInsert.add(callback);
    }
    public static void AddOnExtract(Consumer<EventBody> callback) {
        onExtract.add(callback);
    }

    public static class EventBody {
        public final PersonalManaHolder holder;
        public final Player player;
        public final long target, actual;

        public EventBody(PersonalManaHolder holder, long target, long actual) {
            this.holder = holder;
            this.player = holder.getPlayer();
            this.target = target;
            this.actual = actual;
        }

        public long getDropped() {
            return this.target - this.actual;
        }

        @Override
        public String toString() {
            return "[p=%s,t=%s,a=%s]".formatted(player.getDisplayName().getString(), target, actual);
        }
    }
}
