package com.abyssredemption.daomod.worldgen.boss;

import com.abyssredemption.daomod.registry.ModBlocks;
import com.abyssredemption.daomod.registry.ModItems;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class BossStructureFeature extends Feature<BossStructureConfiguration> {
    public BossStructureFeature(Codec<BossStructureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<BossStructureConfiguration> context) {
        WorldGenLevel level = context.level();
        BossStructureConfiguration config = context.config();
        BlockPos center = context.origin().below();
        Palette palette = palette(config.variant());
        if (config.clearArea()) {
            clear(level, center, config.radius());
        }
        circle(level, center, config.radius(), palette.floor());
        ring(level, center, config.radius() - 2, config.radius(), palette.wall());
        for (int x : new int[]{-config.radius() + 3, config.radius() - 3}) {
            for (int z : new int[]{-config.radius() + 3, config.radius() - 3}) {
                pillar(level, center.offset(x, 1, z), 7 + config.variant(), palette.pillar());
            }
        }
        decorate(level, center, config.variant(), palette);
        set(level, center.offset(0, 1, 0), ModBlocks.BOSS_SEAL.get().defaultBlockState());
        placeTreasure(level, center.offset(0, 1, -config.radius() + 4), context.random(), config.variant());
        return true;
    }

    private static void clear(WorldGenLevel level, BlockPos center, int radius) {
        for (int x = -radius; x <= radius; x++) {
            for (int y = 1; y <= 8; y++) {
                for (int z = -radius; z <= radius; z++) {
                    if (x * x + z * z <= radius * radius) {
                        set(level, center.offset(x, y, z), Blocks.AIR.defaultBlockState());
                    }
                }
            }
        }
    }

    private static void circle(WorldGenLevel level, BlockPos center, int radius, BlockState state) {
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                if (x * x + z * z <= radius * radius) {
                    set(level, center.offset(x, 0, z), state);
                    set(level, center.offset(x, -1, z), state);
                }
            }
        }
    }

    private static void ring(WorldGenLevel level, BlockPos center, int inner, int outer, BlockState state) {
        int inner2 = inner * inner;
        int outer2 = outer * outer;
        for (int x = -outer; x <= outer; x++) {
            for (int z = -outer; z <= outer; z++) {
                int d = x * x + z * z;
                if (d >= inner2 && d <= outer2) {
                    set(level, center.offset(x, 1, z), state);
                }
            }
        }
    }

    private static void pillar(WorldGenLevel level, BlockPos base, int height, BlockState state) {
        for (int y = 0; y < height; y++) {
            set(level, base.above(y), state);
        }
        set(level, base.above(height), Blocks.END_ROD.defaultBlockState());
    }

    private static void decorate(WorldGenLevel level, BlockPos center, int variant, Palette palette) {
        int span = 6 + variant;
        for (int i = -span; i <= span; i++) {
            set(level, center.offset(i, 1, 0), palette.trim());
            set(level, center.offset(0, 1, i), palette.trim());
        }
        set(level, center.offset(0, 2, -4), palette.focus());
        set(level, center.offset(0, 3, -4), Blocks.LIGHTNING_ROD.defaultBlockState());
    }

    private static void placeTreasure(WorldGenLevel level, BlockPos pos, RandomSource random, int variant) {
        set(level, pos, Blocks.BARREL.defaultBlockState());
        if (!(level.getBlockEntity(pos) instanceof Container container)) return;
        String rank = variant <= 3 ? "heaven" : "ancient";
        putRandom(container, 0, ModItems.getCodexPool("artifact", rank), random);
        putRandom(container, 1, ModItems.getCodexPool("herb", rank), random);
        putRandom(container, 2, ModItems.getCodexPool("ore", rank), random);
        container.setChanged();
    }

    private static void putRandom(Container container, int slot,
                                  java.util.List<? extends net.neoforged.neoforge.registries.DeferredItem<?>> pool,
                                  RandomSource random) {
        if (!pool.isEmpty()) {
            container.setItem(slot, new ItemStack(pool.get(random.nextInt(pool.size())).get()));
        }
    }

    private static Palette palette(int variant) {
        return switch (variant) {
            case 2 -> new Palette(Blocks.DARK_PRISMARINE, Blocks.CUT_COPPER, Blocks.BONE_BLOCK,
                    Blocks.SEA_LANTERN, Blocks.LIGHTNING_ROD);
            case 3 -> new Palette(Blocks.DEEPSLATE_BRICKS, Blocks.IRON_BLOCK, Blocks.POLISHED_BLACKSTONE,
                    Blocks.ANVIL, Blocks.LODESTONE);
            case 4 -> new Palette(Blocks.NETHER_BRICKS, Blocks.MAGMA_BLOCK, Blocks.CRIMSON_STEM,
                    Blocks.GOLD_BLOCK, Blocks.CAMPFIRE);
            case 5 -> new Palette(Blocks.PACKED_ICE, Blocks.BLUE_ICE, Blocks.DARK_PRISMARINE,
                    Blocks.SOUL_LANTERN, Blocks.RESPAWN_ANCHOR);
            case 6 -> new Palette(Blocks.PRISMARINE, Blocks.CRYING_OBSIDIAN, Blocks.WARPED_STEM,
                    Blocks.SCULK, Blocks.ENDER_CHEST);
            case 7 -> new Palette(Blocks.END_STONE_BRICKS, Blocks.OBSIDIAN, Blocks.PURPUR_PILLAR,
                    Blocks.END_ROD, Blocks.END_PORTAL_FRAME);
            default -> new Palette(Blocks.POLISHED_DEEPSLATE, Blocks.CHISELED_DEEPSLATE, Blocks.DEEPSLATE_BRICKS,
                    Blocks.GILDED_BLACKSTONE, Blocks.HEAVY_CORE);
        };
    }

    private static void set(WorldGenLevel level, BlockPos pos, BlockState state) {
        level.setBlock(pos, state, Block.UPDATE_CLIENTS);
    }

    private record Palette(BlockState floor, BlockState wall, BlockState pillar, BlockState trim, BlockState focus) {
        private Palette(Block floor, Block wall, Block pillar, Block trim, Block focus) {
            this(floor.defaultBlockState(), wall.defaultBlockState(), pillar.defaultBlockState(),
                    trim.defaultBlockState(), focus.defaultBlockState());
        }
    }
}
