package io.yukkuric.hexop.forge;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

import static io.yukkuric.hexop.HexOPConfig.*;

public class HexOPConfigForge implements API {
    public static HexOPConfigForge INSTANCE;
    public static final String DESCRIP_MEKASUIT_RATIO = "How many media points each FE point equals";

    public static double MekasuitConversionRatio() {
        return INSTANCE.cfgMekasuitConversionRatio.get();
    }

    @Override
    public boolean EnablesMoteChestGUI() {
        return cfgEnablesMoteChestGUI.get();
    }

    @Override
    public boolean EnablesMishapNoYeet() {
        return cfgEnablesMishapNoYeet.get();
    }

    @Override
    public boolean EnablesTeleportVehicles() {
        return cfgEnablesTeleportVehicles.get();
    }

    public ForgeConfigSpec.BooleanValue
            cfgEnablesTeleportVehicles,
            cfgEnablesMishapNoYeet,
            cfgEnablesMoteChestGUI;
    public ForgeConfigSpec.DoubleValue cfgMekasuitConversionRatio;

    public HexOPConfigForge(ForgeConfigSpec.Builder builder) {
        cfgEnablesMoteChestGUI = builder.comment(DESCRIP_MOTE_GLANCE).define("EnablesMoteChestGUI", true);
        cfgEnablesMishapNoYeet = builder.comment(DESCRIP_NO_YEET).define("EnablesMishapNoYeet", true);
        cfgEnablesTeleportVehicles = builder.comment(DESCRIP_TP_VEHICLES).define("EnablesTeleportVehicles", true);
        cfgMekasuitConversionRatio = builder.comment(DESCRIP_MEKASUIT_RATIO).defineInRange("MekasuitConversionRatio", 1, 0, 1e10);
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
