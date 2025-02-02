package net.arathain.charter.item;


import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;

public class RepercussionEffect extends StatusEffect {

    public RepercussionEffect() {
        super(StatusEffectCategory.NEUTRAL, -2320877); // Black color for the effect
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity instanceof PlayerEntity player) {

            player.addStatusEffect(new net.minecraft.entity.effect.StatusEffectInstance(
                    StatusEffects.DARKNESS, 20, amplifier, true, false));

            player.addStatusEffect(new net.minecraft.entity.effect.StatusEffectInstance(
                    StatusEffects.WEAKNESS, 20, 255, true, false));

            player.addStatusEffect(new net.minecraft.entity.effect.StatusEffectInstance(
                    StatusEffects.MINING_FATIGUE, 20, 255, true, false));

            player.addStatusEffect(new net.minecraft.entity.effect.StatusEffectInstance(
                    StatusEffects.GLOWING, 20, 255, true, false));


        }

        super.applyUpdateEffect(entity, amplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true; // Update every tick
    }
}