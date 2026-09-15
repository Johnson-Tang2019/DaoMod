package com.abyssredemption.daomod.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class SwordBeamEntity extends AbstractHurtingProjectile implements ItemSupplier {
    private static final EntityDataAccessor<Float> DAMAGE = SynchedEntityData.defineId(SwordBeamEntity.class, EntityDataSerializers.FLOAT);
    private static final int MAX_LIFETIME_TICKS = 100;

    // 默认构造函数（必须保留，用于系统注册）
    public SwordBeamEntity(EntityType<? extends SwordBeamEntity> type, Level level) {
        super(type, level);
    }

    // 自定义构造函数（发射时调用）
    public SwordBeamEntity(EntityType<? extends SwordBeamEntity> type, LivingEntity shooter, double dx, double dy, double dz, Level level, float damage) {
        // 参数顺序：EntityType, shooter(发射者), new Vec3(加速度), Level
        super(type, shooter, new net.minecraft.world.phys.Vec3(dx, dy, dz), level);
        setDamage(damage);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!this.level().isClientSide) {
            result.getEntity().hurt(swordDamageSource(), getDamage());
            this.discard(); // 击中消失
        }
    }

    public DamageSource swordDamageSource() {
        return getOwner() == null ? damageSources().magic() : damageSources().indirectMagic(this, getOwner());
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!this.level().isClientSide) {


            //方案 B：如果你想要小规模的剑气爆炸效果（威力设为 1.0 左右）

            this.discard(); // 剑气撞墙后消失
        }
    }

    @Override
    protected boolean shouldBurn() { return false; } // 剑气不带火

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide && tickCount >= MAX_LIFETIME_TICKS) {
            discard();
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DAMAGE, 0.0f);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("Damage", getDamage());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setDamage(tag.getFloat("Damage"));
    }

    private float getDamage() {
        return entityData.get(DAMAGE);
    }

    private void setDamage(float damage) {
        entityData.set(DAMAGE, Math.max(0.0f, damage));
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Items.FLINT);
    }
}
