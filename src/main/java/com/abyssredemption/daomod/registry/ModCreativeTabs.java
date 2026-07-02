package com.abyssredemption.daomod.registry;

import com.abyssredemption.daomod.AbsDaoMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AbsDaoMod.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> DAO_TAB = CREATIVE_MODE_TABS.register("dao_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("creativetab.abyssredemptiondaomod.dao_tab"))
                    .icon(() -> new ItemStack(ModItems.DAN.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.SOUL_SWORD.get());
                        output.accept(ModItems.WENLING_DAGGER.get());
                        output.accept(ModItems.ZHANXIE_GUXING.get());
                        output.accept(ModItems.CHUNJUN.get());
                        output.accept(ModItems.JUQUE.get());
                        output.accept(ModItems.XUANYUAN_XIAYU.get());
                        output.accept(ModItems.YINGSHA_BI.get());
                        output.accept(ModItems.JIANQIUYUE.get());
                        output.accept(ModItems.ZHANSHEN_CANREN.get());
                        output.accept(ModItems.ZHANYINGUO_TIANDAO.get());
                        output.accept(ModItems.BISHUI_AMULET.get());
                        output.accept(ModItems.RANXIN_RING.get());
                        output.accept(ModItems.FUYAO_FAN.get());
                        output.accept(ModItems.DAN.get());
                        output.accept(ModItems.PUTUAN_ITEM.get());
                        output.accept(ModItems.LINGZHI_PLANT_ITEM.get());
                        output.accept(ModItems.LINGSHI_ORE_ITEM.get());
                        output.accept(ModItems.CULTIVATION_GUIDE_BOOK.get());
                        output.accept(ModItems.SWORD_LEGENDS_CHALLENGE.get());
                        output.accept(ModItems.CREATION_MASTERS_CHALLENGE.get());
                        output.accept(ModItems.BUDDHA_DEMON_CHALLENGE.get());
                        output.accept(ModItems.FORBIDDEN_ONES_CHALLENGE.get());
                        output.accept(ModItems.ANCIENT_BLOODLINE_CHALLENGE.get());
                        ModItems.CODEX_ITEMS.forEach(item -> output.accept(item.get()));
                        output.accept(ModItems.SWORD_ATTENDANT_SPAWN_EGG.get());
                        output.accept(ModItems.CREATION_DISCIPLE_SPAWN_EGG.get());
                        output.accept(ModItems.THUNDER_MONK_SPAWN_EGG.get());
                        output.accept(ModItems.RAKSHASA_CULTIST_SPAWN_EGG.get());
                        output.accept(ModItems.XUANYUAN_SPAWN_EGG.get());
                        output.accept(ModItems.DUGU_QIUBAI_SPAWN_EGG.get());
                        output.accept(ModItems.QINGLIAN_JIANXIAN_SPAWN_EGG.get());
                        output.accept(ModItems.YE_GUCHENG_SPAWN_EGG.get());
                        output.accept(ModItems.SHEN_DUZHOU_SPAWN_EGG.get());
                        output.accept(ModItems.NAMELESS_ARTIFICER_SPAWN_EGG.get());
                        output.accept(ModItems.GONGSHU_BAN_SPAWN_EGG.get());
                        output.accept(ModItems.GE_HONG_SPAWN_EGG.get());
                        output.accept(ModItems.OUYE_ZI_SPAWN_EGG.get());
                        output.accept(ModItems.SU_HONGYI_SPAWN_EGG.get());
                        output.accept(ModItems.MING_WANG_SPAWN_EGG.get());
                        output.accept(ModItems.DIZANG_SPAWN_EGG.get());
                        output.accept(ModItems.DAMO_SPAWN_EGG.get());
                        output.accept(ModItems.BLOOD_RIVER_ANCESTOR_SPAWN_EGG.get());
                        output.accept(ModItems.WUHUA_SPAWN_EGG.get());
                        output.accept(ModItems.MO_XUAN_SPAWN_EGG.get());
                        output.accept(ModItems.GUANGHAN_FAIRY_SPAWN_EGG.get());
                        output.accept(ModItems.FENGSHEN_DAOIST_SPAWN_EGG.get());
                        output.accept(ModItems.GRAVE_KEEPER_SPAWN_EGG.get());
                        output.accept(ModItems.NING_FAN_SPAWN_EGG.get());
                        output.accept(ModItems.KUAFU_SPAWN_EGG.get());
                        output.accept(ModItems.JINGWEI_SPAWN_EGG.get());
                        output.accept(ModItems.HOUYI_SPAWN_EGG.get());
                        output.accept(ModItems.XINGTIAN_SPAWN_EGG.get());
                        output.accept(ModItems.XUANYUAN_FOURTEEN_SPAWN_EGG.get());
                    })
                    .build());
}
