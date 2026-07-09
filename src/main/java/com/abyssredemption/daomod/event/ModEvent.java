package com.abyssredemption.daomod.event;

import com.abyssredemption.daomod.AbsDaoMod;
import com.abyssredemption.daomod.attachment.CultivationData;
import com.abyssredemption.daomod.item.DaoBladeItem;
import com.abyssredemption.daomod.item.SoulSwordItem;
import com.abyssredemption.daomod.network.CultivationPayload;
import com.abyssredemption.daomod.registry.ModAttachments;
import com.abyssredemption.daomod.registry.ModEffects;
import com.abyssredemption.daomod.registry.ModItems;
import com.abyssredemption.daomod.util.SpiritualAuraHelper;
import com.abyssredemption.daomod.util.SwordFlightHelper;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = AbsDaoMod.MODID)
public class ModEvent {
    private static final ResourceLocation CULTIVATION_HEALTH_ID =
            ResourceLocation.fromNamespaceAndPath(AbsDaoMod.MODID, "cultivation_max_health");
    private static final ResourceLocation CULTIVATION_ATTACK_ID =
            ResourceLocation.fromNamespaceAndPath(AbsDaoMod.MODID, "cultivation_attack_damage");
    private static final ResourceLocation CULTIVATION_ARMOR_ID =
            ResourceLocation.fromNamespaceAndPath(AbsDaoMod.MODID, "cultivation_armor");
    private static final ResourceLocation CULTIVATION_TOUGHNESS_ID =
            ResourceLocation.fromNamespaceAndPath(AbsDaoMod.MODID, "cultivation_armor_toughness");
    private static final ResourceLocation CULTIVATION_KNOCKBACK_ID =
            ResourceLocation.fromNamespaceAndPath(AbsDaoMod.MODID, "cultivation_knockback_resistance");
    private static final ResourceLocation CULTIVATION_SPEED_ID =
            ResourceLocation.fromNamespaceAndPath(AbsDaoMod.MODID, "cultivation_movement_speed");
    private static final ResourceLocation SECT_ATTACK_ID =
            ResourceLocation.fromNamespaceAndPath(AbsDaoMod.MODID, "sect_attack_damage");
    private static final ResourceLocation SECT_ARMOR_ID =
            ResourceLocation.fromNamespaceAndPath(AbsDaoMod.MODID, "sect_armor");
    private static final ResourceLocation SECT_SPEED_ID =
            ResourceLocation.fromNamespaceAndPath(AbsDaoMod.MODID, "sect_movement_speed");
    private static final ResourceLocation SECT_KNOCKBACK_ID =
            ResourceLocation.fromNamespaceAndPath(AbsDaoMod.MODID, "sect_knockback_resistance");
    private static final ResourceLocation SECT_LUCK_ID =
            ResourceLocation.fromNamespaceAndPath(AbsDaoMod.MODID, "sect_luck");

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        syncPlayer(event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        syncPlayer(event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        syncPlayer(event.getEntity());
    }

    private static void syncPlayer(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            syncCultivation(serverPlayer, player.getData(ModAttachments.CULTIVATION));
        }
    }

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            var data = player.getData(ModAttachments.CULTIVATION);
            int stage = data.getStage();
            int realm = data.getRealm();

            if (stage > 0) {
                data.setStage(stage - 1);
                player.displayClientMessage(Component.translatable(
                        "message.abyssredemptiondaomod.death_stage_loss"), true);
            } else if (realm > 0) {
                data.setStage(8);
                data.setRealm(realm - 1);
                player.displayClientMessage(Component.translatable(
                        "message.abyssredemptiondaomod.death_realm_loss"), true);
            } else {
                player.displayClientMessage(Component.translatable(
                        "message.abyssredemptiondaomod.death_minimum"), true);
            }

