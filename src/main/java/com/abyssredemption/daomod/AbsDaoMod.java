package com.abyssredemption.daomod;

import com.abyssredemption.daomod.registry.ModAttachments;
import com.abyssredemption.daomod.registry.ModBlocks;
import com.abyssredemption.daomod.registry.ModEntities;
import com.abyssredemption.daomod.registry.ModItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;


@Mod(AbsDaoMod.MODID)
public class AbsDaoMod {
    public static final String MODID = "abyssredemptiondaomod";

    public AbsDaoMod(IEventBus modEventBus, ModContainer modContainer) {

        ModEntities.ENTITIES.register(modEventBus);
        ModAttachments.ATTACHMENT_TYPES.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);

    }


}
