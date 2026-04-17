package com.abyssredemption.daomod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Marker;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityMountEvent;

import java.util.List;

public class PuTuan extends Block {
    public PuTuan(Properties properties) {
        super(properties);
    }


    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            // 1. 检查当前方块位置是否已经有“座位”了
            // 搜索范围稍微扩大一点点，确保能抓到
            List<ArmorStand> existingSeats = level.getEntitiesOfClass(ArmorStand.class, new AABB(pos).inflate(0.1));
            for (ArmorStand s : existingSeats) {
                // 如果这个盔甲架有乘客，直接返回，不生成新的
                if (s.getTags().contains("daomod_seat") && s.hasPassenger(p -> true)) {
                    return ItemInteractionResult.SUCCESS;
                }
                // 如果这个盔甲架没乘客（残留的），先删掉它，允许玩家重新坐下
                if (s.getTags().contains("daomod_seat")) {
                    s.discard();
                }
            }

            // 2. 创建一个隐形的盔甲架
            ArmorStand seat = new ArmorStand(level, pos.getX() + 0.5, pos.getY() - 1, pos.getZ() + 0.5);
            seat.setInvisible(true);    // 隐形
            seat.setNoGravity(true);    // 禁重力
            seat.addTag("daomod_seat"); // 打上标签
            byte b0 = seat.getEntityData().get(ArmorStand.DATA_CLIENT_FLAGS);
            seat.getEntityData().set(ArmorStand.DATA_CLIENT_FLAGS, (byte)(b0 | 1));
            seat.setInvulnerable(true);
            // 关键点：ArmorStand 默认是可以骑乘的
            level.addFreshEntity(seat);

            player.startRiding(seat, true); // true 表示强制骑乘
        }
        return ItemInteractionResult.SUCCESS;
    }

    @SubscribeEvent
    public static void onEntityDismount(EntityMountEvent event) {
        // 检查是否是我们的 Marker 座位
        if (event.isDismounting() && event.getEntityBeingMounted() instanceof Marker seat) {
            if (seat.getTags().contains("daomod_seat")) {
                seat.discard();
            }
        }
    }
}
