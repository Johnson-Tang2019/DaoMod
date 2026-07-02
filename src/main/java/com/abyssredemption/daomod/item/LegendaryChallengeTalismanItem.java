package com.abyssredemption.daomod.item;

import com.abyssredemption.daomod.entity.LegendaryCultivatorEntity;
import com.abyssredemption.daomod.registry.ModAttachments;
import com.abyssredemption.daomod.registry.ModEntities;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class LegendaryChallengeTalismanItem extends Item {
    private static final int COOLDOWN_TICKS = 1200;
    private final int group;
    private final int requiredRealm;

    public LegendaryChallengeTalismanItem(Properties properties, int group, int requiredRealm) {
        super(properties.stacksTo(1));
        this.group = group;
        this.requiredRealm = requiredRealm;
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
        if (player.getData(ModAttachments.CULTIVATION).getRealm() < requiredRealm) {
            player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.challenge_realm_low",
                    Component.translatable("gui.daomod.realm" + requiredRealm)).withStyle(ChatFormatting.RED), true);
            return InteractionResultHolder.fail(stack);
        }
        if (!(level instanceof ServerLevel serverLevel)) {
            return InteractionResultHolder.fail(stack);
        }
        if (!(player instanceof ServerPlayer serverPlayer) || !hasPreviousVictory(serverPlayer)) {
            player.displayClientMessage(Component.translatable(
                    "message.abyssredemptiondaomod.challenge_previous_required").withStyle(ChatFormatting.RED), true);
            return InteractionResultHolder.fail(stack);
        }
        if (!level.getEntitiesOfClass(LegendaryCultivatorEntity.class,
                new AABB(player.blockPosition()).inflate(96.0), entity -> entity.isAlive()).isEmpty()) {
            player.displayClientMessage(Component.translatable(
                    "message.abyssredemptiondaomod.challenge_already_active").withStyle(ChatFormatting.RED), true);
            return InteractionResultHolder.fail(stack);
        }

        EntityType<? extends LegendaryCultivatorEntity> type = selectType(level.random.nextInt(5));
        LegendaryCultivatorEntity challenger = type.create(serverLevel);
        if (challenger == null) {
            return InteractionResultHolder.fail(stack);
        }

        Vec3 look = player.getLookAngle().multiply(1, 0, 1).normalize();
        Vec3 summonAt = player.position().add(look.scale(6.0));
        challenger.moveTo(summonAt.x, player.getY(), summonAt.z, player.getYRot() + 180.0f, 0.0f);
        challenger.setTarget(player);
        serverLevel.addFreshEntity(challenger);
        serverLevel.sendParticles(ParticleTypes.PORTAL, summonAt.x, player.getY() + 1.2, summonAt.z,
                120, 1.5, 1.8, 1.5, 0.25);
        serverLevel.playSound(null, player.blockPosition(), SoundEvents.END_PORTAL_SPAWN,
                SoundSource.HOSTILE, 1.2f, 0.75f);
        player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.challenge_begins",
                challenger.getDisplayName()).withStyle(ChatFormatting.GOLD), false);
        player.awardStat(Stats.ITEM_USED.get(this));
        player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        return InteractionResultHolder.consume(stack);
    }

    private boolean hasPreviousVictory(ServerPlayer player) {
        if (group == 0) return true;
        var advancement = player.getServer().getAdvancements().get(ResourceLocation.fromNamespaceAndPath(
                "abyssredemptiondaomod", "legend_group_" + (group - 1)));
        return advancement != null && player.getAdvancements().getOrStartProgress(advancement).isDone();
    }

    private EntityType<? extends LegendaryCultivatorEntity> selectType(int index) {
        return switch (group * 5 + index) {
            case 0 -> ModEntities.XUANYUAN.get();
            case 1 -> ModEntities.DUGU_QIUBAI.get();
            case 2 -> ModEntities.QINGLIAN_JIANXIAN.get();
            case 3 -> ModEntities.YE_GUCHENG.get();
            case 4 -> ModEntities.SHEN_DUZHOU.get();
            case 5 -> ModEntities.NAMELESS_ARTIFICER.get();
            case 6 -> ModEntities.GONGSHU_BAN.get();
            case 7 -> ModEntities.GE_HONG.get();
            case 8 -> ModEntities.OUYE_ZI.get();
            case 9 -> ModEntities.SU_HONGYI.get();
            case 10 -> ModEntities.MING_WANG.get();
            case 11 -> ModEntities.DIZANG.get();
            case 12 -> ModEntities.DAMO.get();
            case 13 -> ModEntities.BLOOD_RIVER_ANCESTOR.get();
            case 14 -> ModEntities.WUHUA.get();
            case 15 -> ModEntities.MO_XUAN.get();
            case 16 -> ModEntities.GUANGHAN_FAIRY.get();
            case 17 -> ModEntities.FENGSHEN_DAOIST.get();
            case 18 -> ModEntities.GRAVE_KEEPER.get();
            case 19 -> ModEntities.NING_FAN.get();
            case 20 -> ModEntities.KUAFU.get();
            case 21 -> ModEntities.JINGWEI.get();
            case 22 -> ModEntities.HOUYI.get();
            case 23 -> ModEntities.XINGTIAN.get();
            default -> ModEntities.XUANYUAN_FOURTEEN.get();
        };
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents,
                                TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.abyssredemptiondaomod.challenge_group" + group)
                .withStyle(ChatFormatting.GOLD));
        tooltipComponents.add(Component.translatable("tooltip.abyssredemptiondaomod.challenge_required",
                Component.translatable("gui.daomod.realm" + requiredRealm)).withStyle(ChatFormatting.GRAY));
    }
}
