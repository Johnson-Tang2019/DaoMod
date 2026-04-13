package com.abyssredemption.daomod.registry;

import com.abyssredemption.daomod.AbsDaoMod;
import com.abyssredemption.daomod.item.SoulSwordItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    // 1. 创建注册器
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(AbsDaoMod.MODID);

    // 2. 注册你的灵剑
    public static final DeferredItem<SoulSwordItem> SOUL_SWORD =
            ITEMS.register("soul_sword",
            () -> new SoulSwordItem(Tiers.DIAMOND, new Item.Properties()
                    .attributes(SoulSwordItem.createAttributes(Tiers.DIAMOND, 3, -2.4f))
                    .stacksTo(1))); // 设置不可堆叠

    public static final DeferredItem<Item> PUTUAN_ITEM =
            ITEMS.registerSimpleBlockItem("putuan", ModBlocks.PUTUAN);

}