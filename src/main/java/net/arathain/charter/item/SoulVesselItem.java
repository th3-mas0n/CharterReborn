package net.arathain.charter.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;


public class SoulVesselItem extends Item {

    public SoulVesselItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if(!isViable(stack)) {
            user.setStackInHand(hand, putSoulVessel(stack, user));
            stack.getOrCreateNbt().putInt("CustomModelData", 1);
            return TypedActionResult.success(user.getStackInHand(hand));
        } else {
            return TypedActionResult.pass(user.getStackInHand(hand));
        }
    }
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // Check if the attacker is a player
        if (attacker instanceof PlayerEntity attackerPlayer) {
            // Check if the target is a player
            if (target instanceof PlayerEntity targetPlayer) {
                // If the item does not already have a soul stored
                if (!isViable(stack)) {
                    putSoulVessel(stack, targetPlayer); // Bind the soul of the target player to the item
                    stack.getOrCreateNbt().putInt("CustomModelData", 2); // Update model for "contract signed"
                    attackerPlayer.sendMessage(Text.literal("You have taken the soul of " + targetPlayer.getDisplayName().getString()).formatted(Formatting.GOLD), true);
                    return true; // Indicate successful hit action
                } else {
                    // Optional: Notify the attacker that the item already has a soul
                    attackerPlayer.sendMessage(Text.literal("This vessel already contains a soul!").formatted(Formatting.GOLD), true);
                }
            }
        }
        return super.postHit(stack, target, attacker); // Continue normal behavior
    }
    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (isViable(stack)) {
            tooltip.add(Text.literal("Soul").formatted(Formatting.OBFUSCATED));
            tooltip.add(Text.literal("Contains the soul of " + getSoulName(stack)).formatted(Formatting.GOLD));
            tooltip.add(Text.literal("Retard").formatted(Formatting.OBFUSCATED));

        }
    }

    public static ItemStack putSoulVessel(ItemStack stack, PlayerEntity entity) {
        stack.getOrCreateNbt().putUuid("SoulUUID", entity.getUuid());
        stack.getOrCreateNbt().putString("SoulName", entity.getDisplayName().getString());
        return stack;
    }

    public static ItemStack copyTo(ItemStack from, ItemStack to) {
        if (isViable(from)) {
            to.getOrCreateNbt().putUuid("SoulUUID", from.getOrCreateNbt().getUuid("SoulUUID"));
            to.getOrCreateNbt().putString("SoulName", from.getOrCreateNbt().getString("SoulName"));
        }
        return to;
    }

    public static boolean isViable(ItemStack stack) {
        if (!stack.hasNbt()) return false;
        assert stack.getNbt() != null;
        return stack.getNbt().contains("SoulUUID") && stack.getOrCreateNbt().getUuid("SoulUUID") != null;
    }

    public static void removeDebt(ItemStack stack) {
        if (stack.hasNbt()) {
            stack.getOrCreateNbt().remove("SoulUUID");
            stack.getOrCreateNbt().remove("SoulName");
        }
    }

    public static UUID getSoulUUID(ItemStack stack) {
        if (isViable(stack)) {
            return stack.getOrCreateNbt().getUuid("SoulUUID");
        }
        return null;
    }

    public static String getSoulName(ItemStack stack) {
        if (isViable(stack)) {
            return stack.getOrCreateNbt().getString("SoulName");
        }
        return "";
    }

}
