package io.yukkuric.hexop.personal_mana;

import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PersonalManaEvents {
    private static final List<Consumer<EventInsert>> onInsert = new ArrayList<>();
    private static final List<Consumer<EventExtract>> onExtract = new ArrayList<>();

    public static void resetAll() {
        onInsert.clear();
        onExtract.clear();
    }
    static void OnInsert(EventInsert event) {
        for (var callback : onInsert) callback.accept(event);
    }
    static void OnExtract(EventExtract event) {
        for (var callback : onExtract) callback.accept(event);
    }
    public static void AddOnInsert(Consumer<EventInsert> callback) {
        onInsert.add(callback);
    }
    public static void AddOnExtract(Consumer<EventExtract> callback) {
        onExtract.add(callback);
    }

    public record EventInsert(Player player, int target, int inserted) {
    }
    public record EventExtract(Player player, int target, int extracted) {
    }
}
