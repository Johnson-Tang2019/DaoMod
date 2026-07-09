package com.abyssredemption.daomod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class DaoRegionalBossEntity extends LegendaryCultivatorEntity {
    private final int variant;

    public DaoRegionalBossEntity(EntityType<? extends Monster> type, Level level, int variant) {
        super(type, level, legendForVariant(variant));
        this.variant = variant;
        var spec = specs(variant);
        setAttribute(Attributes.MAX_HEALTH, spec.health());
        setAttribute(Attributes.ATTACK_DAMAGE, spec.attack());
        setAttribute(Attributes.ARMOR, spec.armor());
        setAttribute(Attributes.ARMOR_TOUGHNESS, spec.toughness());
        setAttribute(Attributes.MOVEMENT_SPEED, spec.speed());
        setAttribute(Attributes.KNOCKBACK_RESISTANCE, spec.knockback());
        setHealth((float) spec.health());
        this.xpReward = spec.xp();
    }

    public int getVariant() {
        return variant;
    }

    public static int requiredRealm(int variant) {
        return specs(variant).requiredRealm();
    }

    public String advancementId() {
        return switch (variant) {
            case 2 -> "defeat_leize_jiao";
            case 3 -> "defeat_sword_remnant";
            case 4 -> "defeat_fire_xiao_king";
            case 5 -> "defeat_minghai_monk";
            case 6 -> "defeat_guixu_king";
            case 7 -> "defeat_void_demon";
            default -> "defeat_shentu_stele_spirit";
        };
    }

    private static int legendForVariant(int variant) {
        return switch (variant) {
            case 2 -> 18;
            case 3 -> 2;
            case 4 -> 21;
            case 5 -> 12;
            case 6 -> 19;
            case 7 -> 16;
            default -> 3;
        };
    }

    private static Spec specs(int variant) {
        return switch (variant) {
            case 2 -> new Spec(1200, 30, 26, 12, 0.27, 0.85, 220, 2);
            case 3 -> new Spec(1500, 38, 22, 14, 0.34, 0.70, 240, 2);
            case 4 -> new Spec(1800, 42, 28, 14, 0.31, 0.80, 260, 3);
            case 5 -> new Spec(2200, 48, 32, 16, 0.24, 0.90, 300, 3);
            case 6 -> new Spec(2600, 54, 34, 16, 0.38, 0.80, 340, 3);
            case 7 -> new Spec(3200, 64, 38, 18, 0.30, 0.95, 400, 4);
            default -> new Spec(900, 24, 24, 10, 0.20, 1.00, 180, 2);
        };
    }

    private void setAttribute(net.minecraft.core.Holder<net.minecraft.world.entity.ai.attributes.Attribute> attribute,
                              double value) {
        var instance = getAttribute(attribute);
        if (instance != null) {
            instance.setBaseValue(value);
        }
    }

    private record Spec(double health, double attack, double armor, double toughness, double speed,
                        double knockback, int xp, int requiredRealm) {
    }
}
