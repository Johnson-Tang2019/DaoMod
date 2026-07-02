package com.abyssredemption.daomod.registry;

import com.abyssredemption.daomod.AbsDaoMod;
import com.abyssredemption.daomod.block.PuTuan;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    // 1. 必须先定义这个 BLOCKS 变量！
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(AbsDaoMod.MODID);

    // 2. 之后才能使用 BLOCKS.register
    public static final DeferredBlock<Block> PUTUAN = BLOCKS.register("putuan",
            () -> new PuTuan(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_YELLOW)
                    .strength(0.5f)
                    .sound(SoundType.WOOL)
                    .noOcclusion()));

    public static final DeferredBlock<Block> LINGZHI_PLANT = BLOCKS.register("lingzhi_plant",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PLANT)
                    .strength(0.1f)
                    .sound(SoundType.GRASS)
                    .lightLevel(state -> 3)
                    .noCollission()
                    .noOcclusion()));

    public static final DeferredBlock<Block> LINGSHI_ORE = BLOCKS.register("lingshi_ore",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(4.0f, 5.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE)
                    .lightLevel(state -> 2)));
}
