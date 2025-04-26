package io.yukkuric.hexop.fabric.client;

import io.yukkuric.hexop.render.HexOPHUD;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public final class HexOverpoweredFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register(HexOPHUD::onDrawMediaBar);
    }
}
