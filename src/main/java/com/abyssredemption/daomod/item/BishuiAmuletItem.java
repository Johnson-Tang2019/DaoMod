package com.abyssredemption.daomod.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class BishuiAmuletItem extends Item implements ICurioItem {
    public BishuiAmuletItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity wearer = slotContext.entity();
        if (wearer.level().isClientSide || wearer.tickCount % 40 != 0) {
            return;
        }

        wearer.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 60, 0, false, false, true));
        wearer.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 60, 0, false, false, true));
        wearer.clearFire();
    }
}
