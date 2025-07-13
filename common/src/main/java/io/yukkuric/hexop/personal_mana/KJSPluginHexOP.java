package io.yukkuric.hexop.personal_mana;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import io.yukkuric.hexop.HexOPAttributes;

public class KJSPluginHexOP extends KubeJSPlugin {
    public void registerBindings(BindingsEvent event) {
        if (event.manager.scriptType == ScriptType.CLIENT) return;
        event.add("PersonalMediaEvents", PersonalManaEvents.class);
        event.add("PersonalManaEvents", PersonalManaEvents.class);
        event.add("HexOPAttributes", HexOPAttributes.class);
    }
}
