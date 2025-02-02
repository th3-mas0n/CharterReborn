package net.arathain.charter.block.entity;

import net.arathain.charter.Charter;
import net.arathain.charter.components.CharterComponent;
import net.arathain.charter.components.CharterComponents;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.Optional;
import java.util.UUID;

public class CharterStoneEntity extends BlockEntity implements GeoAnimatable {
    private final AnimatableInstanceCache factory = new AnimatableInstanceCache(this);
    public static final RawAnimation IDLE = new RawAnimation().addAnimation("animation.model.idle");

    public CharterStoneEntity(BlockPos pos, BlockState state) {
        super(Charter.CHARTER_STONE_ENTITY, pos, state);
    }


    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
    }

    @Override
    public void registerControllers(AnimatableManager animationData) {
        animationData.addController(new AnimationController<>(this, "controller", 2, animationEvent -> {
            RawAnimation anime = IDLE;
            animationEvent.getController().setAnimation(anime);
            return PlayState.CONTINUE;
        }));
    }

    @Override
    public AnimatableInstanceCache getFactory() {
        return factory;
    }
}
