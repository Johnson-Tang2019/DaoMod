package com.abyssredemption.daomod.world;

import com.abyssredemption.daomod.AbsDaoMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public final class DimensionKeys {
    public static final ResourceKey<Level> LINGXU = dimension("lingxu");
    public static final ResourceKey<Level> DILUOYUAN = dimension("diluoyuan");
    public static final ResourceKey<Level> XIANYU = dimension("xianyu");
    public static final ResourceKey<Level> LINGXU_LEGACY = dimension("lingxu_realm");
    public static final ResourceKey<Level> DILUOYUAN_LEGACY = dimension("diluo_abyss");
    public static final ResourceKey<Level> XIANYU_LEGACY = dimension("eternal_immortal_realm");

    public static int cultivationLevel(int majorRealm, int minorStage) {
        return Math.max(0, majorRealm) * 9 + Math.max(0, minorStage) + 1;
    }

    private static ResourceKey<Level> dimension(String name) {
        return ResourceKey.create(Registries.DIMENSION,
                ResourceLocation.fromNamespaceAndPath(AbsDaoMod.MODID, name));
    }

    private DimensionKeys() {
    }
}
