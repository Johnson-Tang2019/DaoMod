package com.abyssredemption.daomod.item;

import com.abyssredemption.daomod.network.CultivationPayload;
import com.abyssredemption.daomod.registry.ModAttachments;
import com.abyssredemption.daomod.registry.ModEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class DanItem extends Item {

    private static final int MAX_DAN_DU = 5;
    private static final int FAN_SHI_DURATION = 12000; // 10 分钟（20 tick/秒 * 60秒 * 10分）
    private static final int DAN_DU_EFFECT_DURATION = 12000; // 丹毒显示持续 10 分钟
    private static final FoodProperties DAN_FOOD = new FoodProperties.Builder()
            .nutrition(1)
            .saturationModifier(0)
            .alwaysEdible()
            .build();

    public DanItem(Properties properties) {
        super(properties.food(DAN_FOOD));
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 32; // 快速食用
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.EAT;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        // 检查是否有丹毒反噬效果，有则不能吃
        if (player.hasEffect(ModEffects.DAN_DU_FAN_SHI)) {
            if (!level.isClientSide) {
                player.displayClientMessage(Component.literal("§c丹毒未消，无法服丹！"), true);
            }
            return InteractionResultHolder.fail(itemstack);
        }

        // 可以吃
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(itemstack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        // 先执行食用逻辑（获得状态效果）
        ItemStack result = super.finishUsingItem(stack, level, livingEntity);

        // 仅在服务端处理
        if (!level.isClientSide && livingEntity instanceof ServerPlayer player) {

            // 再次检查反噬（防止漏洞）
            if (player.hasEffect(ModEffects.DAN_DU_FAN_SHI)) {
                return result;
            }

            var data = player.getData(ModAttachments.CULTIVATION);

            // 1. 增加 1 个小境界（stage）
            data.setStage(data.getStage() + 1);

            // 2. 丹毒处理：增加一层丹毒（用 sectOrthodoxy 存储）
            int danDu = data.getSectOrthodoxy() + 1;
            if (danDu >= MAX_DAN_DU) {
                // 达到五层，丹毒反噬：给予自定义反噬效果 10 分钟
                player.addEffect(new MobEffectInstance(ModEffects.DAN_DU_FAN_SHI, FAN_SHI_DURATION, 0, false, true, true));
                // 也给予视觉反馈的反胃效果
                player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 2));
                player.displayClientMessage(Component.literal("§c§l⚠ 丹毒反噬！经脉封闭，修为停滞！"), true);
                danDu = 0; // 反噬后重置丹毒
}
            // 用自定义丹毒效果显示层数（图标显示，持续 10 分钟）
            if (danDu > 0) {
                player.addEffect(new MobEffectInstance(ModEffects.DAN_DU, DAN_DU_EFFECT_DURATION, danDu - 1, false, true, true));
            } else {
                player.removeEffect(ModEffects.DAN_DU);
            }
            data.setSectOrthodoxy(danDu);

            player.setData(ModAttachments.CULTIVATION, data);

            // 3. 同步数据到客户端
            player.connection.send(new CultivationPayload(
                    data.getRealm(),
                    data.getQi(),
                    data.getSectOrthodoxy(),
                    data.getStage(),
                    data.getRealmProgress(),
                    data.getKarma(),
                    data.getSect()
            ));

            // 4. 发送突破提示
            player.displayClientMessage(Component.literal("§e丹药入腹，修为精进！"), true);
        }

        return result;
    }
}
