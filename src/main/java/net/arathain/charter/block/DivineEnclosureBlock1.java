package net.arathain.charter.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;

public class DivineEnclosureBlock1 extends Block {
    public static final VoxelShape SHAPE = createCuboidShape(4, 0, 4, 12, 32, 12);

    private static final int RADIUS = 85; // Half of 170

    public DivineEnclosureBlock1(Settings settings) {
        super (settings);
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