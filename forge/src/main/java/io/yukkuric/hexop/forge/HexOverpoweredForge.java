package io.yukkuric.hexop.forge;

import io.yukkuric.hexop.HexOverpowered;
import net.minecraftforge.fml.common.Mod;

@Mod(HexOverpowered.MOD_ID)
public final class HexOverpoweredForge {
    public HexOverpoweredForge() {
        // Run our common setup.
        HexOverpowered.init();
    }
}
