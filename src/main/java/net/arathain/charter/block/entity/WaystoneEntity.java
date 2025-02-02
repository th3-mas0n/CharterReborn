package net.arathain.charter.block.entity;

import net.arathain.charter.Charter;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class WaystoneEntity extends BlockEntity implements GeoAnimatable {
    private final AnimatableInstanceCache factory = new AnimatableInstanceCache(this);
    public static final RawAnimation IDLE = new RawAnimation().addAnimation("animation.model.idle");

    public WaystoneEntity(BlockPos pos, BlockState state) {
        super(Charter.WAYSTONE_ENTITY, pos, state);
    }

    @Override
    public void registerControllers(AnimatableManager animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "controller", 2, animationEvent -> {
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
