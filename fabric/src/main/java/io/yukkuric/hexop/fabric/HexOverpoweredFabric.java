package io.yukkuric.hexop.fabric;

import at.petrak.hexcasting.common.lib.hex.HexActions;
import io.yukkuric.hexop.HexOPAttributes;
import io.yukkuric.hexop.HexOverpowered;
import io.yukkuric.hexop.actions.HexOPActions;
import io.yukkuric.hexop.actions.mind_env.OpScheduleCall;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.*;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public final class HexOverpoweredFabric extends HexOverpowered implements ModInitializer {
    @Override
    public void onInitialize() {
        var regAction = HexActions.REGISTRY;
        HexOPActions.registerActions((k, v) -> Registry.register(regAction, k, v));
        ServerTickEvents.START_SERVER_TICK.register(OpScheduleCall::ProcessQueue);
        ServerLifecycleEvents.SERVER_STARTING.register(OpScheduleCall::ResetQueue);
        ServerEntityEvents.ENTITY_LOAD.register(HexOPAttributes::applyDefaultValues);
    }

    @Override
    protected boolean isModLoaded(String id) {
        return FabricLoader.getInstance().isModLoaded(id);
    }

    public static void initSelfHook() {
    }

    // dirty work
    static {
        HexOPConfigFabric.setup();
        var regAttr = BuiltInRegistries.ATTRIBUTE;
        HexOPAttributes.registerSelf((k, v) -> Registry.register(regAttr, k, v));
    }
}
