package net.arathain.charter.mixin;


import net.arathain.charter.Charter;
import net.arathain.charter.block.CharterStoneBlock;
import net.arathain.charter.block.WaystoneBlock;
import net.arathain.charter.components.CharterComponent;
import net.arathain.charter.components.CharterComponents;
import net.arathain.charter.entity.SlowFallEntity;
import net.arathain.charter.util.CharterUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.BuiltinRegistries;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.Difficulty;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.*;


@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements SlowFallEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }


    @Shadow
    protected HungerManager hungerManager;


    @Inject(method = "tick", at = @At("HEAD"))
    public void headTick(CallbackInfo info) {
        if (CharterUtil.ownsCurrentCharter(this.getBlockPos(), this.getUuid(), this.getWorld())) {
            if (isFallFlying()) {
                if (forwardSpeed > 0 && getBlockY() - charter$getAverageHeight() <= 64)
                    CharterUtil.applySpeed((PlayerEntity) (Object) this);
                charter$setSlowFalling(false);
                if ((isSneaking()) || isSubmergedInWater())
                    CharterUtil.stopFlying((PlayerEntity) (Object) this);
            } else {
                if (isOnGround() || isTouchingWater())
                    charter$setSlowFalling(false);
                if (charter$isSlowFalling()) {
                    fallDistance = 0F;
                    setVelocity(getVelocity().x, -0.0001, getVelocity().z);
                }
            }
        }
    }


    @Inject(method = "tick()V", at = @At("TAIL"))
    public void tik(CallbackInfo info) {
        if (this.hasStatusEffect(Charter.ETERNAL_DEBT) && this.getWorld().isClient) {
            getWorld().addParticle(ParticleTypes.SOUL_FIRE_FLAME, this.getX() + random.nextGaussian() / 16, this.getY(), this.getZ() + random.nextGaussian() / 16, random.nextGaussian() / 30, 0.01, random.nextGaussian() / 30);
        }
        if (this.hasStatusEffect(Charter.ETERNAL_DEBT) && !this.getWorld().isClient) {
            hungerManager.setSaturationLevel(0);
            if (this.isInsideWaterOrBubbleColumn()) {
                if (this.getEntityWorld().getBiome(this.getBlockPos()).isIn(BiomeTags.IS_RIVER)) {
                    this.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 60, 2, false, false));
                }
                this.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 40, 1, false, false));
            }
        }
    }


    @Unique
    private int charter$getAverageHeight() {
        int averageHeight = 0;
        int radius = 2;
        int diameter = (radius * 2) + 1;
        for (int x = -radius; x <= radius; x++)
            for (int z = -radius; z <= radius; z++)
                averageHeight += getWorld().getTopY(Heightmap.Type.MOTION_BLOCKING, getBlockX() + x, getBlockZ() + z);
        return averageHeight / (diameter * diameter);
    }


    // ... (rest of the existing methods)


    @Unique
    public boolean slowFalling = false;


    @Override
    public void charter$setSlowFalling(boolean slowFalling) {
        this.slowFalling = slowFalling;
    }


    @Override
    public boolean charter$isSlowFalling() {
        return slowFalling;
    }
}