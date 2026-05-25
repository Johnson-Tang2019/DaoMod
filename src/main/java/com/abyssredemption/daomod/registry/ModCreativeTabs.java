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
                        // 添加模组所有物品
                        output.accept(ModItems.SOUL_SWORD.get());
                        output.accept(ModItems.DAN.get());
                        output.accept(ModItems.PUTUAN_ITEM.get());
                    })
                    .build());
}
