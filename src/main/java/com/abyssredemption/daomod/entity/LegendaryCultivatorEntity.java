package com.abyssredemption.daomod.entity;

import com.abyssredemption.daomod.event.ModEvent;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class LegendaryCultivatorEntity extends Monster {
    private final int legend;
    private final ServerBossEvent bossEvent;
    private final Set<UUID> contributors = new HashSet<>();

    public LegendaryCultivatorEntity(EntityType<? extends Monster> type, Level level, int legend) {
        super(type, level);
        this.legend = legend;
        this.xpReward = 120;
        this.bossEvent = new ServerBossEvent(getDisplayName(), BossEvent.BossBarColor.PURPLE,
                BossEvent.BossBarOverlay.PROGRESS);
    }

    public int getLegend() {
        return legend;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 240.0)
                .add(Attributes.ATTACK_DAMAGE, 14.0)
                .add(Attributes.ARMOR, 12.0)
                .add(Attributes.ARMOR_TOUGHNESS, 8.0)
                .add(Attributes.MOVEMENT_SPEED, 0.32)
                .add(Attributes.FOLLOW_RANGE, 40.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean hurt = super.hurt(source, amount);
        if (hurt && source.getEntity() instanceof ServerPlayer player) {
            contributors.add(player.getUUID());
        }
        return hurt;
    }

    @Override
    public void die(DamageSource source) {
        if (level() instanceof ServerLevel serverLevel) {
            int progress = rewardProgress(legend);
            for (UUID uuid : contributors) {
                ServerPlayer player = serverLevel.getServer().getPlayerList().getPlayer(uuid);
                if (player != null && player.level() == serverLevel && player.distanceToSqr(this) <= 128.0 * 128.0) {
                    ModEvent.increaseRealmProgress(player, progress);
                    awardGroupVictory(player, (legend - 1) / 5);
                    player.displayClientMessage(Component.translatable(
                            "message.abyssredemptiondaomod.legendary_shared_reward", progress), false);
                }
            }
        }
        super.die(source);
    }

    public static int rewardProgress(int legend) {
        return 12 + Math.max(0, Math.min(4, (legend - 1) / 5)) * 4;
    }

    private static void awardGroupVictory(ServerPlayer player, int group) {
        var advancement = player.getServer().getAdvancements().get(ResourceLocation.fromNamespaceAndPath(
                "abyssredemptiondaomod", "legend_group_" + group));
        if (advancement != null) {
            player.getAdvancements().award(advancement, "complete");
        }
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.18, false));
        goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.9));
        goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 12.0f));
        goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        bossEvent.setProgress(getHealth() / getMaxHealth());
        if (tickCount % 100 == 0 && getTarget() != null && getTarget().isAlive()) {
            useLegendaryArt(getTarget());
        }
    }

    private void useLegendaryArt(LivingEntity target) {
        if (!(level() instanceof ServerLevel serverLevel)) return;
        switch (legend) {
            case 1 -> {
                for (LivingEntity victim : level().getEntitiesOfClass(LivingEntity.class,
                        target.getBoundingBox().inflate(5), entity -> entity != this && entity.isAlive())) {
                    victim.hurt(damageSources().magic(), 12.0f);
                }
                serverLevel.sendParticles(ParticleTypes.SWEEP_ATTACK, target.getX(), target.getY() + 1, target.getZ(),
                        28, 3.0, 1.0, 3.0, 0.05);
            }
            case 2 -> {
                target.removeAllEffects();
                target.hurt(damageSources().magic(), 16.0f);
                serverLevel.sendParticles(ParticleTypes.REVERSE_PORTAL, target.getX(), target.getY() + 1, target.getZ(),
                        36, 0.8, 1.0, 0.8, 0.08);
            }
            case 3 -> {
                addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 120, 3));
                addEffect(new MobEffectInstance(MobEffects.REGENERATION, 120, 2));
                target.hurt(damageSources().magic(), 10.0f);
                serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, getX(), getY() + 1, getZ(),
                        32, 1.2, 1.0, 1.2, 0.05);
            }
            case 4 -> {
                target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 120, 8));
                target.setTicksFrozen(target.getTicksRequiredToFreeze() + 160);
                serverLevel.sendParticles(ParticleTypes.SNOWFLAKE, target.getX(), target.getY() + 1, target.getZ(),
                        45, 1.0, 1.2, 1.0, 0.04);
            }
            case 5 -> {
                Vec3 behind = target.position().subtract(target.getLookAngle().normalize().scale(2.0));
                randomTeleport(behind.x, behind.y, behind.z, true);
                target.hurt(damageSources().magic(), 14.0f);
                serverLevel.sendParticles(ParticleTypes.PORTAL, getX(), getY() + 1, getZ(),
                        40, 0.8, 1.0, 0.8, 0.1);
            }
            case 6 -> {
                addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 160, 3));
                addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 160, 2));
                target.hurt(damageSources().magic(), 10.0f);
                serverLevel.sendParticles(ParticleTypes.ENCHANT, getX(), getY() + 1, getZ(),
                        48, 1.4, 1.4, 1.4, 0.15);
            }
            case 7 -> {
                heal(18.0f);
                target.hurt(damageSources().magic(), 13.0f);
                target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 2));
                serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK, target.getX(), target.getY() + 1, target.getZ(),
                        52, 1.2, 1.4, 1.2, 0.12);
            }
            case 8 -> {
                addEffect(new MobEffectInstance(MobEffects.REGENERATION, 160, 3));
                target.addEffect(new MobEffectInstance(MobEffects.POISON, 160, 2));
                target.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 120, 0));
                serverLevel.sendParticles(ParticleTypes.SPORE_BLOSSOM_AIR, target.getX(), target.getY() + 1, target.getZ(),
                        42, 0.9, 1.2, 0.9, 0.08);
            }
            case 9 -> {
                target.setRemainingFireTicks(160);
                target.hurt(damageSources().magic(), 15.0f);
                addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 120, 2));
                serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, target.getX(), target.getY() + 1, target.getZ(),
                        45, 1.0, 1.2, 1.0, 0.06);
            }
            case 10 -> {
                target.addEffect(new MobEffectInstance(MobEffects.GLOWING, 160, 0));
                target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 160, 4));
                target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 160, 3));
                target.hurt(damageSources().magic(), 9.0f);
                serverLevel.sendParticles(ParticleTypes.WITCH, target.getX(), target.getY() + 1, target.getZ(),
                        64, 1.4, 1.6, 1.4, 0.1);
            }
            case 11 -> {
                target.addEffect(new MobEffectInstance(MobEffects.WITHER, 140, 2));
                target.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 100, 0));
                target.hurt(damageSources().magic(), 12.0f);
                heal(12.0f);
                serverLevel.sendParticles(ParticleTypes.SOUL, target.getX(), target.getY() + 1, target.getZ(),
                        55, 1.2, 1.5, 1.2, 0.08);
            }
            case 12 -> {
                addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 180, 4));
                addEffect(new MobEffectInstance(MobEffects.REGENERATION, 180, 2));
                target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 140, 3));
                target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 140, 3));
                serverLevel.sendParticles(ParticleTypes.TOTEM_OF_UNDYING, getX(), getY() + 1, getZ(),
                        60, 1.3, 1.7, 1.3, 0.1);
            }
            case 13 -> {
                addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 140, 4));
                target.hurt(damageSources().mobAttack(this), 20.0f);
                Vec3 force = target.position().subtract(position()).normalize().scale(2.2).add(0, 0.7, 0);
                target.push(force.x, force.y, force.z);
                serverLevel.sendParticles(ParticleTypes.CLOUD, target.getX(), target.getY() + 0.8, target.getZ(),
                        45, 1.0, 0.8, 1.0, 0.18);
            }
            case 14 -> {
                target.addEffect(new MobEffectInstance(MobEffects.WITHER, 180, 3));
                target.hurt(damageSources().magic(), 14.0f);
                heal(24.0f);
                serverLevel.sendParticles(ParticleTypes.DAMAGE_INDICATOR, target.getX(), target.getY() + 1, target.getZ(),
                        64, 1.5, 1.5, 1.5, 0.12);
            }
            case 15 -> {
                target.setRemainingFireTicks(240);
                target.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 50, 1));
                target.hurt(damageSources().magic(), 17.0f);
                addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 120, 3));
                serverLevel.sendParticles(ParticleTypes.FLAME, target.getX(), target.getY() + 1, target.getZ(),
                        72, 1.4, 1.8, 1.4, 0.09);
            }
            case 16 -> {
                target.removeAllEffects();
                target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 160, 4));
                target.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 120, 0));
                target.hurt(damageSources().magic(), 16.0f);
                serverLevel.sendParticles(ParticleTypes.REVERSE_PORTAL, target.getX(), target.getY() + 1, target.getZ(),
                        70, 1.6, 1.8, 1.6, 0.04);
            }
            case 17 -> {
                target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 180, 9));
                target.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 180, 4));
                target.setTicksFrozen(target.getTicksRequiredToFreeze() + 240);
                target.hurt(damageSources().freeze(), 12.0f);
                serverLevel.sendParticles(ParticleTypes.SNOWFLAKE, target.getX(), target.getY() + 1, target.getZ(),
                        80, 1.8, 1.8, 1.8, 0.03);
            }
            case 18 -> {
                target.removeAllEffects();
                target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 160, 5));
                target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 160, 4));
                target.addEffect(new MobEffectInstance(MobEffects.GLOWING, 160, 0));
                serverLevel.sendParticles(ParticleTypes.ENCHANTED_HIT, target.getX(), target.getY() + 1, target.getZ(),
                        65, 1.3, 1.6, 1.3, 0.1);
            }
            case 19 -> {
                target.addEffect(new MobEffectInstance(MobEffects.WITHER, 200, 2));
                target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 0));
                target.hurt(damageSources().magic(), 13.0f);
                addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 80, 0));
                serverLevel.sendParticles(ParticleTypes.ASH, target.getX(), target.getY() + 1, target.getZ(),
                        72, 1.5, 1.8, 1.5, 0.05);
            }
            case 20 -> {
                addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 100, 0));
                addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 4));
                Vec3 flank = target.position().subtract(target.getLookAngle().normalize().scale(2.5));
                randomTeleport(flank.x, flank.y, flank.z, true);
                target.hurt(damageSources().magic(), 18.0f);
                serverLevel.sendParticles(ParticleTypes.PORTAL, getX(), getY() + 1, getZ(),
                        60, 1.0, 1.2, 1.0, 0.12);
            }
            case 21 -> {
                addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 160, 4));
                addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 160, 3));
                target.setRemainingFireTicks(120);
                target.hurt(damageSources().mobAttack(this), 18.0f);
                serverLevel.sendParticles(ParticleTypes.END_ROD, getX(), getY() + 2, getZ(),
                        75, 1.8, 2.2, 1.8, 0.12);
            }
            case 22 -> {
                addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 180, 3));
                target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 180, 6));
                target.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 180, 3));
                target.hurt(damageSources().magic(), 10.0f);
                serverLevel.sendParticles(ParticleTypes.ENCHANT, target.getX(), target.getY() + 1, target.getZ(),
                        90, 1.8, 1.8, 1.8, 0.2);
            }
            case 23 -> {
                target.addEffect(new MobEffectInstance(MobEffects.GLOWING, 120, 0));
                target.hurt(damageSources().magic(), 24.0f);
                serverLevel.sendParticles(ParticleTypes.CRIT, target.getX(), target.getY() + 1, target.getZ(),
                        80, 1.0, 1.4, 1.0, 0.18);
            }
            case 24 -> {
                addEffect(new MobEffectInstance(MobEffects.REGENERATION, 160, 4));
                addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 160, 4));
                target.hurt(damageSources().mobAttack(this), 22.0f);
                Vec3 rage = target.position().subtract(position()).normalize().scale(2.8).add(0, 0.9, 0);
                target.push(rage.x, rage.y, rage.z);
                serverLevel.sendParticles(ParticleTypes.EXPLOSION, target.getX(), target.getY() + 1, target.getZ(),
                        8, 1.2, 1.0, 1.2, 0.05);
            }
            case 25 -> {
                target.removeAllEffects();
                target.hurt(damageSources().mobAttack(this), 26.0f);
                target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 140, 4));
                serverLevel.sendParticles(ParticleTypes.POOF, target.getX(), target.getY() + 1, target.getZ(),
                        100, 2.2, 1.8, 2.2, 0.2);
            }
            default -> { }
        }
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        bossEvent.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        bossEvent.removePlayer(player);
    }
}