            data.setQi(0);
            player.setData(ModAttachments.CULTIVATION, data);
            syncCultivation(player, data);
        }
    }

    @SubscribeEvent
    public static void onLivingKilled(LivingDeathEvent event) {
        if (!(event.getSource().getEntity() instanceof ServerPlayer killer)) {
            return;
        }
        if (event.getEntity() instanceof Player) {
            addKarma(killer, 6, true, "message.abyssredemptiondaomod.karma_player");
            return;
        }
        if (!(event.getEntity() instanceof LivingEntity victim)) {
            return;
        }

        dropGuardedResource(victim);

        KarmaResult result = getKarmaForKill(victim);
        addKarma(killer, result.amount(), result.causesNausea(), result.message());
    }

    private static void dropGuardedResource(LivingEntity victim) {
        float health = victim.getMaxHealth();
        String rank;
        float chance;
        if (health >= 150.0f) {
            rank = "ancient";
            chance = 0.25f;
        } else if (health >= 60.0f) {
            rank = "heaven";
            chance = 0.14f;
        } else if (health >= 30.0f) {
            rank = "earth";
            chance = 0.09f;
        } else {
            rank = "mortal";
            chance = 0.045f;
        }
        if (victim.getRandom().nextFloat() >= chance) {
            return;
        }

        String category = victim.getRandom().nextBoolean() ? "herb" : "ore";
        var pool = ModItems.getCodexPool(category, rank);
        if (pool.isEmpty()) {
            return;
        }
        var selected = pool.get(victim.getRandom().nextInt(pool.size()));
        victim.spawnAtLocation(new ItemStack(selected.get()));
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent.Pre event) {
        if (!(event.getSource().getEntity() instanceof Player attacker)) {
            return;
        }

        var stack = attacker.getMainHandItem();
        if (stack.getItem() instanceof SoulSwordItem sword && sword.isAboveRealm(attacker)) {
            event.setNewDamage(event.getNewDamage() * 0.4f);
            if (attacker instanceof ServerPlayer serverPlayer && serverPlayer.tickCount % 40 == 0) {
                serverPlayer.displayClientMessage(Component.translatable(
                        "message.abyssredemptiondaomod.weapon_realm_low",
                        Component.translatable("gui.daomod.realm" + sword.getSwordRealm())), true);
            }
        } else if (stack.getItem() instanceof DaoBladeItem blade && blade.isAboveRealm(attacker)) {
            event.setNewDamage(event.getNewDamage() * 0.4f);
            if (attacker instanceof ServerPlayer serverPlayer && serverPlayer.tickCount % 40 == 0) {
                serverPlayer.displayClientMessage(Component.translatable(
                        "message.abyssredemptiondaomod.weapon_realm_low",
                        Component.translatable("gui.daomod.realm" + blade.getBladeRealm())), true);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity().level().isClientSide) {
            return;
        }

        Player player = event.getEntity();
        updateCultivationHealth(player);
        recoverQi(player);
        handleMeditation(player);
        if (player instanceof ServerPlayer serverPlayer) {
            returnFromImmortalVoid(serverPlayer);
            SwordFlightHelper.tickSwordFlight(serverPlayer);
        }
    }

    private static void returnFromImmortalVoid(ServerPlayer player) {
        if (player.getY() >= player.level().getMinBuildHeight() - 16
                || !player.level().dimension().location().getPath().equals("eternal_immortal_realm")) {
            return;
        }
        ServerLevel overworld = player.getServer().overworld();
        int x = player.blockPosition().getX();
        int z = player.blockPosition().getZ();
        int y = overworld.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z) + 16;
        if (player.teleportTo(overworld, x + 0.5, y, z + 0.5, java.util.Set.of(), player.getYRot(), 0.0f)) {
            player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 20 * 12, 0));
            player.displayClientMessage(Component.translatable(
                    "message.abyssredemptiondaomod.immortal_void_return"), true);
        }
    }

    private static void updateCultivationHealth(Player player) {
        if (player.tickCount % 20 != 0) {
            return;
        }
        var attribute = player.getAttribute(Attributes.MAX_HEALTH);
        if (attribute == null) {
            return;
        }

        var data = player.getData(ModAttachments.CULTIVATION);
        double bonusHealth = data.getRealm() * 10.0 + data.getStage();
        var currentModifier = attribute.getModifier(CULTIVATION_HEALTH_ID);
        if (currentModifier == null || Math.abs(currentModifier.amount() - bonusHealth) >= 0.001) {
            float oldMaximum = player.getMaxHealth();
            float healthRatio = oldMaximum > 0.0f ? player.getHealth() / oldMaximum : 1.0f;
            if (bonusHealth <= 0.0) {
                attribute.removeModifier(CULTIVATION_HEALTH_ID);
            } else {
                attribute.addOrUpdateTransientModifier(new AttributeModifier(
                        CULTIVATION_HEALTH_ID, bonusHealth, AttributeModifier.Operation.ADD_VALUE));
            }
            float newMaximum = player.getMaxHealth();
            if (player.getHealth() > 0.0f) {
                player.setHealth(Math.max(1.0f, Math.min(newMaximum, newMaximum * healthRatio)));
            }
        }

        double cultivationLevel = data.getRealm() + data.getStage() / 9.0;
        updateModifier(player, Attributes.ATTACK_DAMAGE, CULTIVATION_ATTACK_ID,
                cultivationLevel * 1.5, AttributeModifier.Operation.ADD_VALUE);
        updateModifier(player, Attributes.ARMOR, CULTIVATION_ARMOR_ID,
                cultivationLevel * 1.25, AttributeModifier.Operation.ADD_VALUE);
        updateModifier(player, Attributes.ARMOR_TOUGHNESS, CULTIVATION_TOUGHNESS_ID,
                cultivationLevel * 0.75, AttributeModifier.Operation.ADD_VALUE);
        updateModifier(player, Attributes.KNOCKBACK_RESISTANCE, CULTIVATION_KNOCKBACK_ID,
                Math.min(0.5, cultivationLevel * 0.05), AttributeModifier.Operation.ADD_VALUE);
        updateModifier(player, Attributes.MOVEMENT_SPEED, CULTIVATION_SPEED_ID,
                cultivationLevel * 0.015, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);

        int sect = data.getSect();
        updateModifier(player, Attributes.ATTACK_DAMAGE, SECT_ATTACK_ID,
                sect == 1 ? 3.0 : sect == 4 ? 5.0 : 0.0, AttributeModifier.Operation.ADD_VALUE);
        updateModifier(player, Attributes.ARMOR, SECT_ARMOR_ID,
                sect == 3 ? 5.0 : 0.0, AttributeModifier.Operation.ADD_VALUE);
        updateModifier(player, Attributes.MOVEMENT_SPEED, SECT_SPEED_ID,
                sect == 1 ? 0.05 : sect == 4 ? 0.08 : 0.0, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        updateModifier(player, Attributes.KNOCKBACK_RESISTANCE, SECT_KNOCKBACK_ID,
                sect == 3 ? 0.2 : 0.0, AttributeModifier.Operation.ADD_VALUE);
        updateModifier(player, Attributes.LUCK, SECT_LUCK_ID,
                sect == 2 ? 3.0 : 0.0, AttributeModifier.Operation.ADD_VALUE);
    }

    private static void updateModifier(Player player, net.minecraft.core.Holder<net.minecraft.world.entity.ai.attributes.Attribute> attribute,
                                       ResourceLocation id, double amount, AttributeModifier.Operation operation) {
        var instance = player.getAttribute(attribute);
        if (instance == null) {
            return;
        }
        var current = instance.getModifier(id);
        if (amount <= 0.0) {
            if (current != null) instance.removeModifier(id);
            return;
        }
        if (current == null || Math.abs(current.amount() - amount) >= 0.001 || current.operation() != operation) {
            instance.addOrUpdateTransientModifier(new AttributeModifier(id, amount, operation));
        }
    }

    private static void recoverQi(Player player) {
        if (player.tickCount % 100 != 0) {
            return;
        }

        var data = player.getData(ModAttachments.CULTIVATION);
        long currentQi = data.getQi();
        long maxQi = CultivationData.getMaxQi(data.getRealm(), data.getStage());
        if (currentQi >= maxQi) {
            return;
        }

        float recoveryRate = data.getSect() == 2 ? 0.15f : 0.1f;
        int recoveryAmount = (int) (recoveryRate * maxQi);
        long nextQi = Math.min(currentQi + recoveryAmount, maxQi);
        data.setQi(nextQi);
        player.setData(ModAttachments.CULTIVATION, data);

        if (player instanceof ServerPlayer serverPlayer) {
            syncCultivation(serverPlayer, data);
        }
    }

    private static void handleMeditation(Player player) {
        Level level = player.level();
        if (player.tickCount % 100 != 0) {
            return;
        }

        if (!(player.getVehicle() instanceof ArmorStand seat) || !seat.getTags().contains("daomod_seat")) {
            return;
        }

        if (player.hasEffect(ModEffects.DAN_DU_FAN_SHI)) {
            player.displayClientMessage(Component.translatable(
                    "message.abyssredemptiondaomod.meditation_blocked"), true);
            return;
        }

        int auraConcentration = SpiritualAuraHelper.getConcentration(player);
        int progressGain = SpiritualAuraHelper.getMeditationProgressGain(player);
        increaseRealmProgress(player, progressGain);
        player.displayClientMessage(Component.translatable(
                "message.abyssredemptiondaomod.meditation_progress", auraConcentration, progressGain), true);

        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.ENCHANTED_HIT,
                    player.getX(), player.getY() + 1.5, player.getZ(),
                    8 + auraConcentration, 0.5, 0.5, 0.5, 0.01);
        }
    }

    public static void increaseRealmProgress(Player player, int amount) {
        var data = player.getData(ModAttachments.CULTIVATION);
        int oldRealm = data.getRealm();
        int oldStage = data.getStage();
        data.setRealmProgress(data.getRealmProgress() + amount);
        player.setData(ModAttachments.CULTIVATION, data);

        if (data.getRealm() != oldRealm || data.getStage() != oldStage) {
            announceBreakthrough(player, oldRealm, data);
        }

        if (player instanceof ServerPlayer serverPlayer) {
            syncCultivation(serverPlayer, data);
        }
    }

    private static void announceBreakthrough(Player player, int oldRealm, CultivationData data) {
        boolean majorBreakthrough = data.getRealm() != oldRealm;
        if (majorBreakthrough) {
            player.setHealth(player.getMaxHealth());
            data.setQi(CultivationData.getMaxQi(data.getRealm(), data.getStage()));
            player.displayClientMessage(Component.translatable("message.daomod.major_breakthrough",
                    Component.translatable("gui.daomod.realm" + data.getRealm())), false);
            applyTribulation(player, data);
        } else {
            player.heal(Math.max(4.0f, player.getMaxHealth() * 0.25f));
            player.displayClientMessage(Component.translatable("message.daomod.stage_breakthrough",
                    data.getStage() + 1), true);
        }

        if (player.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(majorBreakthrough ? ParticleTypes.TOTEM_OF_UNDYING : ParticleTypes.ENCHANT,
                    player.getX(), player.getY() + 1.0, player.getZ(),
                    majorBreakthrough ? 80 : 30, 0.8, 1.0, 0.8, 0.08);
            serverLevel.sendParticles(ParticleTypes.END_ROD,
                    player.getX(), player.getY() + 1.0, player.getZ(),
                    majorBreakthrough ? 36 : 12, 0.5, 0.8, 0.5, 0.03);
            serverLevel.playSound(null, player.blockPosition(),
                    majorBreakthrough ? SoundEvents.LIGHTNING_BOLT_THUNDER : SoundEvents.PLAYER_LEVELUP,
                    SoundSource.PLAYERS, majorBreakthrough ? 1.2f : 0.7f, majorBreakthrough ? 0.8f : 1.2f);
        }
    }

    private static void applyTribulation(Player player, CultivationData data) {
        if (!(player instanceof ServerPlayer serverPlayer) || !(player.level() instanceof ServerLevel serverLevel)) {
            return;
        }
        int strikes = tribulationStrikes(data.getRealm(), data.getKarma());
        serverPlayer.displayClientMessage(Component.translatable("message.daomod.tribulation", strikes), false);
        for (int i = 0; i < strikes; i++) {
            LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(serverLevel);
            if (lightning != null) {
                double x = player.getX() + (i == 0 ? 0 : serverLevel.random.nextInt(7) - 3);
                double z = player.getZ() + (i == 0 ? 0 : serverLevel.random.nextInt(7) - 3);
                lightning.setPos(x, player.getY(), z);
                lightning.setCause(serverPlayer);
                serverLevel.addFreshEntity(lightning);
            }
        }
        if (data.getKarma() >= 20) {
            int amplifier = heartDemonAmplifier(data.getKarma());
            player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 240 + amplifier * 80, 0));
            player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 300 + amplifier * 100, 0));
            player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 300 + amplifier * 100, amplifier));
            serverPlayer.displayClientMessage(Component.translatable("message.daomod.heart_demon",
                    data.getKarma()), false);
        }
    }

    public static int tribulationStrikes(int realm, int karma) {
        return 1 + Math.max(0, realm) / 2 + Math.min(4, Math.max(0, karma) / 25);
    }

    public static int heartDemonAmplifier(int karma) {
        return Math.min(3, Math.max(0, karma) / 50);
    }

    private static void syncCultivation(ServerPlayer player, CultivationData data) {
        player.connection.send(new CultivationPayload(
                data.getRealm(),
                data.getQi(),
                data.getSectOrthodoxy(),
                data.getStage(),
                data.getRealmProgress(),
                data.getKarma(),
                data.getSect()
        ));
    }

    private static KarmaResult getKarmaForKill(LivingEntity victim) {
        if (isFriendly(victim)) {
            return new KarmaResult(5, true, "message.abyssredemptiondaomod.karma_friendly");
        }
        if (victim instanceof NeutralMob) {
            return new KarmaResult(3, true, "message.abyssredemptiondaomod.karma_neutral");
        }
        return new KarmaResult(1, false, "message.abyssredemptiondaomod.karma_hostile");
    }

    private static boolean isFriendly(LivingEntity entity) {
        MobCategory category = entity.getType().getCategory();
        return entity instanceof Animal
                || entity instanceof TamableAnimal
                || entity instanceof AbstractVillager
                || entity instanceof AbstractGolem
                || category == MobCategory.CREATURE
                || category == MobCategory.AMBIENT
                || category == MobCategory.WATER_CREATURE
                || category == MobCategory.WATER_AMBIENT
                || category == MobCategory.UNDERGROUND_WATER_CREATURE
                || category == MobCategory.AXOLOTLS;
    }

    private static void addKarma(ServerPlayer player, int amount, boolean causesNausea, String messageKey) {
        var data = player.getData(ModAttachments.CULTIVATION);
        data.addKarma(amount);
        player.setData(ModAttachments.CULTIVATION, data);
        syncCultivation(player, data);

        if (causesNausea) {
            player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0, false, true, true));
        }
        player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.karma_added",
                Component.translatable(messageKey), amount, data.getKarma()), true);
    }

    private record KarmaResult(int amount, boolean causesNausea, String message) {
    }
}
