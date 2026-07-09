package com.abyssredemption.daomod.gametest;

import com.abyssredemption.daomod.AbsDaoMod;
import com.abyssredemption.daomod.attachment.CultivationData;
import com.abyssredemption.daomod.entity.DaoRegionalBossEntity;
import com.abyssredemption.daomod.entity.LegendaryCultivatorEntity;
import com.abyssredemption.daomod.entity.SectGuardianEntity;
import com.abyssredemption.daomod.entity.SwordBeamEntity;
import com.abyssredemption.daomod.network.CultivationPayload;
import com.abyssredemption.daomod.registry.ModAttachments;
import com.abyssredemption.daomod.registry.ModBlocks;
import com.abyssredemption.daomod.registry.ModEntities;
import com.abyssredemption.daomod.registry.ModFeatures;
import com.abyssredemption.daomod.world.DimensionKeys;
import com.abyssredemption.daomod.world.DimensionRules;
import com.abyssredemption.daomod.worldgen.SectOutpostFeature;
import com.mojang.serialization.JsonOps;
import io.netty.buffer.Unpooled;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

@GameTestHolder(AbsDaoMod.MODID)
@PrefixGameTestTemplate(false)
public final class DaoGameTests {
    @GameTest(template = "empty", timeoutTicks = 20)
    public static void cultivationProgression(GameTestHelper helper) {
        CultivationData data = new CultivationData(0, 0, 0, 8, 99);
        data.setRealmProgress(100);
        require(helper, data.getRealm() == 1 && data.getStage() == 0 && data.getRealmProgress() == 0,
                "Progress must cross a major realm boundary");

        data.setRealm(8);
        data.setStage(8);
        data.setRealmProgress(100);
        require(helper, data.getRealmProgress() == 99, "Final realm progress must stay capped");
        require(helper, CultivationData.getMaxQi(8, 8) == 9_000_000_000L,
                "Final realm qi cap must use long precision");
        helper.succeed();
    }

    @GameTest(template = "empty", timeoutTicks = 20)
    public static void cultivationPayloadRoundTrip(GameTestHelper helper) {
        CultivationPayload original = new CultivationPayload(8, 8_765_432_109L, 4, 7, 99, 321, 3);
        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        try {
            CultivationPayload.STREAM_CODEC.encode(buffer, original);
            CultivationPayload decoded = CultivationPayload.STREAM_CODEC.decode(buffer);
            require(helper, original.equals(decoded), "Cultivation payload must preserve every multiplayer field");
            helper.succeed();
        } finally {
            buffer.release();
        }
    }

    @GameTest(template = "empty", timeoutTicks = 20)
    public static void cultivationSaveRoundTrip(GameTestHelper helper) {
        var original = new CultivationData(8, 8_765_432_109L, 4, 7, 99, 321, 3);
        var encoded = CultivationData.CODEC.encodeStart(JsonOps.INSTANCE, original).getOrThrow();
        var decoded = CultivationData.CODEC.parse(JsonOps.INSTANCE, encoded).getOrThrow();
        require(helper, decoded.getRealm() == 8 && decoded.getQi() == 8_765_432_109L
                        && decoded.getSectOrthodoxy() == 4 && decoded.getStage() == 7
                        && decoded.getRealmProgress() == 99 && decoded.getKarma() == 321 && decoded.getSect() == 3,
                "Cultivation save codec must preserve every persistent multiplayer field");
        require(helper, NeoForgeRegistries.ATTACHMENT_TYPES.getKey(ModAttachments.CULTIVATION.get())
                        .getNamespace().equals(AbsDaoMod.MODID),
                "Cultivation attachment must use the mod namespace");
        helper.succeed();
    }

    @GameTest(template = "empty", timeoutTicks = 20)
    public static void allHostileEntitiesCreate(GameTestHelper helper) {
        Set<Integer> sects = new HashSet<>();
        Set<Integer> legends = new HashSet<>();
        int regionalBosses = 0;
        try {
            for (var field : ModEntities.class.getFields()) {
                if (!(field.get(null) instanceof DeferredHolder<?, ?> holder)
                        || !(holder.get() instanceof EntityType<?> type)) continue;
                Entity entity = type.create(helper.getLevel());
                if (entity instanceof SectGuardianEntity guardian) {
                    sects.add(guardian.getSect());
                    require(helper, guardian.getMaxHealth() > 0, "Guardian must have health attributes");
                } else if (entity instanceof LegendaryCultivatorEntity legend) {
                    legends.add(legend.getLegend());
                    require(helper, legend.getMaxHealth() == LegendaryCultivatorEntity.maxHealthForLegend(
                                    legend.getLegend()) || entity instanceof com.abyssredemption.daomod.entity.DaoRegionalBossEntity,
                            "Legend health must match its challenge realm");
                    if (entity instanceof DaoRegionalBossEntity regional) {
                        regionalBosses++;
                        require(helper, regional.getVariant() >= 1 && regional.getVariant() <= 7,
                                "Regional boss variant must stay in the documented range");
                        require(helper, !regional.advancementId().isBlank(),
                                "Regional boss must expose an advancement id");
                    }
                }
            }
        } catch (IllegalAccessException exception) {
            helper.fail("Could not inspect hostile entity registry: " + exception.getMessage());
        }
        require(helper, sects.equals(Set.of(1, 2, 3, 4)), "All four sect guardians must create uniquely");
        require(helper, legends.size() == 25 && legends.contains(1) && legends.contains(25),
                "All twenty-five legendary enemies must create uniquely");
        require(helper, regionalBosses == 7, "All seven regional bosses must create");
        require(helper, LegendaryCultivatorEntity.maxHealthForLegend(1) == 240.0
                        && LegendaryCultivatorEntity.maxHealthForLegend(25) == 680.0,
                "Legend health must rise across challenge groups");
        require(helper, LegendaryCultivatorEntity.regionalAdvancementForLegend(2).equals("defeat_sword_remnant")
                        && LegendaryCultivatorEntity.regionalAdvancementForLegend(18).equals("defeat_leize_jiao")
                        && LegendaryCultivatorEntity.regionalAdvancementForLegend(25) == null,
                "Regional boss advancements must stay bound to their legend stand-ins");
        helper.succeed();
    }

