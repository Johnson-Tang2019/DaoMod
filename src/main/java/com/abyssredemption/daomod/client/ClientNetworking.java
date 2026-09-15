package com.abyssredemption.daomod.client;

import com.abyssredemption.daomod.AbsDaoMod;
import com.abyssredemption.daomod.network.CultivationPayload;
import com.abyssredemption.daomod.network.OpenGuideBookPayload;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@EventBusSubscriber(modid = AbsDaoMod.MODID, value = Dist.CLIENT)
public final class ClientNetworking {
    private ClientNetworking() {
    }

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar("1");
        registrar.playToClient(CultivationPayload.TYPE, CultivationPayload.STREAM_CODEC,
                ClientPayloadHandlers::handleCultivationSync);
        registrar.playToClient(OpenGuideBookPayload.TYPE, OpenGuideBookPayload.STREAM_CODEC,
                ClientPayloadHandlers::handleOpenGuideBook);
    }
}
