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
        @Comment("<Display> " + desc_RevealsHexInsideCastingItems)
        private boolean RevealsHexInsideCastingItems = true;
        @Comment("<Mote> " + desc_EnablesMoteItemHandler)
        private boolean EnablesMoteItemHandler = true;
        @Comment("<Mote> " + desc_EnablesMoteChestGUI)
        private boolean EnablesMoteChestGUI = true;
        @Comment("<Misc> " + desc_TrueNameCrossDimension)
        private boolean TrueNameCrossDimension = true;
        @Comment("<Mishap> " + desc_EnablesMishapNoYeet)
        private boolean EnablesMishapNoYeet = true;
        @Comment("<Mishap> " + desc_EnablesTeleportVehicles)
        private boolean EnablesTeleportVehicles = true;
        @Comment("<Pattern> " + desc_EnablesChargeMediaAction)
        private boolean EnablesChargeMediaAction = true;
        @Comment("<Pattern> " + desc_EnablesMindEnvActions)
        private boolean EnablesMindEnvActions = true;
        @Comment("<Pattern> " + desc_TrulyHurtLevel)
        private int TrulyHurtLevel = 2;
        @Comment("<Pattern.FactorCut> " + desc_EnablesFactorCutSpell)
        private boolean EnablesFactorCutSpell = true;
        @Comment("<Pattern.FactorCut> " + desc_FactorCutPrimeCost)
        private int FactorCutPrimeCost = 10000;
        @Comment("<Pattern.FactorCut> " + desc_FactorCutNonPrimeCostScale)
        private int FactorCutNonPrimeCostScale = 50000;
        @Comment("<Pattern.FactorCut> " + desc_FactorCutFallbackCost)
        private int FactorCutFallbackCost = 50001;
        @Comment("<Pattern.FactorCut> " + desc_FactorCutRandomMode)
        private boolean FactorCutRandomMode = false;
        @Comment("<Pattern.FactorCut> " + desc_FactorCutMinimumFactor)
        private int FactorCutMinimumFactor = 2;
        @Comment("<Pattern.FactorCut> " + desc_FactorCutKillingBlowLine)
        private int FactorCutKillingBlowLine = 1;
        @Comment("<Personal Media> " + desc_EnablesPersonalMediaPool)
        private boolean EnablesPersonalMediaPool = true;
        @Comment("<Personal Media> " + desc_PersonalMediaMax)
        private int PersonalMediaMax = HexOverpowered.DEFAULTS.MANA_MAX;
        @Comment("<Personal Media> " + desc_PersonalMediaRegenStep)
        private int PersonalMediaRegenStep = HexOverpowered.DEFAULTS.MANA_REGEN;
        @Comment("<Personal Media> " + desc_PersonalMediaRegenInterval)
        private int PersonalMediaRegenInterval = HexOverpowered.DEFAULTS.MANA_REGEN_INTERVAL;
        @Comment("<Personal Media> " + desc_FakePlayerDontRegenMedia)
        private boolean FakePlayerDontRegenMedia = true;
        @Comment("<Personal Media> " + desc_PersonalMediaAfterEnlightened)
        private boolean PersonalMediaAfterEnlightened = true;
        @Comment("<Personal Media> " + desc_FiresPersonalMediaEvents)
        private boolean FiresPersonalMediaEvents = true;
        @Comment("<Amethyst Circle> " + desc_EnablesAmethystCircle)
        private boolean EnablesAmethystCircle = true;
        @Comment("<Amethyst Circle> " + desc_AmethystCircleSingleChargeCost)
        private int AmethystCircleSingleChargeCost = 100000;
        @Comment("<Amethyst Circle> " + desc_AmethystCircleFullPowerLevel)
        private int AmethystCircleFullPowerLevel = 15;
        @Comment("<Property> " + desc_ExecutablePropertyIota)
        private boolean ExecutablePropertyIota = true;

        public boolean RevealsHexInsideCastingItems() {
            return RevealsHexInsideCastingItems;
        }
        public boolean EnablesMoteItemHandler() {
            return EnablesMoteItemHandler;
        }
        public boolean EnablesMoteChestGUI() {
            return EnablesMoteChestGUI;
        }
        public boolean TrueNameCrossDimension() {
            return TrueNameCrossDimension;
        }
        public boolean EnablesMishapNoYeet() {
            return EnablesMishapNoYeet;
        }
        public boolean EnablesTeleportVehicles() {
            return EnablesTeleportVehicles;
        }
        public boolean EnablesChargeMediaAction() {
            return EnablesChargeMediaAction;
        }
        public boolean EnablesMindEnvActions() {
            return EnablesMindEnvActions;
        }
        public int TrulyHurtLevel() {
            return TrulyHurtLevel;
        }
        public boolean EnablesFactorCutSpell() {
            return EnablesFactorCutSpell;
        }
        public int FactorCutPrimeCost() {
            return FactorCutPrimeCost;
        }
        public int FactorCutNonPrimeCostScale() {
            return FactorCutNonPrimeCostScale;
        }
        public int FactorCutFallbackCost() {
            return FactorCutFallbackCost;
        }
        public boolean FactorCutRandomMode() {
            return FactorCutRandomMode;
        }
        public int FactorCutMinimumFactor() {
            return FactorCutMinimumFactor;
        }
        public int FactorCutKillingBlowLine() {
            return FactorCutKillingBlowLine;
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
        public boolean PersonalMediaAfterEnlightened() {
            return PersonalMediaAfterEnlightened;
        }
        public boolean FiresPersonalMediaEvents() {
            return FiresPersonalMediaEvents;
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
        public boolean ExecutablePropertyIota() {
            return ExecutablePropertyIota;
        }
    }
}
