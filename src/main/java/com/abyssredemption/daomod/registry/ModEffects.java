package com.abyssredemption.daomod.registry;

import com.abyssredemption.daomod.AbsDaoMod;
import com.abyssredemption.daomod.effect.DanDuEffect;
import com.abyssredemption.daomod.effect.DanDuFanShiEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, AbsDaoMod.MODID);

    // 丹毒效果
    public static final DeferredHolder<MobEffect, DanDuEffect> DAN_DU =
            MOB_EFFECTS.register("dan_du", DanDuEffect::new);

    // 丹毒反噬效果：期间不可吃丹，stage/realmProgress 锁定
    public static final DeferredHolder<MobEffect, DanDuFanShiEffect> DAN_DU_FAN_SHI =
            MOB_EFFECTS.register("dan_du_fan_shi", DanDuFanShiEffect::new);
}
