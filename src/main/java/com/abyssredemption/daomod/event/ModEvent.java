package com.abyssredemption.daomod.event;

import com.abyssredemption.daomod.AbsDaoMod;
import com.abyssredemption.daomod.attachment.CultivationData;
import com.abyssredemption.daomod.network.CultivationPayload;
import com.abyssredemption.daomod.registry.ModAttachments;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = AbsDaoMod.MODID)
public class ModEvent {

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity().level().isClientSide) return;

        var player = event.getEntity();

        if (player.tickCount % 100 == 0) {
            var data = player.getData(ModAttachments.CULTIVATION);
            long currentQi = data.getQi();
            long maxQi = CultivationData.getMaxQi(data.getRealm(), data.getStage());
            if (currentQi < maxQi) {
                int recoveryAmount = (int) (0.1f * maxQi);
                long nextQi = Math.min(currentQi + recoveryAmount, maxQi);
                data.setQi(nextQi);
                System.out.println("recover qi! qi:" + data.getQi());
                player.setData(ModAttachments.CULTIVATION, data);
                if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                    serverPlayer.connection.send(new CultivationPayload(
                            data.getRealm(),
                            data.getQi(),
                            data.getSectOrthodoxy(),
                            data.getStage()
                    ));
                }
            }
        }
    }
}
