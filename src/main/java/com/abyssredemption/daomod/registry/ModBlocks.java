package com.abyssredemption.daomod.registry;

import com.abyssredemption.daomod.AbsDaoMod;
import com.abyssredemption.daomod.block.PuTuan;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    // 1. 必须先定义这个 BLOCKS 变量！
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(AbsDaoMod.MODID);

    // 2. 之后才能使用 BLOCKS.register
    public static final DeferredBlock<Block> PUTUAN = BLOCKS.register("putuan",
            () -> new PuTuan(BlockBehaviour.Properties.of()
                    .strength(0.5f)
                    .sound(SoundType.WOOL)
                    .noOcclusion()));
}