package com.abyssredemption.daomod.item;

import com.abyssredemption.daomod.entity.SwordBeamEntity;
import com.abyssredemption.daomod.network.CultivationPayload;
import com.abyssredemption.daomod.registry.ModAttachments;
import com.abyssredemption.daomod.registry.ModEntities;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SoulSwordItem extends SwordItem {
    public SoulSwordItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    // 1. 设置最大蓄力时间（返回一个很大的值，表示可以一直按着）
    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    // 2. 设置蓄力时的动作（使用拉弓动画最像在蓄力）
    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    // 3. 右键点击逻辑：检查炁是否足够
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        var data = player.getData(ModAttachments.CULTIVATION);

        // 基础门槛：至少要有 10 点炁才能开始蓄力
        if (data.getQi() >= 10.0) {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemstack);
        } else {
            if (!level.isClientSide) {
                player.displayClientMessage(Component.literal("炁不足，无法催动灵剑"), true);
            }
            return InteractionResultHolder.fail(itemstack);
        }
    }

    // 4. 释放按键逻辑：发射剑气
    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (!(entity instanceof Player player)) return;

        // 计算按下的时长 (getUseDuration - timeLeft)
        int i = this.getUseDuration(stack, entity) - timeLeft;
        float seconds = i / 20.0f;

        // 逻辑限制：最少蓄力 1s，最多 10s
        if (seconds < 1.0f) return;
        seconds = Math.min(seconds, 10.0f);

        long cost = (long) (seconds * 10); // 消耗 10-100
        var data = player.getData(ModAttachments.CULTIVATION);

        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            if (data.getQi() >= cost) {
                // 扣除并同步
                data.setQi(data.getQi() - cost);
                player.setData(ModAttachments.CULTIVATION, data);
                serverPlayer.connection.send(new CultivationPayload(data.getRealm(), data.getQi(), data.getSectOrthodoxy(), data.getStage()));

                // 生成剑气实体
                shootBeam(level, serverPlayer, seconds);
            } else {
                player.displayClientMessage(Component.literal("炁已枯竭，剑气自溃"), true);
            }
        }
    }

    private void shootBeam(Level level, ServerPlayer player, float power) {
        Vec3 look = player.getLookAngle();
        SwordBeamEntity beam = new SwordBeamEntity(
                ModEntities.SWORD_BEAM.get(), // 1. 传入注册好的类型
                player,                       // 2. 发射者
                look.x, look.y, look.z,      // 3. 方向加速度
                level,                        // 4. 世界
                power * 5.0f                  // 5. 最终伤害
        );

        beam.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
        level.addFreshEntity(beam);
    }
}
