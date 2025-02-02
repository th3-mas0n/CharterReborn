package net.arathain.charter.item;


import com.google.common.collect.ImmutableMultimap;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ToolMaterial;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class DawnBrakerItem extends SwordItem {
    private static final String KILL_COUNT_KEY = "KillCount";
    private static final String CHARGED_KEY = "Charged";
    private static final int KILL_THRESHOLD = 10;
    public DawnBrakerItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(ReachEntityAttributes.REACH, new EntityAttributeModifier("Attack range", 1.2D, EntityAttributeModifier.Operation.ADDITION));
        builder.put(ReachEntityAttributes.ATTACK_RANGE, new EntityAttributeModifier("Attack range", 1.2D, EntityAttributeModifier.Operation.ADDITION));

    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!attacker.world.isClient && target.isDead() && attacker instanceof PlayerEntity) {
            incrementKillCount(stack);
            if (getKillCount(stack) >= KILL_THRESHOLD) {
                setCharged(stack, true);
            }
        }
        return super.postHit(stack, target, attacker);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (isCharged(stack)) {
            createExplosion(user, world);
            setCharged(stack, false);
            resetKillCount(stack);
            return TypedActionResult.success(stack);
        }
        return TypedActionResult.pass(stack);
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        int killCount = getKillCount(stack);
        boolean charged = isCharged(stack);

        tooltip.add(Text.literal("Kill Count: " + killCount).formatted(Formatting.GOLD));
        tooltip.add(Text.literal(charged ? "Fully Charged!" : "Charging...").formatted(charged ? Formatting.GOLD : Formatting.GOLD));
        super.appendTooltip(stack, world, tooltip, context);
    }

    private void createExplosion(PlayerEntity user, World world) {
        Vec3d userPos = user.getPos();
        Box explosionBox = new Box(userPos.add(-5, -5, -5), userPos.add(5, 5, 5));
        List<LivingEntity> entities = world.getEntitiesByClass(LivingEntity.class, explosionBox, entity -> entity != user);

        for (LivingEntity entity : entities) {
            entity.damage(user.getRecentDamageSource().explosion(user), 10.0F);
        }

        world.syncWorldEvent(null, 2001, user.getBlockPos(), 0); // Visual effect
    }

    private int getKillCount(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        return nbt.getInt(KILL_COUNT_KEY);
    }

    private void incrementKillCount(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putInt(KILL_COUNT_KEY, getKillCount(stack) + 1);
    }

    private void resetKillCount(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putInt(KILL_COUNT_KEY, 0);
    }

    private boolean isCharged(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        return nbt.getBoolean(CHARGED_KEY);
    }

    private void setCharged(ItemStack stack, boolean charged) {
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putBoolean(CHARGED_KEY, charged);
    }
}