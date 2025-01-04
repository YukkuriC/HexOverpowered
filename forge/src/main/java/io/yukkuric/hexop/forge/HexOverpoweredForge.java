package io.yukkuric.hexop.forge;

import io.yukkuric.hexop.HexOverpowered;
import io.yukkuric.hexop.forge.hexal.NexusItemCap;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import ram.talia.hexal.common.blocks.entity.BlockEntityMediafiedStorage;

@Mod(HexOverpowered.MOD_ID)
public final class HexOverpoweredForge extends HexOverpowered {
    public HexOverpoweredForge() {
        var evBus = MinecraftForge.EVENT_BUS;

        if (isModLoaded("hexal")) {
            evBus.addGenericListener(BlockEntity.class, (AttachCapabilitiesEvent e) -> {
                var o = e.getObject();
                if (!(o instanceof BlockEntityMediafiedStorage be)) return;
                e.addCapability(ID_NEXUS_INVENTORY, new NexusItemCap.Provider(be));
            });
        }

        var ctx = ModLoadingContext.get();
        HexOPConfigForge.register(ctx);
    }

    @Override
    protected boolean isModLoaded(String id) {
        return ModList.get().isLoaded(id);
    }
}
