package com.abyssredemption.daomod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.EntityType;
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


    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                              Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            // 1. 检查是否已经有人坐在上面了
            List<ArmorStand> existingSeats = level.getEntitiesOfClass(ArmorStand.class, new AABB(pos));
            for (ArmorStand seat : existingSeats) {
                if (seat.hasPassenger(p -> true)) return ItemInteractionResult.SUCCESS;
            }

            // 2. 创建一个隐形的盔甲架作为“座位”
            // 为什么用盔甲架？因为它轻量且容易控制
            // 使用 Marker 实体代替 ArmorStand
            Marker seat = EntityType.MARKER.create(level);
            if (seat != null) {
                seat.moveTo(pos.getX() + 0.5, pos.getY() + 0.1, pos.getZ() + 0.5); // 稍微调高一点
                seat.addTag("daomod_seat");
                level.addFreshEntity(seat);
                player.startRiding(seat);
            }
            // 让玩家骑上去
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
