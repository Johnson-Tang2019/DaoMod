package com.abyssredemption.daomod.world;

import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;

public final class DimensionTravelHandler {
    public static boolean travelNext(ServerPlayer player) {
        ResourceTarget target = player.level().dimension() == Level.OVERWORLD ? new ResourceTarget(DimensionKeys.LINGXU, 32)
                : isLingxu(player.level()) ? new ResourceTarget(DimensionKeys.DILUOYUAN, 17)
                : isDiluoyuan(player.level()) ? new ResourceTarget(DimensionKeys.XIANYU, 48)
                : new ResourceTarget(Level.OVERWORLD, 21);
        return travelTo(player, target);
    }

    public static boolean travelTo(ServerPlayer player, ResourceKey<Level> targetKey) {
        return travelTo(player, new ResourceTarget(targetKey, targetKey == DimensionKeys.XIANYU ? 48 : 32));
    }

    private static boolean travelTo(ServerPlayer player, ResourceTarget target) {
        ServerLevel level = player.getServer().getLevel(target.key());
        if (level == null) return false;
        int x = player.blockPosition().getX();
        int z = player.blockPosition().getZ();
        int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
        if (target.key() == DimensionKeys.XIANYU && y <= level.getMinBuildHeight() + 2) {
            x = 0;
            z = 0;
            y = Math.max(96, level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z));
        }
        y = Math.max(level.getMinBuildHeight() + 4, Math.min(level.getMaxBuildHeight() - 4, y + 1));
        buildPlatform(level, new BlockPos(x, y - 1, z), target.radius() >= 48 ? 5 : 4);
        boolean moved = player.teleportTo(level, x + 0.5, y, z + 0.5, Set.of(), player.getYRot(), player.getXRot());
        if (!moved) return false;
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20 * 10, 4));
        player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 20 * 10, 0));
        player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.geshi_zhimen",
                Component.translatable("dimension.abyssredemptiondaomod." + target.key().location().getPath())), true);
        return true;
    }

    private static boolean isLingxu(Level level) {
        return level.dimension() == DimensionKeys.LINGXU || level.dimension() == DimensionKeys.LINGXU_LEGACY;
    }

    private static boolean isDiluoyuan(Level level) {
        return level.dimension() == DimensionKeys.DILUOYUAN || level.dimension() == DimensionKeys.DILUOYUAN_LEGACY;
    }

    private static void buildPlatform(ServerLevel level, BlockPos center, int radius) {
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                BlockPos pos = center.offset(dx, 0, dz);
                if (level.getBlockState(pos).isAir() || !level.getBlockState(pos.above()).isAir()) {
                    level.setBlock(pos, Blocks.POLISHED_ANDESITE.defaultBlockState(), 3);
                    level.setBlock(pos.above(), Blocks.AIR.defaultBlockState(), 3);
                    level.setBlock(pos.above(2), Blocks.AIR.defaultBlockState(), 3);
                }
            }
        }
    }

    private record ResourceTarget(ResourceKey<Level> key, int radius) {
    }

    private DimensionTravelHandler() {
    }
}
