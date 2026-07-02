package com.abyssredemption.daomod.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class SectOutpostFeature extends Feature<NoneFeatureConfiguration> {
    private final int sect;

    public SectOutpostFeature(Codec<NoneFeatureConfiguration> codec, int sect) {
        super(codec);
        this.sect = sect;
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos center = context.origin().below();
        Palette palette = palette();

        // Foundation and buried footings keep the pavilion stable on uneven terrain.
        for (int x = -4; x <= 4; x++) {
            for (int z = -4; z <= 4; z++) {
                set(level, center.offset(x, 0, z), palette.floor);
                if (Math.abs(x) == 4 || Math.abs(z) == 4) {
                    set(level, center.offset(x, -1, z), palette.foundation);
                }
            }
        }

        // Four columns, railings and an open entrance on the south side.
        for (int x : new int[]{-3, 3}) {
            for (int z : new int[]{-3, 3}) {
                for (int y = 1; y <= 5; y++) {
                    set(level, center.offset(x, y, z), palette.pillar);
                }
            }
        }
        for (int i = -2; i <= 2; i++) {
            set(level, center.offset(i, 1, -4), palette.railing);
            set(level, center.offset(-4, 1, i), palette.railing);
            set(level, center.offset(4, 1, i), palette.railing);
            if (Math.abs(i) > 1) set(level, center.offset(i, 1, 4), palette.railing);
        }

        // Broad lower eave and a raised central roof create a readable Chinese pavilion silhouette.
        for (int x = -5; x <= 5; x++) {
            for (int z = -5; z <= 5; z++) {
                if (Math.abs(x) == 5 || Math.abs(z) == 5 || (Math.abs(x) <= 4 && Math.abs(z) <= 4)) {
                    set(level, center.offset(x, 6, z), palette.roof);
                }
            }
        }
        for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
                set(level, center.offset(x, 7, z), palette.upperRoof);
            }
        }
        set(level, center.offset(0, 8, 0), palette.finial);

        decorate(level, center);
        return true;
    }

    private void decorate(WorldGenLevel level, BlockPos center) {
        switch (sect) {
            case 1 -> {
                set(level, center.offset(0, 1, 0), Blocks.ANVIL.defaultBlockState());
                set(level, center.offset(-1, 1, 0), Blocks.END_ROD.defaultBlockState());
                set(level, center.offset(1, 1, 0), Blocks.END_ROD.defaultBlockState());
                set(level, center.offset(0, 1, -2), Blocks.GRINDSTONE.defaultBlockState());
            }
            case 2 -> {
                set(level, center.offset(0, 1, 0), Blocks.BLAST_FURNACE.defaultBlockState());
                set(level, center.offset(-2, 1, 0), Blocks.CRAFTING_TABLE.defaultBlockState());
                set(level, center.offset(2, 1, 0), Blocks.CAULDRON.defaultBlockState());
                set(level, center.offset(0, 1, -2), Blocks.COPPER_BLOCK.defaultBlockState());
            }
            case 3 -> {
                set(level, center.offset(0, 1, 0), Blocks.BELL.defaultBlockState());
                set(level, center.offset(-2, 1, 0), Blocks.CHISELED_SANDSTONE.defaultBlockState());
                set(level, center.offset(2, 1, 0), Blocks.CHISELED_SANDSTONE.defaultBlockState());
                set(level, center.offset(0, 1, -2), Blocks.GOLD_BLOCK.defaultBlockState());
            }
            case 4 -> {
                set(level, center.offset(0, 1, 0), Blocks.ENCHANTING_TABLE.defaultBlockState());
                set(level, center.offset(-2, 1, 0), Blocks.CRYING_OBSIDIAN.defaultBlockState());
                set(level, center.offset(2, 1, 0), Blocks.CRYING_OBSIDIAN.defaultBlockState());
                set(level, center.offset(0, 1, -2), Blocks.SOUL_LANTERN.defaultBlockState());
            }
            case 5 -> {
                set(level, center.offset(0, 1, 0), Blocks.LODESTONE.defaultBlockState());
                set(level, center.offset(-2, 1, 0), Blocks.GRINDSTONE.defaultBlockState());
                set(level, center.offset(2, 1, 0), Blocks.GRINDSTONE.defaultBlockState());
                set(level, center.offset(0, 1, -2), Blocks.ANVIL.defaultBlockState());
            }
            case 6 -> {
                set(level, center.offset(0, 1, 0), Blocks.BEACON.defaultBlockState());
                set(level, center.offset(-2, 1, 0), Blocks.COPPER_BLOCK.defaultBlockState());
                set(level, center.offset(2, 1, 0), Blocks.WAXED_EXPOSED_COPPER.defaultBlockState());
                set(level, center.offset(0, 1, -2), Blocks.BLAST_FURNACE.defaultBlockState());
            }
            case 7 -> {
                set(level, center.offset(0, 1, 0), Blocks.RESPAWN_ANCHOR.defaultBlockState());
                set(level, center.offset(-2, 1, 0), Blocks.SOUL_SAND.defaultBlockState());
                set(level, center.offset(2, 1, 0), Blocks.SOUL_SOIL.defaultBlockState());
                set(level, center.offset(0, 1, -2), Blocks.CRYING_OBSIDIAN.defaultBlockState());
            }
            case 8 -> {
                set(level, center.offset(0, 1, 0), Blocks.END_PORTAL_FRAME.defaultBlockState());
                set(level, center.offset(-2, 1, 0), Blocks.SCULK.defaultBlockState());
                set(level, center.offset(2, 1, 0), Blocks.SCULK.defaultBlockState());
                set(level, center.offset(0, 1, -2), Blocks.ENDER_CHEST.defaultBlockState());
            }
            case 9 -> {
                set(level, center.offset(0, 1, 0), Blocks.CHISELED_DEEPSLATE.defaultBlockState());
                set(level, center.offset(-2, 1, 0), Blocks.GILDED_BLACKSTONE.defaultBlockState());
                set(level, center.offset(2, 1, 0), Blocks.GILDED_BLACKSTONE.defaultBlockState());
                set(level, center.offset(0, 1, -2), Blocks.HEAVY_CORE.defaultBlockState());
            }
            default -> { }
        }
        set(level, center.offset(-3, 5, -3), Blocks.LANTERN.defaultBlockState());
        set(level, center.offset(3, 5, -3), Blocks.LANTERN.defaultBlockState());
        set(level, center.offset(-3, 5, 3), Blocks.LANTERN.defaultBlockState());
        set(level, center.offset(3, 5, 3), Blocks.LANTERN.defaultBlockState());
    }

    private Palette palette() {
        return switch (sect) {
            case 1 -> new Palette(Blocks.STONE_BRICKS, Blocks.POLISHED_ANDESITE, Blocks.STRIPPED_SPRUCE_LOG,
                    Blocks.SPRUCE_FENCE, Blocks.DEEPSLATE_TILE_SLAB, Blocks.POLISHED_DEEPSLATE, Blocks.END_ROD);
            case 2 -> new Palette(Blocks.BRICKS, Blocks.CUT_COPPER, Blocks.STRIPPED_DARK_OAK_LOG,
                    Blocks.DARK_OAK_FENCE, Blocks.WAXED_WEATHERED_CUT_COPPER_SLAB, Blocks.DARK_PRISMARINE, Blocks.LIGHTNING_ROD);
            case 3 -> new Palette(Blocks.SMOOTH_SANDSTONE, Blocks.CUT_SANDSTONE, Blocks.STRIPPED_ACACIA_LOG,
                    Blocks.ACACIA_FENCE, Blocks.RED_NETHER_BRICK_SLAB, Blocks.GOLD_BLOCK, Blocks.LIGHTNING_ROD);
            case 4 -> new Palette(Blocks.POLISHED_BLACKSTONE_BRICKS, Blocks.CRYING_OBSIDIAN, Blocks.STRIPPED_CRIMSON_STEM,
                    Blocks.NETHER_BRICK_FENCE, Blocks.BLACKSTONE_SLAB, Blocks.OBSIDIAN, Blocks.SOUL_LANTERN);
            case 5 -> new Palette(Blocks.DEEPSLATE_BRICKS, Blocks.POLISHED_DEEPSLATE, Blocks.STRIPPED_SPRUCE_LOG,
                    Blocks.IRON_BARS, Blocks.DEEPSLATE_TILE_SLAB, Blocks.CHISELED_DEEPSLATE, Blocks.LIGHTNING_ROD);
            case 6 -> new Palette(Blocks.CUT_COPPER, Blocks.WAXED_EXPOSED_COPPER, Blocks.STRIPPED_DARK_OAK_LOG,
                    Blocks.COPPER_GRATE, Blocks.WAXED_WEATHERED_CUT_COPPER_SLAB, Blocks.DARK_PRISMARINE, Blocks.LIGHTNING_ROD);
            case 7 -> new Palette(Blocks.POLISHED_BLACKSTONE_BRICKS, Blocks.SOUL_SOIL, Blocks.STRIPPED_WARPED_STEM,
                    Blocks.POLISHED_BLACKSTONE_BRICK_WALL, Blocks.BLACKSTONE_SLAB, Blocks.CRYING_OBSIDIAN, Blocks.SOUL_LANTERN);
            case 8 -> new Palette(Blocks.END_STONE_BRICKS, Blocks.OBSIDIAN, Blocks.PURPUR_PILLAR,
                    Blocks.IRON_BARS, Blocks.PURPUR_SLAB, Blocks.CRYING_OBSIDIAN, Blocks.END_ROD);
            case 9 -> new Palette(Blocks.MUD_BRICKS, Blocks.PACKED_MUD, Blocks.STRIPPED_MANGROVE_LOG,
                    Blocks.MUD_BRICK_WALL, Blocks.POLISHED_BLACKSTONE_SLAB, Blocks.CHISELED_DEEPSLATE, Blocks.LIGHTNING_ROD);
            default -> throw new IllegalStateException("Unknown sect " + sect);
        };
    }

    private static void set(WorldGenLevel level, BlockPos pos, BlockState state) {
        level.setBlock(pos, state, Block.UPDATE_CLIENTS);
    }

    private record Palette(BlockState floor, BlockState foundation, BlockState pillar, BlockState railing,
                           BlockState roof, BlockState upperRoof, BlockState finial) {
        private Palette(Block floor, Block foundation, Block pillar, Block railing, Block roof, Block upperRoof, Block finial) {
            this(floor.defaultBlockState(), foundation.defaultBlockState(), pillar.defaultBlockState(),
                    railing.defaultBlockState(), roof.defaultBlockState(), upperRoof.defaultBlockState(), finial.defaultBlockState());
        }
    }
}
