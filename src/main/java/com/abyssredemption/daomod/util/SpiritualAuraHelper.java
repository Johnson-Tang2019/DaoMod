package com.abyssredemption.daomod.util;

import com.abyssredemption.daomod.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public final class SpiritualAuraHelper {
    private static final int SCAN_RADIUS = 8;
    private static final int MAX_CONCENTRATION = 30;

    private SpiritualAuraHelper() {
    }

    public static int getConcentration(Player player) {
        Level level = player.level();
        BlockPos center = player.blockPosition();
        int concentration = 0;

        for (BlockPos pos : BlockPos.betweenClosed(center.offset(-SCAN_RADIUS, -SCAN_RADIUS, -SCAN_RADIUS),
                center.offset(SCAN_RADIUS, SCAN_RADIUS, SCAN_RADIUS))) {
            var state = level.getBlockState(pos);
            int sourceStrength = getAuraSourceStrength(state);

            if (sourceStrength > 0) {
                int distance = Math.max(1, (int) Math.ceil(Math.sqrt(pos.distSqr(center))));
                concentration += Math.max(1, sourceStrength - distance / 3);
            }
        }

        return Math.min(concentration, MAX_CONCENTRATION);
    }

    public static int getMeditationProgressGain(Player player) {
        int concentration = getConcentration(player);
        return 1 + Math.min(5, concentration / 6);
    }

    public static boolean isAuraSource(BlockState state) {
        return getAuraSourceStrength(state) > 0;
    }

    public static int getAuraSourceStrength(BlockState state) {
        if (state.is(ModBlocks.LINGZHI_PLANT.get())) {
            return 3;
        }
        if (state.is(ModBlocks.LINGSHI_ORE.get())) {
            return 5;
        }
        // ponytail: vanilla ruin cores double as nexus blocks; add a custom block if provenance matters.
        if (state.is(Blocks.BEACON) || state.is(Blocks.END_PORTAL_FRAME) || state.is(Blocks.HEAVY_CORE)) {
            return 12;
        }
        if (state.is(Blocks.LODESTONE) || state.is(Blocks.RESPAWN_ANCHOR)) {
            return 10;
        }
        return 0;
    }
}
