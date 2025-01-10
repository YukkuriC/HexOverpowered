package io.yukkuric.hexop.forge;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

import static io.yukkuric.hexop.HexOPConfig.*;

public class HexOPConfigForge implements API {
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

    public HexOPConfigForge(ForgeConfigSpec.Builder builder) {
        cfgEnablesMoteChestGUI = builder.comment(DESCRIP_MOTE_GLANCE).define("EnablesMoteChestGUI", true);
        cfgEnablesMishapNoYeet = builder.comment(DESCRIP_NO_YEET).define("EnablesMishapNoYeet", true);
        cfgEnablesTeleportVehicles = builder.comment(DESCRIP_TP_VEHICLES).define("EnablesTeleportVehicles", true);
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
