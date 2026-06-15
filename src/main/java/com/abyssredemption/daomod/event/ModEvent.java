package com.abyssredemption.daomod.event;

import com.abyssredemption.daomod.AbsDaoMod;
import com.abyssredemption.daomod.attachment.CultivationData;
import com.abyssredemption.daomod.network.CultivationPayload;
import com.abyssredemption.daomod.registry.ModAttachments;
import com.abyssredemption.daomod.registry.ModEffects;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = AbsDaoMod.MODID)
public class ModEvent {

    // 玩家死亡：下降一个小境界
    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof net.minecraft.server.level.ServerPlayer player) {
            var data = player.getData(ModAttachments.CULTIVATION);
            int stage = data.getStage();
            int realm = data.getRealm();

            if (stage > 0) {
                data.setStage(stage - 1);
                player.displayClientMessage(Component.literal("§c道体受损，境界跌落！"), true);
            } else if (realm > 0) {
                data.setStage(8);
                data.setRealm(realm - 1);
                player.displayClientMessage(Component.literal("§4§l⚠ 大道崩殂！大境界跌落！"), true);
            } else {
                player.displayClientMessage(Component.literal("§7修为已至谷底，无法再降..."), true);
            }

            data.setQi(0);
            player.setData(ModAttachments.CULTIVATION, data);

            player.connection.send(new CultivationPayload(
                    data.getRealm(),
                    data.getQi(),
                    data.getSectOrthodoxy(),
                    data.getStage(),
                    data.getRealmProgress()
            ));
        }
    }

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
                System.out.println( "recover qi! qi:" + data.getQi());
                player.setData(ModAttachments.CULTIVATION, data);
                if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                    serverPlayer.connection.send(new CultivationPayload(
                            data.getRealm(),
                            data.getQi(),
                            data.getSectOrthodoxy(),
                            data.getStage(),
                            data.getRealmProgress()
                    ));
                }
            }
        }
    }

    @EventBusSubscriber(modid = AbsDaoMod.MODID)
    public class SeatHandler {

        // 核心：打坐增加进度
        @SubscribeEvent
        public static void onPlayerTick(PlayerTickEvent.Post event) {
            Player player = event.getEntity();
            Level level = player.level();

            // 仅在服务器端计算，每 5 秒（100 Ticks）触发一次
            if (!level.isClientSide && player.tickCount % 100 == 0) {

                // 检查玩家是否正骑在蒲团座位上
                if (player.getVehicle() instanceof ArmorStand seat && seat.getTags().contains("daomod_seat")) {

                    // 检查是否有丹毒反噬，有则无法修炼
                    if (player.hasEffect(ModEffects.DAN_DU_FAN_SHI)) {
                        player.displayClientMessage(Component.literal("§c经脉封闭，无法修炼！"), true);
                        return;
                    }

                    // 1. 增加境界进度 (这里替换为你自己的数据处理逻辑)
                    increaseRealmProgress(player, 1.0f);
                    // 2. 发送提示信息（可选）
                    player.displayClientMessage(Component.literal("§b灵气入体，境界有所感悟..."), true);

                    // 3. 播放修仙粒子效果（在玩家头顶产生灵气感）
                    if (level instanceof ServerLevel serverLevel) {
                        serverLevel.sendParticles(ParticleTypes.ENCHANTED_HIT,
                                player.getX(), player.getY() + 1.5, player.getZ(),
                                10, 0.5, 0.5, 0.5, 0.01);
                    }
                }
            }
        }

        private static void increaseRealmProgress(Player player, float amount) {
            var data = player.getData(ModAttachments.CULTIVATION);
            data.setRealmProgress(data.getRealmProgress() + 1);
            player.setData(ModAttachments.CULTIVATION, data);
            System.out.println("玩家 " + player.getName().getString() + " 正在打坐，境界进度 + " + amount + "，当前进度：" + data.getRealmProgress());

            player.setData(ModAttachments.CULTIVATION, data);
            if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                serverPlayer.connection.send(new CultivationPayload(
                        data.getRealm(),
                        data.getQi(),
                        data.getSectOrthodoxy(),
                        data.getStage(),
                        data.getRealmProgress()
                ));
            }
        }
    }
}
