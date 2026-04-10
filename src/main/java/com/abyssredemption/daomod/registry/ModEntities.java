package com.abyssredemption.daomod.registry;

import com.abyssredemption.daomod.AbsDaoMod;
import com.abyssredemption.daomod.entity.SwordBeamEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(Registries.ENTITY_TYPE, AbsDaoMod.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<SwordBeamEntity>> SWORD_BEAM =
            ENTITIES.register("sword_beam", () -> EntityType.Builder.<SwordBeamEntity>of(SwordBeamEntity::new, MobCategory.MISC)
                    .sized(1.0F, 0.5F) // 剑气的碰撞箱大小
                    .build("sword_beam"));
}