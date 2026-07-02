package com.abyssredemption.daomod.registry;

import com.abyssredemption.daomod.AbsDaoMod;
import com.abyssredemption.daomod.worldgen.SectOutpostFeature;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(Registries.FEATURE, AbsDaoMod.MODID);

    public static final DeferredHolder<Feature<?>, SectOutpostFeature> SWORD_PAVILION = outpost("sword_pavilion", 1);
    public static final DeferredHolder<Feature<?>, SectOutpostFeature> CREATION_WORKSHOP = outpost("creation_workshop", 2);
    public static final DeferredHolder<Feature<?>, SectOutpostFeature> THUNDER_SHRINE = outpost("thunder_shrine", 3);
    public static final DeferredHolder<Feature<?>, SectOutpostFeature> RAKSHASA_ALTAR = outpost("rakshasa_altar", 4);
    public static final DeferredHolder<Feature<?>, SectOutpostFeature> SWORD_TOMB = outpost("sword_tomb", 5);
    public static final DeferredHolder<Feature<?>, SectOutpostFeature> CREATION_VAULT = outpost("creation_vault", 6);
    public static final DeferredHolder<Feature<?>, SectOutpostFeature> UNDERWORLD_SHRINE = outpost("underworld_shrine", 7);
    public static final DeferredHolder<Feature<?>, SectOutpostFeature> VOID_OBSERVATORY = outpost("void_observatory", 8);
    public static final DeferredHolder<Feature<?>, SectOutpostFeature> ANCIENT_MONUMENT = outpost("ancient_monument", 9);

    private static DeferredHolder<Feature<?>, SectOutpostFeature> outpost(String name, int sect) {
        return FEATURES.register(name, () -> new SectOutpostFeature(NoneFeatureConfiguration.CODEC, sect));
    }

    private ModFeatures() {
    }
}
