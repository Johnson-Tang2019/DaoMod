package com.abyssredemption.daomod.registry;

import com.abyssredemption.daomod.AbsDaoMod;
import com.abyssredemption.daomod.entity.LegendaryCultivatorEntity;
import com.abyssredemption.daomod.entity.SectGuardianEntity;
import com.abyssredemption.daomod.entity.SwordBeamEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(Registries.ENTITY_TYPE, AbsDaoMod.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<SwordBeamEntity>> SWORD_BEAM =
            ENTITIES.register("sword_beam", () -> EntityType.Builder.<SwordBeamEntity>of(SwordBeamEntity::new, MobCategory.MISC)
                    .sized(1.0F, 0.5F) // 剑气的碰撞箱大小
                    .build("sword_beam"));

    public static final DeferredHolder<EntityType<?>, EntityType<SectGuardianEntity>> SWORD_ATTENDANT =
            guardian("sword_attendant", 1);
    public static final DeferredHolder<EntityType<?>, EntityType<SectGuardianEntity>> CREATION_DISCIPLE =
            guardian("creation_disciple", 2);
    public static final DeferredHolder<EntityType<?>, EntityType<SectGuardianEntity>> THUNDER_MONK =
            guardian("thunder_monk", 3);
    public static final DeferredHolder<EntityType<?>, EntityType<SectGuardianEntity>> RAKSHASA_CULTIST =
            guardian("rakshasa_cultist", 4);
    public static final DeferredHolder<EntityType<?>, EntityType<LegendaryCultivatorEntity>> XUANYUAN =
            legend("xuanyuan", 1);
    public static final DeferredHolder<EntityType<?>, EntityType<LegendaryCultivatorEntity>> DUGU_QIUBAI =
            legend("dugu_qiubai", 2);
    public static final DeferredHolder<EntityType<?>, EntityType<LegendaryCultivatorEntity>> QINGLIAN_JIANXIAN =
            legend("qinglian_jianxian", 3);
    public static final DeferredHolder<EntityType<?>, EntityType<LegendaryCultivatorEntity>> YE_GUCHENG =
            legend("ye_gucheng", 4);
    public static final DeferredHolder<EntityType<?>, EntityType<LegendaryCultivatorEntity>> SHEN_DUZHOU =
            legend("shen_duzhou", 5);
    public static final DeferredHolder<EntityType<?>, EntityType<LegendaryCultivatorEntity>> NAMELESS_ARTIFICER =
            legend("nameless_artificer", 6);
    public static final DeferredHolder<EntityType<?>, EntityType<LegendaryCultivatorEntity>> GONGSHU_BAN =
            legend("gongshu_ban", 7);
    public static final DeferredHolder<EntityType<?>, EntityType<LegendaryCultivatorEntity>> GE_HONG =
            legend("ge_hong", 8);
    public static final DeferredHolder<EntityType<?>, EntityType<LegendaryCultivatorEntity>> OUYE_ZI =
            legend("ouye_zi", 9);
    public static final DeferredHolder<EntityType<?>, EntityType<LegendaryCultivatorEntity>> SU_HONGYI =
            legend("su_hongyi", 10);
    public static final DeferredHolder<EntityType<?>, EntityType<LegendaryCultivatorEntity>> MING_WANG =
            legend("ming_wang", 11);
    public static final DeferredHolder<EntityType<?>, EntityType<LegendaryCultivatorEntity>> DIZANG =
            legend("dizang", 12);
    public static final DeferredHolder<EntityType<?>, EntityType<LegendaryCultivatorEntity>> DAMO =
            legend("damo", 13);
    public static final DeferredHolder<EntityType<?>, EntityType<LegendaryCultivatorEntity>> BLOOD_RIVER_ANCESTOR =
            legend("blood_river_ancestor", 14);
    public static final DeferredHolder<EntityType<?>, EntityType<LegendaryCultivatorEntity>> WUHUA =
            legend("wuhua", 15);
    public static final DeferredHolder<EntityType<?>, EntityType<LegendaryCultivatorEntity>> MO_XUAN =
            legend("mo_xuan", 16);
    public static final DeferredHolder<EntityType<?>, EntityType<LegendaryCultivatorEntity>> GUANGHAN_FAIRY =
            legend("guanghan_fairy", 17);
    public static final DeferredHolder<EntityType<?>, EntityType<LegendaryCultivatorEntity>> FENGSHEN_DAOIST =
            legend("fengshen_daoist", 18);
    public static final DeferredHolder<EntityType<?>, EntityType<LegendaryCultivatorEntity>> GRAVE_KEEPER =
            legend("grave_keeper", 19);
    public static final DeferredHolder<EntityType<?>, EntityType<LegendaryCultivatorEntity>> NING_FAN =
            legend("ning_fan", 20);
    public static final DeferredHolder<EntityType<?>, EntityType<LegendaryCultivatorEntity>> KUAFU =
            legend("kuafu", 21);
    public static final DeferredHolder<EntityType<?>, EntityType<LegendaryCultivatorEntity>> JINGWEI =
            legend("jingwei", 22);
    public static final DeferredHolder<EntityType<?>, EntityType<LegendaryCultivatorEntity>> HOUYI =
            legend("houyi", 23);
    public static final DeferredHolder<EntityType<?>, EntityType<LegendaryCultivatorEntity>> XINGTIAN =
            legend("xingtian", 24);
    public static final DeferredHolder<EntityType<?>, EntityType<LegendaryCultivatorEntity>> XUANYUAN_FOURTEEN =
            legend("xuanyuan_fourteen", 25);

    private static DeferredHolder<EntityType<?>, EntityType<SectGuardianEntity>> guardian(String name, int sect) {
        return ENTITIES.register(name, () -> EntityType.Builder.<SectGuardianEntity>of(
                        (type, level) -> new SectGuardianEntity(type, level, sect), MobCategory.MONSTER)
                .sized(0.6f, 1.95f)
                .clientTrackingRange(8)
                .build(name));
    }

    private static DeferredHolder<EntityType<?>, EntityType<LegendaryCultivatorEntity>> legend(String name, int variant) {
        float width = variant == 21 || variant == 25 ? 1.25f : variant == 24 ? 0.9f : 0.65f;
        float height = variant == 21 || variant == 25 ? 3.6f : variant == 24 ? 2.6f : 2.0f;
        return ENTITIES.register(name, () -> EntityType.Builder.<LegendaryCultivatorEntity>of(
                        (type, level) -> new LegendaryCultivatorEntity(type, level, variant), MobCategory.MONSTER)
                .sized(width, height)
                .clientTrackingRange(12)
                .build(name));
    }

    public static void registerAttributes(EntityAttributeCreationEvent event) {
        var attributes = SectGuardianEntity.createAttributes().build();
        event.put(SWORD_ATTENDANT.get(), attributes);
        event.put(CREATION_DISCIPLE.get(), attributes);
        event.put(THUNDER_MONK.get(), attributes);
        event.put(RAKSHASA_CULTIST.get(), attributes);
        var legendaryAttributes = LegendaryCultivatorEntity.createAttributes().build();
        event.put(XUANYUAN.get(), legendaryAttributes);
        event.put(DUGU_QIUBAI.get(), legendaryAttributes);
        event.put(QINGLIAN_JIANXIAN.get(), legendaryAttributes);
        event.put(YE_GUCHENG.get(), legendaryAttributes);
        event.put(SHEN_DUZHOU.get(), legendaryAttributes);
        event.put(NAMELESS_ARTIFICER.get(), legendaryAttributes);
        event.put(GONGSHU_BAN.get(), legendaryAttributes);
        event.put(GE_HONG.get(), legendaryAttributes);
        event.put(OUYE_ZI.get(), legendaryAttributes);
        event.put(SU_HONGYI.get(), legendaryAttributes);
        event.put(MING_WANG.get(), legendaryAttributes);
        event.put(DIZANG.get(), legendaryAttributes);
        event.put(DAMO.get(), legendaryAttributes);
        event.put(BLOOD_RIVER_ANCESTOR.get(), legendaryAttributes);
        event.put(WUHUA.get(), legendaryAttributes);
        event.put(MO_XUAN.get(), legendaryAttributes);
        event.put(GUANGHAN_FAIRY.get(), legendaryAttributes);
        event.put(FENGSHEN_DAOIST.get(), legendaryAttributes);
        event.put(GRAVE_KEEPER.get(), legendaryAttributes);
        event.put(NING_FAN.get(), legendaryAttributes);
        event.put(KUAFU.get(), legendaryAttributes);
        event.put(JINGWEI.get(), legendaryAttributes);
        event.put(HOUYI.get(), legendaryAttributes);
        event.put(XINGTIAN.get(), legendaryAttributes);
        event.put(XUANYUAN_FOURTEEN.get(), legendaryAttributes);
    }

    public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        registerSpawn(event, SWORD_ATTENDANT.get(), false);
        registerSpawn(event, CREATION_DISCIPLE.get(), false);
        registerSpawn(event, THUNDER_MONK.get(), false);
        registerSpawn(event, RAKSHASA_CULTIST.get(), true);
    }

    private static void registerSpawn(RegisterSpawnPlacementsEvent event, EntityType<SectGuardianEntity> type,
                                      boolean requiresDarkness) {
        event.register(type, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                requiresDarkness ? Monster::checkMonsterSpawnRules : Monster::checkAnyLightMonsterSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }
}
