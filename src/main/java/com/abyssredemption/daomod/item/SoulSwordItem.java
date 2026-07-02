package com.abyssredemption.daomod.item;

import com.abyssredemption.daomod.entity.SwordBeamEntity;
import com.abyssredemption.daomod.network.CultivationPayload;
import com.abyssredemption.daomod.registry.ModAttachments;
import com.abyssredemption.daomod.registry.ModEntities;
import com.abyssredemption.daomod.util.SwordFlightHelper;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SoulSwordItem extends SwordItem {
    private static final String[] REALM_NAMES = {
            "炼气", "筑基", "金丹", "元婴", "化神", "炼虚", "合体", "大乘", "渡劫"
    };
    private final int swordRealm;

    public SoulSwordItem(Tier tier, Properties properties) {
        this(tier, properties, 0);
    }

    public SoulSwordItem(Tier tier, Properties properties, int swordRealm) {
        super(tier, properties);
        this.swordRealm = Math.max(0, Math.min(8, swordRealm));
    }

    public int getSwordRealm() {
        return swordRealm;
    }

    public boolean isAboveRealm(Player player) {
        return player.getData(ModAttachments.CULTIVATION).getRealm() < swordRealm;
    }

    public static String getRealmName(int realm) {
        if (realm < 0 || realm >= REALM_NAMES.length) {
            return "未知";
        }
        return REALM_NAMES[realm];
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        var data = player.getData(ModAttachments.CULTIVATION);

        if (SwordFlightHelper.tryStartSwordFlight(level, player, hand, itemstack)) {
            return InteractionResultHolder.consume(itemstack);
        }

        if (isAboveRealm(player)) {
            if (!level.isClientSide) {
                player.displayClientMessage(Component.literal("此剑品阶为" + getRealmName(swordRealm)
                        + "，当前境界不足，只能作普通剑使用。"), true);
            }
            return InteractionResultHolder.fail(itemstack);
        }

        if (data.getQi() >= 10) {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemstack);
        }

        if (!level.isClientSide) {
            player.displayClientMessage(Component.literal("炁不足，无法催动灵剑。"), true);
        }
        return InteractionResultHolder.fail(itemstack);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (!(entity instanceof Player player) || isAboveRealm(player)) {
            return;
        }

        int heldTicks = this.getUseDuration(stack, entity) - timeLeft;
        float seconds = heldTicks / 20.0f;
        if (seconds < 1.0f) {
            return;
        }
        seconds = Math.min(seconds, 10.0f);

        long cost = (long) (seconds * 10);
        var data = player.getData(ModAttachments.CULTIVATION);

        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            if (data.getQi() >= cost) {
                data.setQi(data.getQi() - cost);
                player.setData(ModAttachments.CULTIVATION, data);
                serverPlayer.connection.send(new CultivationPayload(data.getRealm(), data.getQi(), data.getSectOrthodoxy(),
                        data.getStage(), data.getRealmProgress(), data.getKarma(), data.getSect()));
                shootBeam(level, serverPlayer, player.getUsedItemHand(), seconds);
            } else {
                player.displayClientMessage(Component.literal("炁已枯竭，剑气自溃。"), true);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents,
                                TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.literal("品阶：" + getRealmName(swordRealm)).withStyle(ChatFormatting.AQUA));
        tooltipComponents.add(Component.literal("境界不足时无法催动剑气，近战伤害降低。").withStyle(ChatFormatting.GRAY));
    }

    private void shootBeam(Level level, ServerPlayer player, InteractionHand usedHand, float power) {
        Vec3 look = player.getLookAngle();
        SwordBeamEntity beam = new SwordBeamEntity(
                ModEntities.SWORD_BEAM.get(),
                player,
                look.x, look.y, look.z,
                level,
                power * 5.0f
        );

        Vec3 muzzle = getSwordMuzzlePosition(player, usedHand, look);
        beam.setPos(muzzle.x, muzzle.y, muzzle.z);
        level.addFreshEntity(beam);
    }

    private Vec3 getSwordMuzzlePosition(ServerPlayer player, InteractionHand usedHand, Vec3 look) {
        HumanoidArm arm = usedHand == InteractionHand.MAIN_HAND
                ? player.getMainArm()
                : player.getMainArm().getOpposite();
        double side = arm == HumanoidArm.RIGHT ? 1.0 : -1.0;

        double yaw = Math.toRadians(player.getYRot());
        Vec3 right = new Vec3(-Math.cos(yaw), 0.0, -Math.sin(yaw));

        return new Vec3(player.getX(), player.getEyeY() - 0.42, player.getZ())
                .add(look.normalize().scale(0.95))
                .add(right.scale(0.38 * side));
    }
}
