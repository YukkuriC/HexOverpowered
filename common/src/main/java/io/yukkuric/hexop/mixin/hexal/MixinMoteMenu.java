package io.yukkuric.hexop.mixin.hexal;

import io.yukkuric.hexop.HexOPConfig;
import io.yukkuric.hexop.hexal.CachedNexusInventory;
import io.yukkuric.hexop.hexal.CompressedChestMenu;
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
        var ctrl = ((CachedNexusInventory.Control) be);
        var api = ctrl.getAPI();
        api.doForceRefresh();
        return new MenuProvider() {
            @Nullable
            public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
                return new CompressedChestMenu(i, inventory, ctrl.wrapForChest(), api);
            }

            public Component getDisplayName() {
                return Component.translatable("hexop.mote.chest.title");
            }
        };
    }

    @Override
    public @NotNull InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (level.isClientSide) return InteractionResult.SUCCESS;
        if (!HexOPConfig.EnablesMoteChestGUI() || !HexOPConfig.EnablesMoteItemHandler()) return InteractionResult.PASS;

        MenuProvider menuProvider = this.getMenuProvider(blockState, level, blockPos);
        if (menuProvider != null) {
            player.openMenu(menuProvider);
            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }
}
