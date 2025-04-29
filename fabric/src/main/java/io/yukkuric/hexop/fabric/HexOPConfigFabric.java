package io.yukkuric.hexop.fabric;

import io.yukkuric.hexop.HexOverpowered;
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
        @Comment(DESCRIP_TP_VEHICLES)
        private boolean EnablesTeleportVehicles = true;
        @Comment(DESCRIP_MANA_ENABLE)
        private boolean EnablesPersonalMediaPool = true;
        @Comment(DESCRIP_MANA_MAX)
        private int PersonalMediaMax = HexOverpowered.DEFAULTS.MANA_MAX;
        @Comment(DESCRIP_MANA_REGEN_STEP)
        private int PersonalMediaRegenStep = HexOverpowered.DEFAULTS.MANA_REGEN;
        @Comment(DESCRIP_MANA_REGEN_INTERVAL)
        private int PersonalMediaRegenInterval = HexOverpowered.DEFAULTS.MANA_REGEN_INTERVAL;
        @Comment(DESCRIP_FAKE_PLAYERS_NOT_REGEN_MANA)
        private boolean FakePlayerDontRegenMedia = true;


        @Override
        public boolean EnablesMoteChestGUI() {
            return EnablesMoteChestGUI;
        }

        @Override
        public boolean EnablesMishapNoYeet() {
            return EnablesMishapNoYeet;
        }

        @Override
        public boolean EnablesTeleportVehicles() {
            return EnablesTeleportVehicles;
        }
        @Override
        public boolean EnablesPersonalMediaPool() {
            return EnablesPersonalMediaPool;
        }
        @Override
        public int PersonalMediaMax() {
            return PersonalMediaMax;
        }
        @Override
        public int PersonalMediaRegenStep() {
            return PersonalMediaRegenStep;
        }
        @Override
        public int PersonalMediaRegenInterval() {
            return PersonalMediaRegenInterval;
        }
        @Override
        public boolean FakePlayerDontRegenMedia() {
            return FakePlayerDontRegenMedia;
        }
    }
}