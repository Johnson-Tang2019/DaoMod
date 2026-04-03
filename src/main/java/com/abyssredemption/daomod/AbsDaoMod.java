package com.abyssredemption.daomod;

import com.abyssredemption.daomod.network.CultivationPayload;
import com.abyssredemption.daomod.registry.ModAttachments;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;


@Mod(AbsDaoMod.MODID)
public class AbsDaoMod {
    public static final String MODID = "abyssredemptiondaomod";

    public AbsDaoMod(IEventBus modEventBus, ModContainer modContainer) {
        ModAttachments.ATTACHMENT_TYPES.register(modEventBus);

        modEventBus.addListener(this::registerNetworking);
    }

    private void registerNetworking(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1.0.0");
        registrar.playToClient(
                CultivationPayload.TYPE,
                CultivationPayload.STREAM_CODEC,
                (payload, context) -> {
                    context.enqueueWork(() -> {
                        context.player().setData(ModAttachments.CULTIVATION,
                                new com.abyssredemption.daomod.attachment.CultivationData(payload.realm(), payload.qi()));
                    });
                }
        );
    }
}
