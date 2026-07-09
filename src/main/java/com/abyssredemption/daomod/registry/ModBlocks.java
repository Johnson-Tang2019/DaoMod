package com.abyssredemption.daomod.registry;

import com.abyssredemption.daomod.AbsDaoMod;
import com.abyssredemption.daomod.block.PuTuan;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    // 1. 必须先定义这个 BLOCKS 变量！
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(AbsDaoMod.MODID);
    public static final Map<String, DeferredBlock<Block>> CODEX_RESOURCE_BLOCKS = new HashMap<>();

    private static final String[] HERBS = {
            "juling_cao", "huoxue_teng", "yingguang_tai", "ningqi_hua", "bihan_cao", "shexian_guo",
            "tiegu_hua", "zuixian_tao", "bihuo_shenzao", "qianji_yin", "jianyi_cao", "tunling_teng",
            "qiqiao_linglong_guo", "kurong_shen", "jihan_binglian", "niepan_hua", "minghe_duya_hua",
            "yinyang_nizhuan_cao", "wudao_cha", "zaohua_qinglian", "sansheng_shishang_hua", "hundun_qingti"
    };
    private static final String[] ORES = {
            "chenyin_jingtie", "hantie_kuang", "chitong_shi", "lingci_sha", "gengjin_shenkuang",
            "lihuo_chijin", "hantan_yintie", "lihuo_sha", "chenyuan_zhongshi", "daoling_yu",
            "kongming_shi", "wuse_shenshi", "xingchen_yuntie", "xukong_jing", "zhanshen_xuanjin",
            "longxue_shi", "leijie_jin", "butian_wuse_shi", "suiyue_sha", "hunyuan_zushi"
    };

    static {
        for (String id : HERBS) {
            CODEX_RESOURCE_BLOCKS.put(id, BLOCKS.register(id, () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PLANT).strength(0.1f).sound(SoundType.GRASS).noCollission().noOcclusion())));
        }
        for (String id : ORES) {
            CODEX_RESOURCE_BLOCKS.put(id, BLOCKS.register(id, () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE).strength(4.0f, 5.0f).requiresCorrectToolForDrops().sound(SoundType.STONE))));
        }
    }

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
