package io.yukkuric.hexop.fabric;

import io.yukkuric.hexop.HexOPAttributes;
import io.yukkuric.hexop.HexOverpowered;
import io.yukkuric.hexop.actions.mind_env.OpScheduleCall;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.*;

public final class HexOverpoweredFabric extends HexOverpowered implements ModInitializer {
    @Override
    public void onInitialize() {
        ServerTickEvents.START_SERVER_TICK.register(OpScheduleCall::ProcessQueue);
        ServerLifecycleEvents.SERVER_STARTING.register(OpScheduleCall::ResetQueue);
        ServerEntityEvents.ENTITY_LOAD.register(HexOPAttributes::applyDefaultValues);
    }

    public static void initSelfHook() {
    }

    // dirty work
    static {
        HexOPConfigFabric.setup();
        HexOPAttributes.registerSelf();
    }
}
