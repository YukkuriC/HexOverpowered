package io.yukkuric.hexop.forge;

import io.yukkuric.hexop.HexOverpowered;
import io.yukkuric.hexop.render.HexOPHUD;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HexOverpowered.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class HexOPClient {
    @SubscribeEvent
    public static void onRenderHUD(RegisterGuiOverlaysEvent e) {
        e.registerAbove(VanillaGuiOverlay.EXPERIENCE_BAR.id(), "hexop_media_bar",
                (gui, poseStack, partialTick, width, height) -> HexOPHUD.onDrawMediaBar(poseStack, partialTick));
    }
}
