package com.abyssredemption.daomod.item;

import com.abyssredemption.daomod.registry.ModAttachments;
import com.abyssredemption.daomod.util.SpiritualAuraHelper;
import java.util.List;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class WenlingDaggerItem extends Item {
    private static final int SENSE_RANGE = 10;
    private static final int COOLDOWN_TICKS = 100;
    private static final int SWORD_REALM = 1;

    public WenlingDaggerItem(Properties properties) {
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

        if (player.getData(ModAttachments.CULTIVATION).getRealm() < SWORD_REALM) {
            player.displayClientMessage(Component.literal("问灵短剑品阶为" + SoulSwordItem.getRealmName(SWORD_REALM)
                    + "，当前境界不足，无法催动感应。").withStyle(ChatFormatting.GRAY), true);
            return InteractionResultHolder.fail(stack);
        }

        Optional<BlockPos> target = findNearestAuraSource(level, player);
        if (target.isEmpty()) {
            player.displayClientMessage(Component.literal("问灵短剑微微一静，十米内未见可聚灵之物。")
                    .withStyle(ChatFormatting.GRAY), true);
        } else {
            BlockPos pos = target.get();
            BlockState state = level.getBlockState(pos);
            double distance = Math.sqrt(pos.distSqr(player.blockPosition()));
            int strength = SpiritualAuraHelper.getAuraSourceStrength(state);
            String direction = describeDirection(player, pos);

            player.displayClientMessage(Component.literal("剑身轻震，")
                    .withStyle(ChatFormatting.AQUA)
                    .append(Component.literal(direction).withStyle(ChatFormatting.GOLD))
                    .append(Component.literal("约 " + String.format("%.1f", distance) + " 米处有聚灵之物：")
                            .withStyle(ChatFormatting.GRAY))
                    .append(state.getBlock().getName().copy().withStyle(ChatFormatting.GREEN))
                    .append(Component.literal("，灵气强度 " + strength).withStyle(ChatFormatting.BLUE)), true);

            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.ENCHANTED_HIT,
                        pos.getX() + 0.5, pos.getY() + 0.6, pos.getZ() + 0.5,
                        8, 0.25, 0.25, 0.25, 0.01);
            }
        }

        player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.awardStat(Stats.ITEM_USED.get(this));
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents,
                                TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.literal("品阶：" + SoulSwordItem.getRealmName(SWORD_REALM)).withStyle(ChatFormatting.AQUA));
        tooltipComponents.add(Component.literal("筑基及以上方可催动感应灵气源。").withStyle(ChatFormatting.GRAY));
    }

    private static Optional<BlockPos> findNearestAuraSource(Level level, Player player) {
        BlockPos center = player.blockPosition();
        int rangeSqr = SENSE_RANGE * SENSE_RANGE;
        BlockPos nearest = null;
        double nearestDistance = Double.MAX_VALUE;

        for (BlockPos pos : BlockPos.betweenClosed(center.offset(-SENSE_RANGE, -SENSE_RANGE, -SENSE_RANGE),
                center.offset(SENSE_RANGE, SENSE_RANGE, SENSE_RANGE))) {
            double distance = pos.distSqr(center);
            if (distance > rangeSqr || distance >= nearestDistance) {
                continue;
            }

            if (SpiritualAuraHelper.isAuraSource(level.getBlockState(pos))) {
                nearest = pos.immutable();
                nearestDistance = distance;
            }
        }

        return Optional.ofNullable(nearest);
    }

    private static String describeDirection(Player player, BlockPos target) {
        Vec3 look = player.getLookAngle();
        Vec3 forward = new Vec3(look.x, 0.0, look.z).normalize();
        Vec3 targetCenter = Vec3.atCenterOf(target);
        Vec3 offset = targetCenter.subtract(player.position());
        Vec3 toTarget = new Vec3(offset.x, 0.0, offset.z).normalize();

        double forwardDot = forward.dot(toTarget);
        double side = forward.z * toTarget.x - forward.x * toTarget.z;

        if (forwardDot > 0.55) {
            return "前方";
        }
        if (forwardDot < -0.55) {
            return "身后";
        }
        return side > 0.0 ? "左侧" : "右侧";
    }
}
