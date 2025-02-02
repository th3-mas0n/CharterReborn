package net.arathain.charter.block;

import net.arathain.charter.Charter;
import net.arathain.charter.block.entity.CharterStoneEntity;
import net.arathain.charter.block.particle.BindingAmbienceParticleEffect;
import net.arathain.charter.components.CharterComponent;
import net.arathain.charter.components.CharterComponents;
import net.arathain.charter.util.CharterUtil;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;
import net.minecraft.util.math.random.Random;

public class MonolithBlock extends Block implements Waterloggable, BlockEntityProvider {
    public static final VoxelShape SHAPE = createCuboidShape(0, 0, 0,16, 39, 16);

    private static final int RADIUS = 85; // Half of 170


    public MonolithBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(Properties.WATERLOGGED, false));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        if (CharterUtil.getCharterAtPos(pos, (World) world) != null) {
            return false;
        } else {
            return super.canPlaceAt(state, world, pos);
        }
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        if (state.contains(Properties.WATERLOGGED) && state.get(Properties.WATERLOGGED)) {
            world.createAndScheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
    }



    @Override
    public boolean tryFillWithFluid(WorldAccess world, BlockPos pos, BlockState state, FluidState fluidState) {
        if (!state.get(Properties.WATERLOGGED) && fluidState.getFluid() == Fluids.WATER) {
            if (!world.isClient()) {
                world.setBlockState(pos, state.with(Properties.WATERLOGGED, true), 3);
                world.createAndScheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
            }
            return true;
        }
        return false;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        final BlockState state = this.getDefaultState();
        if (state.contains(Properties.WATERLOGGED)) {
            final FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
            final boolean source = fluidState.isIn(FluidTags.WATER) && fluidState.getLevel() == 8;
            return state.with(Properties.WATERLOGGED, source);
        }
        return state;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.contains(Properties.WATERLOGGED) && state.get(Properties.WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CharterStoneEntity(pos, state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder.add(Properties.WATERLOGGED));
    }


    public void onPlaced(World world, BlockPos pos, BlockState state, PlayerEntity placer, net.minecraft.item.ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);

        if (!world.isClient) {
            createBarrier((ServerWorld) world, pos);
        }
    }

    private void createBarrier(ServerWorld world, BlockPos center) {
        for (int x = -RADIUS; x <= RADIUS; x++) {
            for (int y = -RADIUS; y <= RADIUS; y++) {
                for (int z = -RADIUS; z <= RADIUS; z++) {
                    if (Math.abs(x) == RADIUS || Math.abs(y) == RADIUS || Math.abs(z) == RADIUS) {
                        Vec3d particlePos = new Vec3d(center.getX() + x + 0.5, center.getY() + y + 0.5, center.getZ() + z + 0.5);

                        // Alternate between Soul Flame and Soul particles
                        DefaultParticleType particleType = (x + y + z) % 2 == 0
                                ? ParticleTypes.SOUL_FIRE_FLAME // Soul Flame particle
                                : ParticleTypes.SOUL;          // Soul particle

                        // Spawn the particle
                        world.spawnParticles(particleType,
                                particlePos.x, particlePos.y, particlePos.z,
                                1, // Count
                                0.0, 0.0, 0.0, // Offset
                                0.01); // Speed
                    }
                }
            }
        }
    }

    public static boolean isInsideBarrier(BlockPos center, BlockPos testPos) {
        Box barrierBox = new Box(center).expand(RADIUS);
        return !barrierBox.contains(Vec3d.of(testPos));
    }
}