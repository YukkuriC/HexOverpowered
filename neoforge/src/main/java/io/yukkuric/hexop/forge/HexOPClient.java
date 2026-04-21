package io.yukkuric.hexop.forge;

import io.yukkuric.hexop.HexOverpowered;
import io.yukkuric.hexop.render.HexOPHUD;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@EventBusSubscriber(modid = HexOverpowered.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class HexOPClient {
    @SubscribeEvent
    public static void onRenderHUD(RegisterGuiLayersEvent e) {
        e.registerAbove(VanillaGuiLayers.EXPERIENCE_BAR, HexOverpowered.opModLoc("media_bar"),
                (gui, poseStack) -> HexOPHUD.onDrawMediaBar(gui));
    }
}
