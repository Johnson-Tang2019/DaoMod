package com.abyssredemption.daomod.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FuyaoFanItem extends Item {
    private static final double RANGE = 6.0;
    private static final int COOLDOWN_TICKS = 20 * 8;

    public FuyaoFanItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide) {
            return InteractionResultHolder.success(stack);
        }
        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.fail(stack);
        }

        Vec3 look = player.getLookAngle().normalize();
        int affected = 0;
        for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(RANGE),
                entity -> entity.isAlive() && entity != player)) {
            Vec3 offset = target.position().subtract(player.position());
            if (offset.lengthSqr() > RANGE * RANGE || look.dot(offset.normalize()) < 0.35) {
                continue;
            }

            Vec3 push = new Vec3(offset.x, 0.0, offset.z).normalize().scale(1.05).add(0.0, 0.25, 0.0);
            target.push(push.x, push.y, push.z);
            target.hurtMarked = true;
            target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 40, 0, false, true));
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 0, false, true));
            affected++;
        }

        player.displayClientMessage(Component.literal(affected > 0
                        ? "扶摇扇卷起小旋风，扰乱了 " + affected + " 个目标。"
                        : "扶摇扇轻响，前方没有可扰动的目标。")
                .withStyle(ChatFormatting.AQUA), true);
        player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
