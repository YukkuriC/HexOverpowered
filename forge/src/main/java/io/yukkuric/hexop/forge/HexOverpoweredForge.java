package io.yukkuric.hexop.forge;

import at.petrak.hexcasting.common.lib.HexRegistries;
import at.petrak.hexcasting.forge.cap.ForgeCapabilityHandler;
import at.petrak.hexcasting.forge.cap.HexCapabilities;
import io.yukkuric.hexop.HexOPAttributes;
import io.yukkuric.hexop.HexOverpowered;
import io.yukkuric.hexop.actions.HexOPActions;
import io.yukkuric.hexop.actions.mind_env.OpScheduleCall;
import io.yukkuric.hexop.forge.hexal.NexusItemCap;
import io.yukkuric.hexop.forge.mekanism.MekTooltip;
import io.yukkuric.hexop.forge.mekanism.MekasuitMediaHolder;
import mekanism.common.item.gear.ItemMekaSuitArmor;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;
import ram.talia.hexal.common.blocks.entity.BlockEntityMediafiedStorage;

@Mod(HexOverpowered.MOD_ID)
public final class HexOverpoweredForge extends HexOverpowered {
    public HexOverpoweredForge() {
        var evBus = MinecraftForge.EVENT_BUS;
        evBus.addListener((TickEvent.ServerTickEvent event) -> {
            if (event.phase == TickEvent.Phase.START) OpScheduleCall.ProcessQueue(event.getServer());
        });
        evBus.addListener((ServerStartingEvent event) -> OpScheduleCall.ProcessQueue(event.getServer()));

        if (isModLoaded("hexal")) {
            evBus.addGenericListener(BlockEntity.class, (AttachCapabilitiesEvent<BlockEntity> e) -> {
                var o = e.getObject();
                if (!(o instanceof BlockEntityMediafiedStorage be)) return;
                e.addCapability(ID_NEXUS_INVENTORY, new NexusItemCap.Provider(be));
            });
        }
        if (isModLoaded("mekanism")) {
            evBus.addGenericListener(ItemStack.class, (AttachCapabilitiesEvent<ItemStack> e) -> {
                var stack = e.getObject();
                if (!(stack.getItem() instanceof ItemMekaSuitArmor)) return;
                e.addCapability(ID_MEKASUIT_MEDIA_POOL, ForgeCapabilityHandler.provide(stack, HexCapabilities.MEDIA, () -> new MekasuitMediaHolder(stack)));
            });
            evBus.addListener(MekTooltip::handleMekasuitTooltip);
        }

        var modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener((RegisterEvent event) -> {
            var key = event.getRegistryKey();
            if (key.equals(HexRegistries.ACTION)) {
                HexOPActions.registerActions();
            } else if (key.equals(Registries.ATTRIBUTE)) {
                HexOPAttributes.registerSelf();
            }
        });

        modBus.addListener((EntityAttributeModificationEvent e) -> {
            for (var attr : HexOPAttributes.getAll())
                e.add(EntityType.PLAYER, attr);
        });

        var ctx = ModLoadingContext.get();
        HexOPConfigForge.register(ctx);
    }

    @Override
    protected boolean isModLoaded(String id) {
        return ModList.get().isLoaded(id);
    }
}
