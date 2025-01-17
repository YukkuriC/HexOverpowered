package io.yukkuric.hexop.forge;

import at.petrak.hexcasting.forge.cap.ForgeCapabilityHandler;
import at.petrak.hexcasting.forge.cap.HexCapabilities;
import io.yukkuric.hexop.HexOverpowered;
import io.yukkuric.hexop.forge.hexal.NexusItemCap;
import io.yukkuric.hexop.forge.mekanism.MekTooltip;
import io.yukkuric.hexop.forge.mekanism.MekasuitMediaHolder;
import mekanism.common.item.gear.ItemMekaSuitArmor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import ram.talia.hexal.common.blocks.entity.BlockEntityMediafiedStorage;

import java.lang.reflect.Method;

@Mod(HexOverpowered.MOD_ID)
public final class HexOverpoweredForge extends HexOverpowered {
    static Method doProvide;

    public HexOverpoweredForge() throws NoSuchMethodException {
        var evBus = MinecraftForge.EVENT_BUS;

        if (isModLoaded("hexal")) {
            evBus.addGenericListener(BlockEntity.class, (AttachCapabilitiesEvent<BlockEntity> e) -> {
                var o = e.getObject();
                if (!(o instanceof BlockEntityMediafiedStorage be)) return;
                e.addCapability(ID_NEXUS_INVENTORY, new NexusItemCap.Provider(be));
            });
        }
        if (isModLoaded("mekanism")) {
            doProvide = ForgeCapabilityHandler.class.getDeclaredMethod("provide", ItemStack.class, Capability.class, NonNullSupplier.class);
            doProvide.setAccessible(true);
            evBus.addGenericListener(ItemStack.class, (AttachCapabilitiesEvent<ItemStack> e) -> {
                var stack = e.getObject();
                if (!(stack.getItem() instanceof ItemMekaSuitArmor)) return;
                NonNullSupplier getter = () -> new MekasuitMediaHolder(stack);
                try {
                    e.addCapability(ID_MEKASUIT_MEDIA_POOL, (ICapabilityProvider) doProvide.invoke(null, stack, HexCapabilities.MEDIA, getter));
                } catch (Throwable ex) {
                    throw new RuntimeException(ex);
                }
            });
            evBus.addListener(MekTooltip::handleMekasuitTooltip);
        }

        var ctx = ModLoadingContext.get();
        HexOPConfigForge.register(ctx);
    }

    @Override
    protected boolean isModLoaded(String id) {
        return ModList.get().isLoaded(id);
    }
}
