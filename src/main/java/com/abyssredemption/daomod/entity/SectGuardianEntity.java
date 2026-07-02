package com.abyssredemption.daomod.entity;

import com.abyssredemption.daomod.registry.ModAttachments;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
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

public class SectGuardianEntity extends Monster {
    private final int sect;

    public SectGuardianEntity(EntityType<? extends Monster> type, Level level, int sect) {
        super(type, level);
        this.sect = sect;
        this.xpReward = 12 + sect * 3;
    }

    public int getSect() {
        return sect;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 36.0)
                .add(Attributes.ATTACK_DAMAGE, 6.0)
                .add(Attributes.ARMOR, 4.0)
                .add(Attributes.MOVEMENT_SPEED, 0.29)
                .add(Attributes.FOLLOW_RANGE, 24.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.15);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.1, false));
        goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.9));
        goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0f));
        goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<Player>(
                this, Player.class, 10, true, false, this::isHostileTo));
    }

    private boolean isHostileTo(net.minecraft.world.entity.LivingEntity entity) {
        if (!(entity instanceof Player player)) {
            return false;
        }
        if (player.isCreative() || player.isSpectator()) {
            return false;
        }
        int playerSect = player.getData(ModAttachments.CULTIVATION).getSect();
        return sect == 4 || (playerSect != 0 && playerSect != sect);
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean hit = super.doHurtTarget(target);
        if (!hit || !(target instanceof net.minecraft.world.entity.LivingEntity living)) {
            return hit;
        }
        switch (sect) {
            case 1 -> living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 50, 0));
            case 2 -> addEffect(new MobEffectInstance(MobEffects.REGENERATION, 80, 0));
            case 3 -> living.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 80, 0));
            case 4 -> living.addEffect(new MobEffectInstance(MobEffects.WITHER, 60, 0));
            default -> { }
        }
        return true;
    }
}
