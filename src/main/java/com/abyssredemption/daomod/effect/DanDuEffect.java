package com.abyssredemption.daomod.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class DanDuEffect extends MobEffect {

    public DanDuEffect() {
        // 有害效果（红色），不显示粒子
        super(MobEffectCategory.HARMFUL, 0x6B006B); // 深紫色
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        // 丹毒不做任何实际伤害，仅作为图标显示
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        // 每 tick 都返回 true 来维持效果，但不做任何事情
        return false;
    }
}
