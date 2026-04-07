package com.abyssredemption.daomod.registry;

import com.abyssredemption.daomod.attachment.CultivationData;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModAttachments {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, "daomod");

    public static final Supplier<AttachmentType<CultivationData>> CULTIVATION =
            ATTACHMENT_TYPES.register("cultivation", () -> AttachmentType.builder(() ->
                            new CultivationData(0, 0, 0, 0)
                    )
                    .serialize(CultivationData.CODEC)
                    .copyOnDeath()
                    .build());
}
