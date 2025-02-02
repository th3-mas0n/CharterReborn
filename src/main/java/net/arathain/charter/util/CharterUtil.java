package net.arathain.charter.util;

import net.arathain.charter.components.CharterComponent;
import net.arathain.charter.components.CharterComponents;
import net.arathain.charter.entity.SlowFallEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.*;


public class CharterUtil {

    /**
     * Fetches the charter component at a specific position in the world (using Vec3d).
     *
     * @param pos   The position to check.
     * @param world The world to search in.
     * @return The found CharterComponent, or null if none exist at the position.
     */
    public static CharterComponent getCharterAtPos(Vec3d pos, World world) {
        if (pos == null || world == null) return null;

        List<CharterComponent> charters = CharterComponents.CHARTERS.get(world).getCharters();
        for (CharterComponent potentialComponent : charters) {
            for (Box box : potentialComponent.getAreas()) {
                if (box.contains(pos)) {
                    return potentialComponent;
                }
            }
        }
        return null;
    }

    /**
     * Fetches the charter component at a specific position in the world (using BlockPos).
     *
     * @param blockPos The block position to check.
     * @param world    The world to search in.
     * @return The found CharterComponent, or null if none exist at the position.
     */
    public static CharterComponent getCharterAtPos(BlockPos blockPos, World world) {
        if (blockPos == null || world == null) return null;

        List<CharterComponent> charters = CharterComponents.CHARTERS.get(world).getCharters();
        for (CharterComponent potentialComponent : charters) {
            for (Box box : potentialComponent.getAreas()) {
                if (box.contains(blockPos.getX(), blockPos.getY(), blockPos.getZ())) {
                    return potentialComponent;
                }
            }
        }
        return null;
    }

    /**
     * Checks if a player is in any charter.
     *
     * @param player The player entity to check.
     * @param world  The world to check in.
     * @return True if the player is part of any charter, false otherwise.
     */
    public static boolean isInCharter(PlayerEntity player, World world) {
        if (player == null || world == null) return false;

        List<CharterComponent> charters = CharterComponents.CHARTERS.get(world).getCharters();
        for (CharterComponent potentialComponent : charters) {
            if (potentialComponent.getMembers().contains(player.getUuid())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a player (by UUID) is in any charter.
     *
     * @param playerUuid The UUID of the player to check.
     * @param world      The world to check in.
     * @return True if the player is part of any charter, false otherwise.
     */
    public static boolean isInCharter(UUID playerUuid, World world) {
        if (playerUuid == null || world == null) return false;

        List<CharterComponent> charters = CharterComponents.CHARTERS.get(world).getCharters();
        for (CharterComponent potentialComponent : charters) {
            if (potentialComponent.getMembers().contains(playerUuid)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a player owns the charter at a specific position.
     *
     * @param pos    The position of the charter.
     * @param player The player entity to check.
     * @param world  The world to check in.
     * @return True if the player owns the charter, false otherwise.
     */
    public static boolean ownsCurrentCharter(BlockPos pos, PlayerEntity player, World world) {
        if (pos == null || player == null || world == null) return false;

        CharterComponent charter = getCharterAtPos(pos, world);
        return charter != null && charter.getCharterOwnerUuid().equals(player.getUuid());
    }

    /**
     * Checks if a player (by UUID) owns the charter at a specific position.
     *
     * @param pos        The position of the charter.
     * @param playerUuid The UUID of the player to check.
     * @param world      The world to check in.
     * @return True if the player owns the charter, false otherwise.
     */
    public static boolean ownsCurrentCharter(BlockPos pos, UUID playerUuid, World world) {
        if (pos == null || playerUuid == null || world == null) return false;

        CharterComponent charter = getCharterAtPos(pos, world);
        return charter != null && charter.getCharterOwnerUuid().equals(playerUuid);
    }

    /**
     * Checks if a living entity owns any charter.
     *
     * @param entity The living entity to check.
     * @param world  The world to check in.
     * @return True if the entity owns a charter, false otherwise.
     */
    public static boolean ownsCharter(LivingEntity entity, World world) {
        if (entity == null || world == null || !(entity instanceof PlayerEntity)) return false;

        List<CharterComponent> charters = CharterComponents.CHARTERS.get(world).getCharters();
        for (CharterComponent charter : charters) {
            if (charter.getCharterOwnerUuid().equals(entity.getUuid())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Applies a custom speed boost to the player.
     *
     * @param player The player entity to modify speed for.
     */
    public static void applySpeed(PlayerEntity player) {
        if (player == null) return;

        ((SlowFallEntity) player).charter$setSlowFalling(false);

        Vec3d rotation = player.getRotationVector();
        Vec3d velocity = player.getVelocity();
        float speedMultiplier = player.getPitch() < -75 && player.getPitch() > -105 ? 0.06f : 0.03f;

        player.setVelocity(velocity.add(rotation.x * speedMultiplier, rotation.y * speedMultiplier, rotation.z * speedMultiplier));
    }

    /**
     * Stops the player's flying mode and adjusts pitch/yaw.
     *
     * @param player The player entity to modify.
     */
    public static void stopFlying(PlayerEntity player) {
        if (player == null) return;

        ((SlowFallEntity) player).charter$setSlowFalling(true);

        if (player.getPitch() < -90 || player.getPitch() > 90) {
            player.setPitch(player.getPitch() > 0 ? -180 + player.getPitch() : 180 + player.getPitch());
            player.setYaw(180 + player.getYaw());
        }

        player.stopFallFlying();
    }
}