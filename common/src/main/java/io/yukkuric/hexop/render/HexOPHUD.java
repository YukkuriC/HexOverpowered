package io.yukkuric.hexop.render;

import com.mojang.blaze3d.systems.RenderSystem;
import io.yukkuric.hexop.HexOverpowered;
import io.yukkuric.hexop.personal_mana.PersonalManaHolder;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class HexOPHUD {
    public static final ResourceLocation imgMediaBar = new ResourceLocation(HexOverpowered.MOD_ID, "textures/gui/media_bar.png");

    public static void onDrawMediaBar(GuiGraphics gui, float partialTicks) {
        var client = Minecraft.getInstance();
        var profiler = client.getProfiler();
        var player = client.player;

        profiler.push("media_bar");
        var mediaHolder = PersonalManaHolder.get(player);
        var value = mediaHolder.getMedia();
        var maxValue = mediaHolder.getMaxMedia();
        if (value <= 0 || maxValue <= 0) {
            profiler.pop();
            return;
        }


        int width = 182;
        int x = client.getWindow().getGuiScaledWidth() / 2 - width / 2;
        int y = client.getWindow().getGuiScaledHeight() - 29; // TODO: cfg
        width *= (double) value / (double) maxValue;

        var alpha = (float) Math.sin(Util.getMillis() / 200D) * 0.5f + 1F;
        RenderSystem.setShaderColor(1, 0.8f, 0.6f, alpha);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        gui.blit(imgMediaBar, x, y, 0, 0, width, 0, 256, 256);
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1, 1, 1, 1);

        profiler.pop();
    }
}
