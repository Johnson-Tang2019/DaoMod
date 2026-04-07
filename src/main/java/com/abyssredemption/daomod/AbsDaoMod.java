package com.abyssredemption.daomod;

import com.abyssredemption.daomod.registry.ModAttachments;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;


@Mod(AbsDaoMod.MODID)
public class AbsDaoMod {
    public static final String MODID = "abyssredemptiondaomod";

    public AbsDaoMod(IEventBus modEventBus, ModContainer modContainer) {
        ModAttachments.ATTACHMENT_TYPES.register(modEventBus);


    }


}
