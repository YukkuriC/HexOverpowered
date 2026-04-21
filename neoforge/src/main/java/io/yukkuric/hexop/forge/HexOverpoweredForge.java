package io.yukkuric.hexop.forge;

import at.petrak.hexcasting.common.lib.HexRegistries;
import at.petrak.hexcasting.forge.cap.HexCapabilities;
import io.yukkuric.hexop.HexOPAttributes;
import io.yukkuric.hexop.HexOverpowered;
import io.yukkuric.hexop.actions.HexOPActions;
import io.yukkuric.hexop.actions.mind_env.OpScheduleCall;
import io.yukkuric.hexop.forge.interop.mekanism.MekTooltip;
import io.yukkuric.hexop.forge.interop.mekanism.MekasuitMediaHolder;
import mekanism.common.item.gear.ItemMekaSuitArmor;
import mekanism.common.registries.MekanismItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.apache.commons.lang3.NotImplementedException;

@Mod(HexOverpowered.MOD_ID)
public final class HexOverpoweredForge extends HexOverpowered {
    public HexOverpoweredForge(ModContainer modContainer) {
        var evBus = NeoForge.EVENT_BUS;
        evBus.addListener((ServerTickEvent.Pre event) -> {
            OpScheduleCall.ProcessQueue(event.getServer());
        });
        evBus.addListener((ServerStartingEvent event) -> OpScheduleCall.ResetQueue(event.getServer()));

        if (HexOverpowered.IsModLoaded("hexal")) {
            /*
            evBus.addListener((RegisterCapabilitiesEvent e) -> {
                e.registerBlockEntity(
                        HexCapabilities.MEDIA_HANDLER.block(),
                        BlockEntityMediafiedStorage.class,
                        (be, direction) -> new NexusItemCap(be)
                );
            });
            */
            throw new NotImplementedException("until hexal 1.21");
        }
        if (HexOverpowered.IsModLoaded("mekanism")) {
            evBus.addListener((RegisterCapabilitiesEvent e) -> {
                e.registerItem(
                        HexCapabilities.Item.MEDIA,
                        (stack, ctx) -> {
                            if (!(stack.getItem() instanceof ItemMekaSuitArmor)) return null;
                            return new MekasuitMediaHolder(stack);
                        },
                        MekanismItems.MEKASUIT_HELMET,
                        MekanismItems.MEKASUIT_BODYARMOR,
                        MekanismItems.MEKASUIT_PANTS,
                        MekanismItems.MEKASUIT_BOOTS
                );
            });
            evBus.addListener(MekTooltip::handleMekasuitTooltip);
        }

        var modBus = modContainer.getEventBus();
        modBus.addListener((RegisterEvent event) -> {
            var key = event.getRegistryKey();
            if (key.equals(HexRegistries.ACTION)) {
                HexOPActions.registerActions((k, v) -> event.register(HexRegistries.ACTION, k, () -> v));
            } else if (key.equals(Registries.ATTRIBUTE)) {
                HexOPAttributes.registerSelf((k, v) -> event.register(Registries.ATTRIBUTE, k, () -> v));
            }
        });

        modBus.addListener((EntityAttributeModificationEvent e) -> {
            for (var attr : HexOPAttributes.getAllHolders())
                e.add(EntityType.PLAYER, attr);
        });
        evBus.addListener((EntityJoinLevelEvent e) -> HexOPAttributes.applyDefaultValues(e.getEntity(), e.getLevel()));

        HexOPConfigForge.register(modContainer);

        HexOPXPlatForge.init();
    }
}
