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
        @Comment(desc_RevealsHexInsideCastingItems)
        private boolean RevealsHexInsideCastingItems = true;
        @Comment(desc_EnablesMoteChestGUI)
        private boolean EnablesMoteChestGUI = true;
        @Comment(desc_EnablesMishapNoYeet)
        private boolean EnablesMishapNoYeet = true;
        @Comment(desc_EnablesTeleportVehicles)
        private boolean EnablesTeleportVehicles = true;
        @Comment(desc_EnablesPersonalMediaPool)
        private boolean EnablesPersonalMediaPool = true;
        @Comment(desc_PersonalMediaMax)
        private int PersonalMediaMax = HexOverpowered.DEFAULTS.MANA_MAX;
        @Comment(desc_PersonalMediaRegenStep)
        private int PersonalMediaRegenStep = HexOverpowered.DEFAULTS.MANA_REGEN;
        @Comment(desc_PersonalMediaRegenInterval)
        private int PersonalMediaRegenInterval = HexOverpowered.DEFAULTS.MANA_REGEN_INTERVAL;
        @Comment(desc_FakePlayerDontRegenMedia)
        private boolean FakePlayerDontRegenMedia = true;
        @Comment(desc_EnablesAmethystCircle)
        private boolean EnablesAmethystCircle = true;
        @Comment(desc_AmethystCircleSingleChargeCost)
        private int AmethystCircleSingleChargeCost = 100000;
        @Comment(desc_AmethystCircleFullPowerLevel)
        private int AmethystCircleFullPowerLevel = 15;

        public boolean RevealsHexInsideCastingItems() {
            return RevealsHexInsideCastingItems;
        }
        public boolean EnablesMoteChestGUI() {
            return EnablesMoteChestGUI;
        }
        public boolean EnablesMishapNoYeet() {
            return EnablesMishapNoYeet;
        }
        public boolean EnablesTeleportVehicles() {
            return EnablesTeleportVehicles;
        }
        public boolean EnablesPersonalMediaPool() {
            return EnablesPersonalMediaPool;
        }
        public int PersonalMediaMax() {
            return PersonalMediaMax;
        }
        public int PersonalMediaRegenStep() {
            return PersonalMediaRegenStep;
        }
        public int PersonalMediaRegenInterval() {
            return PersonalMediaRegenInterval;
        }
        public boolean FakePlayerDontRegenMedia() {
            return FakePlayerDontRegenMedia;
        }
        public boolean EnablesAmethystCircle() {
            return EnablesAmethystCircle;
        }
        public int AmethystCircleSingleChargeCost() {
            return AmethystCircleSingleChargeCost;
        }
        public int AmethystCircleFullPowerLevel() {
            return AmethystCircleFullPowerLevel;
        }
    }
}
