package net.arathain.charter.item;


import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import java.util.List;


public class DawnBrakerItem extends SwordItem {
    public DawnBrakerItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }


    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // Use getDamageSources().create() method
        if (attacker.getWorld().isDay()) {
            target.damage(attacker.getWorld().getDamageSources().mobProjectile(attacker, null), (float) (getAttackDamage() * 0.2));
        }
        return super.postHit(stack, target, attacker);
    }


    public void inventoryTick(ItemStack stack, World world, LivingEntity entity, int slot, boolean selected) {
        if (!world.isClient() && entity instanceof PlayerEntity player) {
            if (world.isDay() && selected && world.getTime() % 200 == 0) {
                player.heal(2.0F);
            }
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.charter.dawnbreaker.tooltip").formatted(Formatting.GOLD));
        super.appendTooltip(stack, world, tooltip, context);
    }

    private boolean isDaytime(World world) {
        return world.getTimeOfDay() % 24000L < 12000L; // 0-12000 ticks = daytime
    }

    private float getBonusDamage() {
        return 10.0F * 0.2F; // 20% bonus
    }
}