package com.abyssredemption.daomod.world;

import com.abyssredemption.daomod.AbsDaoMod;
import com.abyssredemption.daomod.network.CultivationPayload;
import com.abyssredemption.daomod.registry.ModAttachments;
import com.abyssredemption.daomod.registry.ModEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = AbsDaoMod.MODID)
public final class DimensionEnvironmentHandler {
    private static final ResourceLocation PRESSURE_SPEED_ID =
            ResourceLocation.fromNamespaceAndPath(AbsDaoMod.MODID, "dimension_pressure_speed");

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide || player.tickCount % 100 != 0) return;
        int gap = pressureGap(player);
        double penalty = gap <= 0 ? 0.0 : gap <= 3 ? -0.10 : gap <= 8 ? -0.20 : -0.35;
        var speed = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speed != null) {
            if (penalty == 0.0) speed.removeModifier(PRESSURE_SPEED_ID);
            else speed.addOrUpdateTransientModifier(new AttributeModifier(
                    PRESSURE_SPEED_ID, penalty, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        }
        if (gap >= 4) {
            player.hurt(player.damageSources().magic(), gap >= 16 ? 4.0f : gap >= 9 ? 2.0f : 1.0f);
        }
        if (gap >= 16) {
            player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 120, 1));
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 120, 1));
            player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 120, 0));
        }
        applyBiomeHazard(player);
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent.Pre event) {
        if (event.getSource().getEntity() instanceof Player player) {
            int gap = pressureGap(player);
            if (gap > 0) {
                float multiplier = gap <= 3 ? 0.9f : gap <= 8 ? 0.8f : 0.65f;
                event.setNewDamage(event.getNewDamage() * multiplier);
            }
        }
    }

    private static int pressureGap(Player player) {
        var data = player.getData(ModAttachments.CULTIVATION);
        return DimensionRules.pressureGap(player.level(), data.getRealm(), data.getStage());
    }

    private static void applyBiomeHazard(Player player) {
        String biome = player.level().getBiome(player.blockPosition()).unwrapKey()
                .map(key -> key.location().getPath()).orElse("");
        switch (biome) {
            case "donghai_leize" -> thunderPressure(player);
            case "nanjiang_huoyu" -> {
                player.igniteForSeconds(4);
                player.addEffect(new MobEffectInstance(ModEffects.DAN_DU, 20 * 8, 0));
            }
            case "beiji_minghai" -> {
                player.setTicksFrozen(Math.min(player.getTicksRequiredToFreeze() + 80, player.getTicksFrozen() + 80));
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 120, 1));
            }
            case "xiji_jinge" -> {
                if (player.getArmorValue() <= 0) player.hurt(player.damageSources().cactus(), 1.0f);
            }
            case "guixu_shenhai", "xukong_tianwai" -> addDaoErosion(player, 1);
            default -> {
                if (player.level().dimension() == DimensionKeys.DILUOYUAN
                        || player.level().dimension() == DimensionKeys.DILUOYUAN_LEGACY) {
                    addDaoErosion(player, 2);
                }
            }
        }
    }

    private static void thunderPressure(Player player) {
        if (!(player.level() instanceof ServerLevel level) || level.random.nextFloat() >= 0.15f) return;
        BlockPos pos = player.blockPosition();
        if (!level.canSeeSky(pos)) return;
        LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(level);
        if (lightning == null) return;
        lightning.setPos(player.getX(), player.getY(), player.getZ());
        if (player instanceof ServerPlayer serverPlayer) lightning.setCause(serverPlayer);
        level.addFreshEntity(lightning);
    }

    private static void addDaoErosion(Player player, int amount) {
        if (!(player instanceof ServerPlayer serverPlayer)) return;
        var data = player.getData(ModAttachments.CULTIVATION);
        data.addKarma(amount);
        player.setData(ModAttachments.CULTIVATION, data);
        serverPlayer.connection.send(new CultivationPayload(
                data.getRealm(), data.getQi(), data.getSectOrthodoxy(), data.getStage(),
                data.getRealmProgress(), data.getKarma(), data.getSect()));
        player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 100, 0));
    }

    private DimensionEnvironmentHandler() {
    }
}
