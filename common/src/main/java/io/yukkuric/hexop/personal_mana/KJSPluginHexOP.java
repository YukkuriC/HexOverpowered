package io.yukkuric.hexop.personal_mana;

import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingRegistry;
import io.yukkuric.hexop.HexOPAttributes;

public class KJSPluginHexOP implements KubeJSPlugin {
    public void registerBindings(BindingRegistry event) {
        if (event.type().isClient()) return;
        event.add("PersonalMediaEvents", PersonalManaEvents.class);
        event.add("PersonalManaEvents", PersonalManaEvents.class);
        event.add("HexOPAttributes", HexOPAttributes.class);
    }
}
