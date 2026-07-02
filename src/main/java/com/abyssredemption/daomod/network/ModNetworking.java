package com.abyssredemption.daomod.network;

import com.abyssredemption.daomod.AbsDaoMod;
import com.abyssredemption.daomod.client.ClientPayloadHandlers;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = AbsDaoMod.MODID)
public class ModNetworking {

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        // 设置协议版本
        final PayloadRegistrar registrar = event.registrar("1");

        // 注册从服务端发送到客户端的包 (PlayToClient)
        registrar.playToClient(
                CultivationPayload.TYPE,
                CultivationPayload.STREAM_CODEC,
                ModNetworking::handleCultivationSync
        );

        registrar.playToClient(
                OpenGuideBookPayload.TYPE,
                OpenGuideBookPayload.STREAM_CODEC,
                ModNetworking::handleOpenGuideBook
        );
    }

    // 客户端处理逻辑
    private static void handleCultivationSync(final CultivationPayload payload, final net.neoforged.neoforge.network.handling.IPayloadContext context) {
        ClientPayloadHandlers.handleCultivationSync(payload, context);
    }

    private static void handleOpenGuideBook(final OpenGuideBookPayload payload, final net.neoforged.neoforge.network.handling.IPayloadContext context) {
        ClientPayloadHandlers.handleOpenGuideBook(payload, context);
    }
}
