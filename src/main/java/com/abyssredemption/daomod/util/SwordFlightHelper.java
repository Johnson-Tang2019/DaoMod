package com.abyssredemption.daomod.util;

import com.abyssredemption.daomod.attachment.CultivationData;
import com.abyssredemption.daomod.item.SoulSwordItem;
import com.abyssredemption.daomod.network.CultivationPayload;
import com.abyssredemption.daomod.registry.ModAttachments;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public final class SwordFlightHelper {
    public static final int MIN_REALM = 1;
    private static final long TAKEOFF_QI_COST = 5;
    private static final long FLIGHT_QI_COST = 1;
    private static final int QI_INTERVAL = 10;
    private static final int DURABILITY_INTERVAL = 20;

    private SwordFlightHelper() {
    }

    public static boolean isSwordFlightSword(ItemStack stack) {
        return stack.getItem() instanceof SoulSwordItem;
    }

    public static boolean canUseSwordFlight(Player player, ItemStack stack) {
        if (!(stack.getItem() instanceof SoulSwordItem sword)) {
            return false;
        }

        var data = player.getData(ModAttachments.CULTIVATION);
        return data.getRealm() >= MIN_REALM
                && !sword.isAboveRealm(player)
                && data.getQi() > 0
                && canSpendDurability(stack);
    }

    public static boolean canStartSwordFlight(Player player, ItemStack stack) {
        return canUseSwordFlight(player, stack)
                && !player.onGround()
                && !player.isFallFlying()
                && !player.isInWater()
                && !player.isPassenger();
    }

    public static boolean tryStartSwordFlight(Level level, Player player, InteractionHand hand, ItemStack stack) {
        if (!canStartSwordFlight(player, stack)) {
            return false;
        }

        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            var data = serverPlayer.getData(ModAttachments.CULTIVATION);
            if (data.getQi() < TAKEOFF_QI_COST) {
                serverPlayer.displayClientMessage(Component.literal("炁不足，无法御剑起势。"), true);
                return false;
            }

            data.setQi(data.getQi() - TAKEOFF_QI_COST);
            serverPlayer.setData(ModAttachments.CULTIVATION, data);
            syncCultivation(serverPlayer, data);
            serverPlayer.startFallFlying();
            serverPlayer.getCooldowns().addCooldown(stack.getItem(), 20);
            serverPlayer.displayClientMessage(Component.literal("御剑乘风。"), true);
        }

        return true;
    }

    public static void tickSwordFlight(ServerPlayer player) {
        if (!player.isFallFlying()) {
            return;
        }

        HandStack handStack = findFlightSword(player);
        if (handStack == null || !canUseSwordFlight(player, handStack.stack())) {
            stopSwordFlight(player, "御剑中断。");
            return;
        }

        var data = player.getData(ModAttachments.CULTIVATION);
        if (player.tickCount % QI_INTERVAL == 0) {
            if (data.getQi() < FLIGHT_QI_COST) {
                stopSwordFlight(player, "炁已枯竭，御剑难继。");
                return;
            }
            data.setQi(data.getQi() - FLIGHT_QI_COST);
            player.setData(ModAttachments.CULTIVATION, data);
            syncCultivation(player, data);
        }

        if (player.tickCount % DURABILITY_INTERVAL == 0) {
            handStack.stack().hurtAndBreak(1, player, LivingEntity.getSlotForHand(handStack.hand()));
            if (!canSpendDurability(handStack.stack())) {
                stopSwordFlight(player, "剑身受损，难以继续御空。");
                return;
            }
        }

        steerSwordFlight(player);
        spawnFlightParticles(player);
    }

    public static ItemStack getVisibleFlightSword(Player player) {
        if (!player.isFallFlying()) {
            return ItemStack.EMPTY;
        }

        ItemStack mainHand = player.getMainHandItem();
        if (isSwordFlightSword(mainHand)) {
            return mainHand;
        }

        ItemStack offHand = player.getOffhandItem();
        return isSwordFlightSword(offHand) ? offHand : ItemStack.EMPTY;
    }

    private static void steerSwordFlight(ServerPlayer player) {
        Vec3 look = player.getLookAngle();
        Vec3 horizontal = new Vec3(look.x, 0.0, look.z);
        if (horizontal.lengthSqr() > 0.0001) {
            horizontal = horizontal.normalize().scale(0.065);
        }

        Vec3 velocity = player.getDeltaMovement();
        double y = velocity.y;
        if (look.y > 0.05) {
            y = Math.min(0.28, y + look.y * 0.045);
        } else {
            y = Math.max(-0.08, y);
        }

        player.setDeltaMovement(
                (velocity.x + horizontal.x) * 0.98,
                y,
                (velocity.z + horizontal.z) * 0.98
        );
        player.fallDistance = 0.0f;
        player.hasImpulse = true;
    }

    private static void spawnFlightParticles(ServerPlayer player) {
        if (!(player.level() instanceof ServerLevel serverLevel) || player.tickCount % 4 != 0) {
            return;
        }

        serverLevel.sendParticles(ParticleTypes.ENCHANTED_HIT,
                player.getX(), player.getY() + 0.05, player.getZ(),
                3, 0.25, 0.05, 0.25, 0.01);
    }

    private static boolean canSpendDurability(ItemStack stack) {
        return !stack.isDamageableItem() || stack.getDamageValue() < stack.getMaxDamage() - 1;
    }

    private static HandStack findFlightSword(Player player) {
        ItemStack mainHand = player.getMainHandItem();
        if (isSwordFlightSword(mainHand)) {
            return new HandStack(InteractionHand.MAIN_HAND, mainHand);
        }

        ItemStack offHand = player.getOffhandItem();
        if (isSwordFlightSword(offHand)) {
            return new HandStack(InteractionHand.OFF_HAND, offHand);
        }

        return null;
    }

    private static void stopSwordFlight(ServerPlayer player, String message) {
        player.stopFallFlying();
        player.displayClientMessage(Component.literal(message), true);
    }

    private static void syncCultivation(ServerPlayer player, CultivationData data) {
        player.connection.send(new CultivationPayload(
                data.getRealm(),
                data.getQi(),
                data.getSectOrthodoxy(),
                data.getStage(),
                data.getRealmProgress(),
                data.getKarma(),
                data.getSect()
        ));
    }

    private record HandStack(InteractionHand hand, ItemStack stack) {
    }
}