    @GameTest(template = "empty", timeoutTicks = 20)
    public static void bossStructurePlacesSealAndReward(GameTestHelper helper) {
        BlockPos origin = helper.absolutePos(new BlockPos(12, 2, 12));
        boolean placed = ModFeatures.BOSS_STRUCTURE.get().place(new FeaturePlaceContext<>(Optional.empty(),
                helper.getLevel(), helper.getLevel().getChunkSource().getGenerator(), RandomSource.create(7), origin,
                new com.abyssredemption.daomod.worldgen.boss.BossStructureConfiguration(2, 12, true)));
        require(helper, placed, "Boss structure must place successfully");
        require(helper, helper.getLevel().getBlockState(origin.below().offset(0, 1, 0)).is(ModBlocks.BOSS_SEAL.get()),
                "Boss structure must contain a seal block");
        require(helper, !helper.getLevel().getBlockState(origin.below().offset(0, 1, 0)).getValue(
                        net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED),
                "Boss seal must start inactive");
        require(helper, helper.getLevel().getBlockEntity(origin.below().offset(0, 1, -8)) instanceof Container,
                "Boss structure must contain a reward barrel");
        helper.succeed();
    }

    @GameTest(template = "empty", timeoutTicks = 20)
    public static void sectOutpostContainsTreasure(GameTestHelper helper) {
        BlockPos origin = helper.absolutePos(new BlockPos(8, 2, 8));
        SectOutpostFeature feature = new SectOutpostFeature(NoneFeatureConfiguration.CODEC, 1);
        boolean placed = feature.place(new FeaturePlaceContext<>(Optional.empty(), helper.getLevel(),
                helper.getLevel().getChunkSource().getGenerator(), RandomSource.create(42), origin,
                NoneFeatureConfiguration.INSTANCE));
        require(helper, placed, "Sect outpost must place successfully");
        require(helper, helper.getLevel().getBlockEntity(origin.offset(0, 0, 2)) instanceof Container container
                        && !container.getItem(0).isEmpty()
                        && !container.getItem(1).isEmpty()
                        && !container.getItem(2).isEmpty(),
                "Sect treasure must contain an artifact, herb, and ore");
        helper.succeed();
    }

    @GameTest(template = "empty", timeoutTicks = 20)
    public static void allCodexResourcesHaveWorldBlocks(GameTestHelper helper) {
        require(helper, ModBlocks.CODEX_RESOURCE_BLOCKS.size() == 42,
                "All twenty-two herbs and twenty ores must have world blocks");
        require(helper, ModBlocks.CODEX_RESOURCE_BLOCKS.values().stream().map(DeferredHolder::get).distinct().count() == 42,
                "Every codex resource must use a distinct registered block");
        helper.succeed();
    }

    @GameTest(template = "empty", timeoutTicks = 20)
    public static void settingWorldRegionsAreRegistered(GameTestHelper helper) {
        var registries = helper.getLevel().registryAccess();
        var biomes = registries.registryOrThrow(Registries.BIOME);
        for (String region : Set.of("zhongtian_shentu", "donghai_leize", "xiji_jinge", "nanjiang_huoyu",
                "beiji_minghai", "guixu_shenhai", "xukong_tianwai")) {
            require(helper, biomes.containsKey(id(region)), "Missing setting biome: " + region);
        }
        helper.succeed();
    }

    @GameTest(template = "empty", timeoutTicks = 20)
    public static void dimensionRulesMatchCultivationLevels(GameTestHelper helper) {
        require(helper, DimensionKeys.cultivationLevel(0, 0) == 1, "First stage must be level 1");
        require(helper, DimensionKeys.cultivationLevel(8, 8) == 81, "Final stage must be level 81");
        require(helper, DimensionRules.ruleFor(DimensionKeys.LINGXU).minLevel() == 14,
                "Lingxu must require foundation mid-stage");
        require(helper, DimensionRules.ruleFor(DimensionKeys.DILUOYUAN).cultivationMultiplier() == 4.5,
                "Diluo Abyss must be a high-risk cultivation realm");
        require(helper, DimensionRules.ruleFor(DimensionKeys.LINGXU_LEGACY).minLevel()
                        == DimensionRules.ruleFor(DimensionKeys.LINGXU).minLevel(),
                "Legacy Lingxu id must keep the same pressure rules");
        helper.succeed();
    }

    @GameTest(template = "empty", timeoutTicks = 20)
    public static void swordBeamKeepsPlayerAttribution(GameTestHelper helper) {
        var player = helper.makeMockPlayer(GameType.SURVIVAL);
        var beam = new SwordBeamEntity(ModEntities.SWORD_BEAM.get(), player, 0, 0, 1,
                helper.getLevel(), 8.0f);
        require(helper, beam.swordDamageSource().getEntity() == player,
                "Sword beam damage must remain attributed to its player for multiplayer rewards");
        helper.succeed();
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(AbsDaoMod.MODID, path);
    }

    private static void require(GameTestHelper helper, boolean condition, String message) {
        if (!condition) helper.fail(message);
    }

    private DaoGameTests() {
    }
}
