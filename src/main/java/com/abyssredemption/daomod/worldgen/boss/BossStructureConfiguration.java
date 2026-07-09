package com.abyssredemption.daomod.worldgen.boss;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record BossStructureConfiguration(int variant, int radius, boolean clearArea) implements FeatureConfiguration {
    public static final Codec<BossStructureConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("variant").forGetter(BossStructureConfiguration::variant),
            Codec.INT.optionalFieldOf("radius", 18).forGetter(BossStructureConfiguration::radius),
            Codec.BOOL.optionalFieldOf("clear_area", true).forGetter(BossStructureConfiguration::clearArea)
    ).apply(instance, BossStructureConfiguration::new));
}
