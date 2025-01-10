package io.yukkuric.hexop.fabric;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

import static io.yukkuric.hexop.HexOPConfig.*;

@Config(name = "HexOverpowered")
public class HexOPConfigFabric implements ConfigData {
    @ConfigEntry.Gui.CollapsibleObject
    private final Common common = new Common();

    public static void setup() {
        AutoConfig.register(HexOPConfigFabric.class, JanksonConfigSerializer::new);
        var instance = AutoConfig.getConfigHolder(HexOPConfigFabric.class).getConfig();
        bindConfigImp(instance.common);
    }

    public static class Common implements API, ConfigData {
        @Comment(DESCRIP_MOTE_GLANCE)
        private boolean EnablesMoteChestGUI = true;
        @Comment(DESCRIP_NO_YEET)
        private boolean EnablesMishapNoYeet = true;


        @Override
        public boolean EnablesMoteChestGUI() {
            return EnablesMoteChestGUI;
        }

        @Override
        public boolean EnablesMishapNoYeet() {
            return EnablesMishapNoYeet;
        }
    }
}