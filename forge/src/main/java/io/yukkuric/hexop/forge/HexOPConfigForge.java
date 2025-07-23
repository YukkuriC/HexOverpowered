package io.yukkuric.hexop.forge;

import io.yukkuric.hexop.HexOverpowered;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

import static io.yukkuric.hexop.HexOPConfig.*;

public class HexOPConfigForge implements API {
    public static HexOPConfigForge INSTANCE;
    private static final String desc_MekasuitConversionRatio = "<MekaSuit>\nHow many media points each FE point equals";
    public static double MekasuitConversionRatio() {
        return INSTANCE.cfg_MekasuitConversionRatio.get();
    }

    public boolean RevealsHexInsideCastingItems() {
        return cfg_RevealsHexInsideCastingItems.get();
    }
    public boolean EnablesMoteChestGUI() {
        return cfg_EnablesMoteChestGUI.get();
    }
    public boolean EnablesMishapNoYeet() {
        return cfg_EnablesMishapNoYeet.get();
    }
    public boolean EnablesTeleportVehicles() {
        return cfg_EnablesTeleportVehicles.get();
    }
    public boolean EnablesChargeMediaAction() {
        return cfg_EnablesChargeMediaAction.get();
    }
    public boolean EnablesMindEnvActions() {
        return cfg_EnablesMindEnvActions.get();
    }
    public boolean EnablesPersonalMediaPool() {
        return cfg_EnablesPersonalMediaPool.get();
    }
    public int PersonalMediaMax() {
        return cfg_PersonalMediaMax.get();
    }
    public int PersonalMediaRegenStep() {
        return cfg_PersonalMediaRegenStep.get();
    }
    public int PersonalMediaRegenInterval() {
        return cfg_PersonalMediaRegenInterval.get();
    }
    public boolean FakePlayerDontRegenMedia() {
        return cfg_FakePlayerDontRegenMedia.get();
    }
    public boolean PersonalMediaAfterEnlightened() {
        return cfg_PersonalMediaAfterEnlightened.get();
    }
    public boolean FiresPersonalMediaEvents() {
        return cfg_FiresPersonalMediaEvents.get();
    }

    public ForgeConfigSpec.BooleanValue
            cfg_RevealsHexInsideCastingItems,
            cfg_EnablesMoteChestGUI,
            cfg_EnablesMishapNoYeet,
            cfg_EnablesTeleportVehicles,
            cfg_EnablesChargeMediaAction,
            cfg_EnablesMindEnvActions,
            cfg_EnablesPersonalMediaPool,
            cfg_FakePlayerDontRegenMedia,
            cfg_PersonalMediaAfterEnlightened,
            cfg_FiresPersonalMediaEvents;
    public ForgeConfigSpec.IntValue
            cfg_PersonalMediaMax,
            cfg_PersonalMediaRegenStep,
            cfg_PersonalMediaRegenInterval;
    public ForgeConfigSpec.DoubleValue
            cfg_MekasuitConversionRatio;

    public HexOPConfigForge(ForgeConfigSpec.Builder builder) {
        cfg_RevealsHexInsideCastingItems = builder.comment(desc_RevealsHexInsideCastingItems).define("RevealsHexInsideCastingItems", true);
        cfg_EnablesMoteChestGUI = builder.comment(desc_EnablesMoteChestGUI).define("EnablesMoteChestGUI", true);
        cfg_EnablesMishapNoYeet = builder.comment(desc_EnablesMishapNoYeet).define("EnablesMishapNoYeet", true);
        cfg_EnablesTeleportVehicles = builder.comment(desc_EnablesTeleportVehicles).define("EnablesTeleportVehicles", true);
        cfg_EnablesChargeMediaAction = builder.comment(desc_EnablesChargeMediaAction).define("EnablesChargeMediaAction", true);
        cfg_EnablesMindEnvActions = builder.comment(desc_EnablesMindEnvActions).define("EnablesMindEnvActions", true);
        cfg_EnablesPersonalMediaPool = builder.comment(desc_EnablesPersonalMediaPool).define("EnablesPersonalMediaPool", true);
        cfg_PersonalMediaMax = builder.comment(desc_PersonalMediaMax).defineInRange("PersonalMediaMax", HexOverpowered.DEFAULTS.MANA_MAX, 0, (int) 1e10);
        cfg_PersonalMediaRegenStep = builder.comment(desc_PersonalMediaRegenStep).defineInRange("PersonalMediaRegenStep", HexOverpowered.DEFAULTS.MANA_REGEN, 0, (int) 1e10);
        cfg_PersonalMediaRegenInterval = builder.comment(desc_PersonalMediaRegenInterval).defineInRange("PersonalMediaRegenInterval", HexOverpowered.DEFAULTS.MANA_REGEN_INTERVAL, 0, (int) 1e10);
        cfg_FakePlayerDontRegenMedia = builder.comment(desc_FakePlayerDontRegenMedia).define("FakePlayerDontRegenMedia", true);
        cfg_PersonalMediaAfterEnlightened = builder.comment(desc_PersonalMediaAfterEnlightened).define("PersonalMediaAfterEnlightened", true);
        cfg_FiresPersonalMediaEvents = builder.comment(desc_FiresPersonalMediaEvents).define("FiresPersonalMediaEvents", true);
        cfg_MekasuitConversionRatio = builder.comment(desc_MekasuitConversionRatio).defineInRange("MekasuitConversionRatio", 1, 0, 1e10);

        INSTANCE = this;
    }

    private static final Pair<HexOPConfigForge, ForgeConfigSpec> CFG_REGISTRY;

    static {
        CFG_REGISTRY = new ForgeConfigSpec.Builder().configure(HexOPConfigForge::new);
    }

    public static void register(ModLoadingContext ctx) {
        bindConfigImp(CFG_REGISTRY.getKey());
        ctx.registerConfig(ModConfig.Type.COMMON, CFG_REGISTRY.getValue());
    }
}
