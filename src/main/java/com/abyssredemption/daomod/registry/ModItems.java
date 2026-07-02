package com.abyssredemption.daomod.registry;

import com.abyssredemption.daomod.AbsDaoMod;
import com.abyssredemption.daomod.item.BishuiAmuletItem;
import com.abyssredemption.daomod.item.CodexItem;
import com.abyssredemption.daomod.item.CultivationGuideBookItem;
import com.abyssredemption.daomod.item.DanItem;
import com.abyssredemption.daomod.item.DaoBladeItem;
import com.abyssredemption.daomod.item.FuyaoFanItem;
import com.abyssredemption.daomod.item.LegendaryChallengeTalismanItem;
import com.abyssredemption.daomod.item.RanxinRingItem;
import com.abyssredemption.daomod.item.SoulSwordItem;
import com.abyssredemption.daomod.item.WenlingDaggerItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.Tiers;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    // 1. 创建注册器
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(AbsDaoMod.MODID);
    public static final List<DeferredItem<? extends Item>> CODEX_ITEMS = new ArrayList<>();
    public static final Map<String, List<DeferredItem<? extends Item>>> CODEX_ITEM_POOLS = new HashMap<>();

    private record CodexEntry(String id, String category, String rank) {}

    private static final CodexEntry[] CODEX_ENTRIES = {
            // 法器（凡阶）
            new CodexEntry("bishui_xichanzhu", "artifact", "mortal"),
            new CodexEntry("zhiyuan_feixingnu", "artifact", "mortal"),
            new CodexEntry("naying_ping", "artifact", "mortal"),
            new CodexEntry("ganshan_bian", "artifact", "mortal"),
            new CodexEntry("cangjing_ye", "artifact", "mortal"),
            new CodexEntry("jieling_suo", "artifact", "mortal"),
            new CodexEntry("zhaodan_jing", "artifact", "mortal"),
            // 法宝（地阶）
            new CodexEntry("shanhe_cuo", "artifact", "earth"),
            new CodexEntry("lihuo_fentian_ling", "artifact", "earth"),
            new CodexEntry("fuyu_fanyun_gai", "artifact", "earth"),
            new CodexEntry("fulong_jinsuo", "artifact", "earth"),
            new CodexEntry("chuanxiao_suo", "artifact", "earth"),
            new CodexEntry("zhenmo_yin", "artifact", "earth"),
            new CodexEntry("juling_wanqi_zhen", "artifact", "earth"),
            new CodexEntry("yinyang_liangjie_bei", "artifact", "earth"),
            // 灵宝（天阶）
            new CodexEntry("wending_laoxiu", "artifact", "heaven"),
            new CodexEntry("fentian_zhulong", "artifact", "heaven"),
            new CodexEntry("jiushi_cihang", "artifact", "heaven"),
            new CodexEntry("zhaohun_qianmian", "artifact", "heaven"),
            new CodexEntry("duntian_wuxing", "artifact", "heaven"),
            new CodexEntry("xiangmo_numu", "artifact", "heaven"),
            new CodexEntry("nongchao_yiyin", "artifact", "heaven"),
            new CodexEntry("lietian_bengshan", "artifact", "heaven"),
            new CodexEntry("suanjin_shensuan", "artifact", "heaven"),
            // 通天灵宝（古阶）
            new CodexEntry("wuxing_dingshi_ta", "artifact", "ancient"),
            new CodexEntry("suihan_kurong_zhong", "artifact", "ancient"),
            new CodexEntry("zhutian_xingxiu_tu", "artifact", "ancient"),
            new CodexEntry("hunyuan_qiankun_san", "artifact", "ancient"),
            new CodexEntry("fantian_fudi_yin", "artifact", "ancient"),
            new CodexEntry("qibao_qingjing_shu", "artifact", "ancient"),
            new CodexEntry("yinyang_chongsu_lu", "artifact", "ancient"),
            new CodexEntry("miedu_honghulu", "artifact", "ancient"),
            new CodexEntry("guixu_dinghai_zhu", "artifact", "ancient"),
            // 至尊神兵/因果器（神阶）
            new CodexEntry("suiyue_liuzhuan_lun", "artifact", "divine"),
            new CodexEntry("wanfa_benyuan_shengjuan", "artifact", "divine"),
            new CodexEntry("lunhui_pan", "artifact", "divine"),
            new CodexEntry("geshi_zhimen", "artifact", "divine"),
            new CodexEntry("hundun_zhenjie_zhong", "artifact", "divine"),
            new CodexEntry("zhongsheng_yinguo_jitan", "artifact", "divine"),
            new CodexEntry("xukong_dingxing_mao", "artifact", "divine"),
            new CodexEntry("tiandao_caijue_chui", "artifact", "divine"),
            new CodexEntry("hongmeng_chuangshi_lian", "artifact", "divine"),
            // 灵植
            new CodexEntry("juling_cao", "herb", "mortal"), new CodexEntry("huoxue_teng", "herb", "mortal"),
            new CodexEntry("yingguang_tai", "herb", "mortal"), new CodexEntry("ningqi_hua", "herb", "mortal"),
            new CodexEntry("bihan_cao", "herb", "mortal"), new CodexEntry("shexian_guo", "herb", "mortal"),
            new CodexEntry("tiegu_hua", "herb", "earth"), new CodexEntry("zuixian_tao", "herb", "earth"),
            new CodexEntry("bihuo_shenzao", "herb", "earth"), new CodexEntry("qianji_yin", "herb", "earth"),
            new CodexEntry("jianyi_cao", "herb", "earth"), new CodexEntry("tunling_teng", "herb", "earth"),
            new CodexEntry("qiqiao_linglong_guo", "herb", "heaven"), new CodexEntry("kurong_shen", "herb", "heaven"),
            new CodexEntry("jihan_binglian", "herb", "heaven"), new CodexEntry("niepan_hua", "herb", "heaven"),
            new CodexEntry("minghe_duya_hua", "herb", "heaven"), new CodexEntry("yinyang_nizhuan_cao", "herb", "heaven"),
            new CodexEntry("wudao_cha", "herb", "ancient"), new CodexEntry("zaohua_qinglian", "herb", "ancient"),
            new CodexEntry("sansheng_shishang_hua", "herb", "ancient"), new CodexEntry("hundun_qingti", "herb", "ancient"),
            // 矿材
            new CodexEntry("chenyin_jingtie", "ore", "mortal"), new CodexEntry("hantie_kuang", "ore", "mortal"),
            new CodexEntry("chitong_shi", "ore", "mortal"), new CodexEntry("lingci_sha", "ore", "mortal"),
            new CodexEntry("gengjin_shenkuang", "ore", "earth"), new CodexEntry("lihuo_chijin", "ore", "earth"),
            new CodexEntry("hantan_yintie", "ore", "earth"), new CodexEntry("lihuo_sha", "ore", "earth"),
            new CodexEntry("chenyuan_zhongshi", "ore", "earth"), new CodexEntry("daoling_yu", "ore", "earth"),
            new CodexEntry("kongming_shi", "ore", "heaven"), new CodexEntry("wuse_shenshi", "ore", "heaven"),
            new CodexEntry("xingchen_yuntie", "ore", "heaven"), new CodexEntry("xukong_jing", "ore", "heaven"),
            new CodexEntry("zhanshen_xuanjin", "ore", "heaven"), new CodexEntry("longxue_shi", "ore", "heaven"),
            new CodexEntry("leijie_jin", "ore", "ancient"), new CodexEntry("butian_wuse_shi", "ore", "ancient"),
            new CodexEntry("suiyue_sha", "ore", "ancient"), new CodexEntry("hunyuan_zushi", "ore", "ancient")
    };

    static {
        for (CodexEntry entry : CODEX_ENTRIES) {
            DeferredItem<? extends Item> item = ITEMS.register(entry.id(),
                    () -> new CodexItem(propertiesFor(entry),
                            "category.abyssredemptiondaomod." + entry.category(),
                            "rank.abyssredemptiondaomod." + entry.rank(), entry.id()));
            CODEX_ITEMS.add(item);
            CODEX_ITEM_POOLS.computeIfAbsent(entry.category() + ":" + entry.rank(), ignored -> new ArrayList<>())
                    .add(item);
        }
    }

    public static List<DeferredItem<? extends Item>> getCodexPool(String category, String rank) {
        return CODEX_ITEM_POOLS.getOrDefault(category + ":" + rank, List.of());
    }

    private static Item.Properties propertiesFor(CodexEntry entry) {
        Item.Properties properties = new Item.Properties()
                .stacksTo(entry.category().equals("artifact") ? 1 : 64)
                .rarity(switch (entry.rank()) {
                    case "earth" -> Rarity.UNCOMMON;
                    case "heaven" -> Rarity.RARE;
                    case "ancient", "divine" -> Rarity.EPIC;
                    default -> Rarity.COMMON;
                });
        if (entry.rank().equals("ancient") || entry.rank().equals("divine")) {
            properties.fireResistant();
        }
        return properties;
    }

    // 2. 注册你的灵剑
    public static final DeferredItem<SoulSwordItem> SOUL_SWORD =
            registerRankedSword("soul_sword", 2);

    public static final DeferredItem<SoulSwordItem> ZHANXIE_GUXING =
            registerRankedSword("zhanxie_guxing", 3);

    public static final DeferredItem<SoulSwordItem> XUANYUAN_XIAYU =
            registerRankedSword("xuanyuan_xiayu", 8);

    public static final DeferredItem<SoulSwordItem> CHUNJUN =
            registerRankedSword("chunjun", 5);

    public static final DeferredItem<SoulSwordItem> JUQUE =
            registerRankedSword("juque", 5);

    public static final DeferredItem<DaoBladeItem> YINGSHA_BI =
            registerRankedBlade("yingsha_bi", 2);

    public static final DeferredItem<DaoBladeItem> JIANQIUYUE =
            registerRankedBlade("jianqiuyue", 2);

    public static final DeferredItem<DaoBladeItem> ZHANSHEN_CANREN =
            registerRankedBlade("zhanshen_canren", 7);

    public static final DeferredItem<DaoBladeItem> ZHANYINGUO_TIANDAO =
            registerRankedBlade("zhanyinguo_tiandao", 8);

    public static final DeferredItem<WenlingDaggerItem> WENLING_DAGGER =
            ITEMS.register("wenling_dagger",
            () -> new WenlingDaggerItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<BishuiAmuletItem> BISHUI_AMULET =
            ITEMS.register("bishui_amulet",
            () -> new BishuiAmuletItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<RanxinRingItem> RANXIN_RING =
            ITEMS.register("ranxin_ring",
            () -> new RanxinRingItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<FuyaoFanItem> FUYAO_FAN =
            ITEMS.register("fuyao_fan",
            () -> new FuyaoFanItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<BlockItem> PUTUAN_ITEM =
            ITEMS.registerSimpleBlockItem("putuan", ModBlocks.PUTUAN);

    public static final DeferredItem<BlockItem> LINGZHI_PLANT_ITEM =
            ITEMS.registerSimpleBlockItem("lingzhi_plant", ModBlocks.LINGZHI_PLANT);

    public static final DeferredItem<BlockItem> LINGSHI_ORE_ITEM =
            ITEMS.registerSimpleBlockItem("lingshi_ore", ModBlocks.LINGSHI_ORE);

    // 3. 注册丹药
    public static final DeferredItem<DanItem> DAN =
            ITEMS.register("dan",
            () -> new DanItem(new Item.Properties().stacksTo(64)));

    // 4. 注册修炼指南书
    public static final DeferredItem<CultivationGuideBookItem> CULTIVATION_GUIDE_BOOK =
            ITEMS.register("cultivation_guide_book",
            () -> new CultivationGuideBookItem(new Item.Properties()));
    public static final DeferredItem<LegendaryChallengeTalismanItem> SWORD_LEGENDS_CHALLENGE = challengeTalisman("sword_legends_challenge", 0, 4);
    public static final DeferredItem<LegendaryChallengeTalismanItem> CREATION_MASTERS_CHALLENGE = challengeTalisman("creation_masters_challenge", 1, 4);
    public static final DeferredItem<LegendaryChallengeTalismanItem> BUDDHA_DEMON_CHALLENGE = challengeTalisman("buddha_demon_challenge", 2, 5);
    public static final DeferredItem<LegendaryChallengeTalismanItem> FORBIDDEN_ONES_CHALLENGE = challengeTalisman("forbidden_ones_challenge", 3, 6);
    public static final DeferredItem<LegendaryChallengeTalismanItem> ANCIENT_BLOODLINE_CHALLENGE = challengeTalisman("ancient_bloodline_challenge", 4, 7);

    public static final DeferredItem<SpawnEggItem> SWORD_ATTENDANT_SPAWN_EGG = ITEMS.register("sword_attendant_spawn_egg",
            () -> new SpawnEggItem(ModEntities.SWORD_ATTENDANT.get(), 0xE7EEF7, 0x4C78A8, new Item.Properties()));
    public static final DeferredItem<SpawnEggItem> CREATION_DISCIPLE_SPAWN_EGG = ITEMS.register("creation_disciple_spawn_egg",
            () -> new SpawnEggItem(ModEntities.CREATION_DISCIPLE.get(), 0xD8C49A, 0x4E8D72, new Item.Properties()));
    public static final DeferredItem<SpawnEggItem> THUNDER_MONK_SPAWN_EGG = ITEMS.register("thunder_monk_spawn_egg",
            () -> new SpawnEggItem(ModEntities.THUNDER_MONK.get(), 0xC99032, 0x7B2D25, new Item.Properties()));
    public static final DeferredItem<SpawnEggItem> RAKSHASA_CULTIST_SPAWN_EGG = ITEMS.register("rakshasa_cultist_spawn_egg",
            () -> new SpawnEggItem(ModEntities.RAKSHASA_CULTIST.get(), 0x31243C, 0xA32637, new Item.Properties()));
    public static final DeferredItem<SpawnEggItem> XUANYUAN_SPAWN_EGG = legendaryEgg("xuanyuan", ModEntities.XUANYUAN, 0xD5B55B, 0x5D351F);
    public static final DeferredItem<SpawnEggItem> DUGU_QIUBAI_SPAWN_EGG = legendaryEgg("dugu_qiubai", ModEntities.DUGU_QIUBAI, 0xE7E7E7, 0x292733);
    public static final DeferredItem<SpawnEggItem> QINGLIAN_JIANXIAN_SPAWN_EGG = legendaryEgg("qinglian_jianxian", ModEntities.QINGLIAN_JIANXIAN, 0x5EAF83, 0xE8F2D1);
    public static final DeferredItem<SpawnEggItem> YE_GUCHENG_SPAWN_EGG = legendaryEgg("ye_gucheng", ModEntities.YE_GUCHENG, 0xDDEDFC, 0x6688A7);
    public static final DeferredItem<SpawnEggItem> SHEN_DUZHOU_SPAWN_EGG = legendaryEgg("shen_duzhou", ModEntities.SHEN_DUZHOU, 0x344A68, 0xAFD7F2);
    public static final DeferredItem<SpawnEggItem> NAMELESS_ARTIFICER_SPAWN_EGG = legendaryEgg("nameless_artificer", ModEntities.NAMELESS_ARTIFICER, 0x6B5D49, 0xD6B86B);
    public static final DeferredItem<SpawnEggItem> GONGSHU_BAN_SPAWN_EGG = legendaryEgg("gongshu_ban", ModEntities.GONGSHU_BAN, 0x3B4852, 0x71D4C5);
    public static final DeferredItem<SpawnEggItem> GE_HONG_SPAWN_EGG = legendaryEgg("ge_hong", ModEntities.GE_HONG, 0xEEE0B0, 0xB64B36);
    public static final DeferredItem<SpawnEggItem> OUYE_ZI_SPAWN_EGG = legendaryEgg("ouye_zi", ModEntities.OUYE_ZI, 0x4B2523, 0xF0903D);
    public static final DeferredItem<SpawnEggItem> SU_HONGYI_SPAWN_EGG = legendaryEgg("su_hongyi", ModEntities.SU_HONGYI, 0xA92E45, 0xE6C66D);
    public static final DeferredItem<SpawnEggItem> MING_WANG_SPAWN_EGG = legendaryEgg("ming_wang", ModEntities.MING_WANG, 0x17151F, 0x6A52A2);
    public static final DeferredItem<SpawnEggItem> DIZANG_SPAWN_EGG = legendaryEgg("dizang", ModEntities.DIZANG, 0x7B3D25, 0xD8B45B);
    public static final DeferredItem<SpawnEggItem> DAMO_SPAWN_EGG = legendaryEgg("damo", ModEntities.DAMO, 0x9C4D2D, 0xE4B45C);
    public static final DeferredItem<SpawnEggItem> BLOOD_RIVER_ANCESTOR_SPAWN_EGG = legendaryEgg("blood_river_ancestor", ModEntities.BLOOD_RIVER_ANCESTOR, 0x6E101E, 0xD3313E);
    public static final DeferredItem<SpawnEggItem> WUHUA_SPAWN_EGG = legendaryEgg("wuhua", ModEntities.WUHUA, 0xEEE7DD, 0xB8203A);
    public static final DeferredItem<SpawnEggItem> MO_XUAN_SPAWN_EGG = legendaryEgg("mo_xuan", ModEntities.MO_XUAN, 0x181821, 0x7560A0);
    public static final DeferredItem<SpawnEggItem> GUANGHAN_FAIRY_SPAWN_EGG = legendaryEgg("guanghan_fairy", ModEntities.GUANGHAN_FAIRY, 0xE5F3F5, 0x87B9D1);
    public static final DeferredItem<SpawnEggItem> FENGSHEN_DAOIST_SPAWN_EGG = legendaryEgg("fengshen_daoist", ModEntities.FENGSHEN_DAOIST, 0xD7C48A, 0x5F685E);
    public static final DeferredItem<SpawnEggItem> GRAVE_KEEPER_SPAWN_EGG = legendaryEgg("grave_keeper", ModEntities.GRAVE_KEEPER, 0x4C4A43, 0x7B9A88);
    public static final DeferredItem<SpawnEggItem> NING_FAN_SPAWN_EGG = legendaryEgg("ning_fan", ModEntities.NING_FAN, 0x313541, 0x9B4555);
    public static final DeferredItem<SpawnEggItem> KUAFU_SPAWN_EGG = legendaryEgg("kuafu", ModEntities.KUAFU, 0xA85A2B, 0xF1B94F);
    public static final DeferredItem<SpawnEggItem> JINGWEI_SPAWN_EGG = legendaryEgg("jingwei", ModEntities.JINGWEI, 0x4B799A, 0xE65A4E);
    public static final DeferredItem<SpawnEggItem> HOUYI_SPAWN_EGG = legendaryEgg("houyi", ModEntities.HOUYI, 0x5B4A34, 0x9DD7E0);
    public static final DeferredItem<SpawnEggItem> XINGTIAN_SPAWN_EGG = legendaryEgg("xingtian", ModEntities.XINGTIAN, 0x642B26, 0xC7803C);
    public static final DeferredItem<SpawnEggItem> XUANYUAN_FOURTEEN_SPAWN_EGG = legendaryEgg("xuanyuan_fourteen", ModEntities.XUANYUAN_FOURTEEN, 0x3D4147, 0x817A6B);

    private static DeferredItem<SpawnEggItem> legendaryEgg(String name,
            net.neoforged.neoforge.registries.DeferredHolder<net.minecraft.world.entity.EntityType<?>,
                    net.minecraft.world.entity.EntityType<com.abyssredemption.daomod.entity.LegendaryCultivatorEntity>> type,
            int primary, int secondary) {
        return ITEMS.register(name + "_spawn_egg",
                () -> new SpawnEggItem(type.get(), primary, secondary, new Item.Properties().rarity(Rarity.EPIC)));
    }

    private static DeferredItem<LegendaryChallengeTalismanItem> challengeTalisman(String name, int group, int realm) {
        return ITEMS.register(name, () -> new LegendaryChallengeTalismanItem(
                new Item.Properties().rarity(Rarity.EPIC).fireResistant(), group, realm));
    }

    private static DeferredItem<SoulSwordItem> registerRankedSword(String name, int swordRealm) {
        return ITEMS.register(name,
                () -> new SoulSwordItem(Tiers.DIAMOND, new Item.Properties()
                        .attributes(SoulSwordItem.createAttributes(Tiers.DIAMOND, 3, -2.4f))
                        .stacksTo(1), swordRealm));
    }

    private static DeferredItem<DaoBladeItem> registerRankedBlade(String name, int bladeRealm) {
        return ITEMS.register(name,
                () -> new DaoBladeItem(Tiers.DIAMOND, new Item.Properties()
                        .attributes(DaoBladeItem.createAttributes(Tiers.DIAMOND, 6, -2.7f))
                        .stacksTo(1), bladeRealm));
    }
}
