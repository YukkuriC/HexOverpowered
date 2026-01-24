package io.yukkuric.hexop.forge;

import at.petrak.hexcasting.forge.cap.ForgeCapabilityHandler;
import at.petrak.hexcasting.forge.cap.HexCapabilities;
import io.yukkuric.hexop.HexOPAttributes;
import io.yukkuric.hexop.HexOverpowered;
import io.yukkuric.hexop.actions.mind_env.OpScheduleCall;
import io.yukkuric.hexop.forge.hexal.NexusItemCap;
import io.yukkuric.hexop.forge.interop.mekanism.MekTooltip;
import io.yukkuric.hexop.forge.interop.mekanism.MekasuitMediaHolder;
import mekanism.common.item.gear.ItemMekaSuitArmor;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;
import ram.talia.hexal.common.blocks.entity.BlockEntityMediafiedStorage;

import java.lang.reflect.Method;

@Mod(HexOverpowered.MOD_ID)
public final class HexOverpoweredForge extends HexOverpowered {
    static Method doProvide;

    public HexOverpoweredForge() throws NoSuchMethodException {
        var evBus = MinecraftForge.EVENT_BUS;
        evBus.addListener((TickEvent.ServerTickEvent event) -> {
            if (event.phase == TickEvent.Phase.START) OpScheduleCall.ProcessQueue(event.getServer());
        });
        evBus.addListener((ServerStartingEvent event) -> OpScheduleCall.ResetQueue(event.getServer()));

        if (HexOverpowered.IsModLoaded("hexal")) {
            evBus.addGenericListener(BlockEntity.class, (AttachCapabilitiesEvent<BlockEntity> e) -> {
                var o = e.getObject();
                if (!(o instanceof BlockEntityMediafiedStorage be)) return;
                e.addCapability(ID_NEXUS_INVENTORY, new NexusItemCap.Provider(be));
            });
        }
        if (HexOverpowered.IsModLoaded("mekanism")) {
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

        var modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener((RegisterEvent event) -> {
            var key = event.getRegistryKey();
            if (key.equals(Registry.ATTRIBUTE_REGISTRY)) {
                HexOPAttributes.registerSelfForge((k, v) -> event.register(Registry.ATTRIBUTE_REGISTRY, k, () -> v));
            }
        });

        modBus.addListener((EntityAttributeModificationEvent e) -> {
            for (var attr : HexOPAttributes.getAll())
                e.add(EntityType.PLAYER, attr);
        });
        evBus.addListener((EntityJoinLevelEvent e) -> HexOPAttributes.applyDefaultValues(e.getEntity(), e.getLevel()));

        var ctx = ModLoadingContext.get();
        HexOPConfigForge.register(ctx);
    }
}
