package com.abyssredemption.daomod.world;

import java.util.Map;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public final class DimensionRules {
    private static final Map<ResourceKey<Level>, DimensionRule> RULES = Map.of(
            DimensionKeys.LINGXU, new DimensionRule(14, 2.0, 1.5, 1.2, 1.1),
            DimensionKeys.DILUOYUAN, new DimensionRule(46, 4.5, 0.7, 2.5, 1.8),
            DimensionKeys.XIANYU, new DimensionRule(73, 6.0, 2.5, 3.0, 0.5),
            DimensionKeys.LINGXU_LEGACY, new DimensionRule(14, 2.0, 1.5, 1.2, 1.1),
            DimensionKeys.DILUOYUAN_LEGACY, new DimensionRule(46, 4.5, 0.7, 2.5, 1.8),
            DimensionKeys.XIANYU_LEGACY, new DimensionRule(73, 6.0, 2.5, 3.0, 0.5));

    public static DimensionRule ruleFor(ResourceKey<Level> dimension) {
        return RULES.get(dimension);
    }

    public static int pressureGap(Level level, int realm, int stage) {
        DimensionRule rule = ruleFor(level.dimension());
        return rule == null ? 0 : rule.minLevel() - DimensionKeys.cultivationLevel(realm, stage);
    }

    public record DimensionRule(int minLevel, double cultivationMultiplier, double qiRegenMultiplier,
                                double karmaMultiplier, double toxinMultiplier) {
    }

    private DimensionRules() {
    }
}
