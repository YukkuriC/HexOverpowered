package io.yukkuric.hexop.mixin.circle;

import at.petrak.hexcasting.api.casting.circles.ICircleComponent;
import at.petrak.hexcasting.api.casting.eval.env.CircleCastEnv;
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage;
import at.petrak.hexcasting.api.misc.MediaConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import org.spongepowered.asm.mixin.Mixin;

import java.util.EnumSet;

@Mixin(BuddingAmethystBlock.class)
public class AmethystCircle extends Block implements ICircleComponent {
    private static final int SHARD_STRENGTH_MAX = 15;
    private static final int SHARD_STRENGTH_STEP = 1;
    private static final double CHARGE_COST = MediaConstants.CRYSTAL_UNIT;
    private static LegacyRandomSource BUDDING_CHARGER = new LegacyRandomSource(114514) {
        private int cnt = 0;

        @Override
        public int nextInt(int i) {
            if (i == 5) return 0; // always grow
            cnt = (cnt + 1) % i;
            return cnt;
        }
    };

    public AmethystCircle(Properties properties) {
        super(properties);
    }

    @Override
    public ControlFlow acceptControlFlow(CastingImage imageIn, CircleCastEnv env, Direction enterDir, BlockPos pos, BlockState state, ServerLevel world) {
        // broken between waves
        if (!state.is(Blocks.BUDDING_AMETHYST)) return new ControlFlow.Stop();

        // update self
        {
            // decide grow strength
            var maxCost = (int) Math.ceil(CHARGE_COST * SHARD_STRENGTH_MAX / SHARD_STRENGTH_STEP);
            var growthStrength = (int) Math.floor((maxCost - env.extractMedia(maxCost, true)) / CHARGE_COST) * SHARD_STRENGTH_STEP;
            growthStrength = Math.min(growthStrength, SHARD_STRENGTH_MAX);
            if (growthStrength < 1) {// failed
                world.setBlockAndUpdate(pos, Blocks.AMETHYST_BLOCK.defaultBlockState());
                return new ControlFlow.Stop();
            }
            env.extractMedia((int) CHARGE_COST, false);

            // grow shards
            for (var i = 0; i < growthStrength; i++) {
                Blocks.BUDDING_AMETHYST.randomTick(state, world, pos, BUDDING_CHARGER);
            }
        }

        // next wave
        {
            var exitDirsSet = this.possibleExitDirections(pos, state, world);
            exitDirsSet.remove(enterDir.getOpposite());
            var exitDirs = exitDirsSet.stream().map((dir) -> this.exitPositionFromDirection(pos, dir));
            return new ControlFlow.Continue(imageIn, exitDirs.toList());
        }
    }

    @Override
    public boolean canEnterFromDirection(Direction direction, BlockPos blockPos, BlockState blockState, ServerLevel serverLevel) {
        return blockState.is(Blocks.BUDDING_AMETHYST);
    }

    @Override
    public EnumSet<Direction> possibleExitDirections(BlockPos blockPos, BlockState blockState, Level level) {
        return EnumSet.allOf(Direction.class);
    }

    @Override
    public BlockState startEnergized(BlockPos blockPos, BlockState blockState, Level level) {
        return blockState;
    }

    @Override
    public boolean isEnergized(BlockPos blockPos, BlockState blockState, Level level) {
        return false;
    }

    @Override
    public BlockState endEnergized(BlockPos blockPos, BlockState blockState, Level level) {
        return blockState;
    }
}
