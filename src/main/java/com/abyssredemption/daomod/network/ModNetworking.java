package com.abyssredemption.daomod.network;

import com.abyssredemption.daomod.AbsDaoMod;
import com.abyssredemption.daomod.attachment.CultivationData;
import com.abyssredemption.daomod.registry.ModAttachments;
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
    }

    // 客户端处理逻辑
    private static void handleCultivationSync(final CultivationPayload payload, final net.neoforged.neoforge.network.handling.IPayloadContext context) {
        context.enqueueWork(() -> {
            net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
            if (mc.player != null) {
                // 将收到的数据转换回你的 CultivationData 类
                CultivationData newData = new CultivationData(
                        payload.realm(),
                        payload.qi(), // 如果你类里是double，记得强转
                        payload.sectOrthodoxy(),
                        payload.stage()
                );
                // 更新客户端 Attachment
                mc.player.setData(ModAttachments.CULTIVATION, newData);
            }
        });
    }
}