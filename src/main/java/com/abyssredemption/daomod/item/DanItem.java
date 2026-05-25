package com.abyssredemption.daomod.item;

import com.abyssredemption.daomod.network.CultivationPayload;
import com.abyssredemption.daomod.registry.ModAttachments;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DanItem extends Item {

    private static final FoodProperties DAN_FOOD = new FoodProperties.Builder()
            .nutrition(4)
            .saturationModifier(1.2f)
            .alwaysEdible()
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 100, 1), 1.0f)
            .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 2400, 0), 1.0f)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 6000, 0), 1.0f)
            .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 6000, 0), 1.0f)
            .build();

    public DanItem(Properties properties) {
        super(properties.food(DAN_FOOD));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        // 先执行食用逻辑（获得状态效果）
        ItemStack result = super.finishUsingItem(stack, level, livingEntity);

        // 仅在服务端处理境界提升
        if (!level.isClientSide && livingEntity instanceof ServerPlayer player) {
            var data = player.getData(ModAttachments.CULTIVATION);

            // 增加 1 个小境界（stage）
            data.setStage(data.getStage() + 1);

            player.setData(ModAttachments.CULTIVATION, data);

            // 同步数据到客户端
            player.connection.send(new CultivationPayload(
                    data.getRealm(),
                    data.getQi(),
                    data.getSectOrthodoxy(),
                    data.getStage(),
                    data.getRealmProgress()
            ));

            // 发送提示
            player.displayClientMessage(Component.literal("§e丹药入腹，修为精进！"), true);
        }

        return result;
    }
}
