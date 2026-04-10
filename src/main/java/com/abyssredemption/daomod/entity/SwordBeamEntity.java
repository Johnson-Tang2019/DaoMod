package com.abyssredemption.daomod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class SwordBeamEntity extends AbstractHurtingProjectile implements ItemSupplier {
    private float damage;

    // 默认构造函数（必须保留，用于系统注册）
    public SwordBeamEntity(EntityType<? extends SwordBeamEntity> type, Level level) {
        super(type, level);
    }

    // 自定义构造函数（发射时调用）
    public SwordBeamEntity(EntityType<? extends SwordBeamEntity> type, LivingEntity shooter, double dx, double dy, double dz, Level level, float damage) {
        // 参数顺序：EntityType, shooter(发射者), new Vec3(加速度), Level
        super(type, shooter, new net.minecraft.world.phys.Vec3(dx, dy, dz), level);
        this.damage = damage;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!this.level().isClientSide) {
            result.getEntity().hurt(this.damageSources().magic(), this.damage);
            this.discard(); // 击中消失
        }
    }

    @Override
    protected boolean shouldBurn() { return false; } // 剑气不带火

    @Override
    public ItemStack getItem() {
        // 暂时借用“烈焰粉”或“青金石”的贴图作为剑气的外观，你可以换成任何物品
        return new ItemStack(Items.FLINT);
    }
}