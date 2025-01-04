package io.yukkuric.hexop.mixin.hexal;

import io.yukkuric.hexop.hexal.CachedNexusInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import ram.talia.hexal.common.blocks.BlockMediafiedStorage;

@Mixin(BlockMediafiedStorage.class)
public abstract class MixinMoteMenu extends BlockBehaviour {
    public MixinMoteMenu(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable MenuProvider getMenuProvider(BlockState blockState, Level level, BlockPos blockPos) {
        var be = level.getBlockEntity(blockPos);
        if (!(be instanceof Container container)) return null;
        var ctrl = (CachedNexusInventory.Control) be;
        ctrl.doRefresh();
        return new MenuProvider() {
            @Nullable
            public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
                switch (container.getContainerSize()) {
                    case 9:
                        return new ChestMenu(MenuType.GENERIC_9x1, i, inventory, container, 1);
                    case 18:
                        return new ChestMenu(MenuType.GENERIC_9x2, i, inventory, container, 2);
                    case 27:
                        return ChestMenu.threeRows(i, inventory, container);
                    case 36:
                        return new ChestMenu(MenuType.GENERIC_9x4, i, inventory, container, 4);
                    case 45:
                        return new ChestMenu(MenuType.GENERIC_9x5, i, inventory, container, 5);
                    case 54:
                        return ChestMenu.sixRows(i, inventory, container);
                }
                return null;
            }

            public Component getDisplayName() {
                return Component.literal("Mote Chest");
            }
        };
    }

    @Override
    public @NotNull InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (level.isClientSide) return InteractionResult.SUCCESS;

        MenuProvider menuProvider = this.getMenuProvider(blockState, level, blockPos);
        if (menuProvider != null) {
            player.openMenu(menuProvider);
            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }
}
