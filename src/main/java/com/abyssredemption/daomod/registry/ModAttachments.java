package com.abyssredemption.daomod.registry;

import com.abyssredemption.daomod.AbsDaoMod;
import com.abyssredemption.daomod.data.CultivationData;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.attachment.AttachmentType;

public class ModAttachments {

    public static final AttachmentType<CultivationData> CULTIVATION =
            AttachmentType.builder(() -> new CultivationData())
                    .serialize(CultivationData::serializeNBT, CultivationData::deserializeNBT)
                    .buildAndRegister(new ResourceLocation("xiu_mod", "cultivation"));

}
