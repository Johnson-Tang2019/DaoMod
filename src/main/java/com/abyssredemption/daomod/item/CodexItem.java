package com.abyssredemption.daomod.item;

import com.abyssredemption.daomod.AbsDaoMod;
import com.abyssredemption.daomod.network.CultivationPayload;
import com.abyssredemption.daomod.registry.ModAttachments;
import com.abyssredemption.daomod.registry.ModBlocks;
import java.util.List;
import java.util.Set;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;

public class CodexItem extends Item {
    private static final ResourceKey<Level> LINGXU_REALM = dimension("lingxu_realm");
    private static final ResourceKey<Level> DILUO_ABYSS = dimension("diluo_abyss");
    private static final ResourceKey<Level> ETERNAL_IMMORTAL_REALM = dimension("eternal_immortal_realm");
    private final String categoryKey;
    private final String rankKey;
    private final String abilityId;

    public CodexItem(Properties properties, String categoryKey, String rankKey, String abilityId) {
        super(properties);
        this.categoryKey = categoryKey;
        this.rankKey = rankKey;
        this.abilityId = abilityId;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents,
                                TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.abyssredemptiondaomod.category", Component.translatable(categoryKey))
                .withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable("tooltip.abyssredemptiondaomod.rank", Component.translatable(rankKey))
                .withStyle(ChatFormatting.AQUA));
        if (isArtifact() && requiredRealm() > 0) {
            tooltipComponents.add(Component.translatable("tooltip.abyssredemptiondaomod.required_realm",
                    Component.translatable("gui.daomod.realm" + requiredRealm())).withStyle(ChatFormatting.RED));
        }
        if (isArtifact()) {
            tooltipComponents.add(Component.translatable("tooltip.abyssredemptiondaomod.qi_cost", qiCost())
                    .withStyle(ChatFormatting.BLUE));
        }
        tooltipComponents.add(Component.translatable(stack.getDescriptionId() + ".lore")
                .withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!canControl(player)) {
            if (!level.isClientSide) {
                player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.realm_too_low",
                        Component.translatable("gui.daomod.realm" + requiredRealm())), true);
            }
            return InteractionResultHolder.fail(stack);
        }
        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.fail(stack);
        }
        if (level.isClientSide) {
            return InteractionResultHolder.success(stack);
        }
        if (!hasEnoughQi(player)) {
            player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.qi_too_low", qiCost()), true);
            return InteractionResultHolder.fail(stack);
        }

        boolean activated = switch (abilityId) {
            case "bishui_xichanzhu" -> applyWaterWard(player);
            case "zhiyuan_feixingnu" -> fireMeteorBolt(level, player);
            case "naying_ping" -> concealPlayer(player);
            case "ganshan_bian" -> driveBackEnemies(level, player);
            case "cangjing_ye" -> studyScripture(player);
            case "zhaodan_jing" -> revealNearby(level, player);
            case "shanhe_cuo" -> suppressWithMountain(level, player);
            case "lihuo_fentian_ling" -> kindleEarthFire(level, player);
            case "fuyu_fanyun_gai" -> summonRain(level, player);
            case "fulong_jinsuo" -> bindDragon(level, player);
            case "chuanxiao_suo" -> pierceTheSky(player);
            case "zhenmo_yin" -> soundDemonQuelling(level, player);
            case "juling_wanqi_zhen" -> raiseSpiritField(level, player);
            case "yinyang_liangjie_bei" -> turnLifeFace(player);
            case "wending_laoxiu" -> reforgeEquipment(player);
            case "fentian_zhulong" -> scorchDomain(level, player);
            case "jiushi_cihang" -> invokeMercy(level, player);
            case "zhaohun_qianmian" -> defileMinds(level, player);
            case "duntian_wuxing" -> hideFromHeaven(player);
            case "xiangmo_numu" -> shatterRules(level, player);
            case "nongchao_yiyin" -> banishWithTide(level, player);
            case "lietian_bengshan" -> crushWithStarWeight(level, player);
            case "suanjin_shensuan" -> calculateWeakness(level, player);
            case "wuxing_dingshi_ta" -> commandFiveElements(level, player);
            case "suihan_kurong_zhong" -> freezeEpoch(level, player);
            case "zhutian_xingxiu_tu" -> shiftStars(level, player);
            case "hunyuan_qiankun_san" -> reverseCausality(player);
            case "fantian_fudi_yin" -> commandGravity(level, player);
            case "qibao_qingjing_shu" -> purifyAllArts(level, player);
            case "yinyang_chongsu_lu" -> reshapeLife(player);
            case "miedu_honghulu" -> invokeTrueName(level, player);
            case "guixu_dinghai_zhu" -> stabilizeChaos(player);
            case "suiyue_liuzhuan_lun" -> reverseTimeField(level, player);
            case "wanfa_benyuan_shengjuan" -> invokeOriginArts(player);
            case "lunhui_pan" -> turnReincarnation(level, player);
            case "geshi_zhimen" -> crossWorldGate(level, player);
            case "hundun_zhenjie_zhong" -> shakeWorld(level, player);
            case "zhongsheng_yinguo_jitan" -> sacrificeKarma(level, player);
            case "xukong_dingxing_mao" -> anchorVoid(level, player);
            case "tiandao_caijue_chui" -> judgeByHeaven(level, player);
            case "hongmeng_chuangshi_lian" -> bloomCreation(level, player);
            case "juling_cao", "huoxue_teng", "yingguang_tai", "ningqi_hua", "bihan_cao", "shexian_guo",
                 "tiegu_hua", "zuixian_tao", "bihuo_shenzao", "qianji_yin", "jianyi_cao", "tunling_teng",
                 "qiqiao_linglong_guo", "kurong_shen", "jihan_binglian", "niepan_hua", "minghe_duya_hua",
                 "yinyang_nizhuan_cao", "wudao_cha", "zaohua_qinglian", "sansheng_shishang_hua",
                 "hundun_qingti" -> consumeSpiritHerb(player, stack);
            default -> false;
        };
        if (!activated) {
            return InteractionResultHolder.pass(stack);
        }
        consumeQi(player);
        int cooldown = rankKey.endsWith("divine") ? 20 * 60
                : rankKey.endsWith("ancient") ? 20 * 30
                : rankKey.endsWith("heaven") ? 20 * 20 : 20 * 8;
        player.getCooldowns().addCooldown(this, cooldown);
        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        var holder = ModBlocks.CODEX_RESOURCE_BLOCKS.get(abilityId);
        if (holder == null) {
            return super.useOn(context);
        }
        var level = context.getLevel();
        var target = context.getClickedPos().relative(context.getClickedFace());
        var state = holder.get().defaultBlockState();
        if (!level.getBlockState(target).canBeReplaced() || !state.canSurvive(level, target)) {
            return InteractionResult.FAIL;
        }
        if (!level.isClientSide) {
            level.setBlock(target, state, 11);
            if (context.getPlayer() == null || !context.getPlayer().getAbilities().instabuild) {
                context.getItemInHand().shrink(1);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target,
                                                   InteractionHand hand) {
        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResult.PASS;
        }
        if (!canControl(player)) {
            if (!player.level().isClientSide) {
                player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.realm_too_low",
                        Component.translatable("gui.daomod.realm" + requiredRealm())), true);
            }
            return InteractionResult.FAIL;
        }
        if (abilityId.equals("yinyang_liangjie_bei") && !player.isShiftKeyDown()) {
            if (!player.level().isClientSide) {
                if (!hasEnoughQi(player)) {
                    player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.qi_too_low", qiCost()), true);
                    return InteractionResult.FAIL;
                }
                target.hurt(player.level().damageSources().playerAttack(player), 5.0f);
                target.addEffect(new MobEffectInstance(MobEffects.WITHER, 20 * 5, 0));
                player.getCooldowns().addCooldown(this, 20 * 12);
                consumeQi(player);
                player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.yinyang_death"), true);
            }
            return InteractionResult.sidedSuccess(player.level().isClientSide);
        }
        if (!abilityId.equals("jieling_suo")) {
            return InteractionResult.PASS;
        }
        if (!player.level().isClientSide) {
            if (!hasEnoughQi(player)) {
                player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.qi_too_low", qiCost()), true);
                return InteractionResult.FAIL;
            }
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20 * 8, 3));
            target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 20 * 8, 1));
            player.getCooldowns().addCooldown(this, 20 * 12);
            consumeQi(player);
            player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.jieling_suo"), true);
        }
        return InteractionResult.sidedSuccess(player.level().isClientSide);
    }

    private boolean applyWaterWard(Player player) {
        player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 20 * 60, 0));
        player.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 20 * 30, 0));
        player.clearFire();
        player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.bishui_xichanzhu"), true);
        return true;
    }

    private boolean concealPlayer(Player player) {
        player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 20 * 20, 0));
        player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.naying_ping"), true);
        return true;
    }

    private boolean studyScripture(Player player) {
        player.giveExperiencePoints(5);
        player.addEffect(new MobEffectInstance(MobEffects.LUCK, 20 * 30, 0));
        player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.cangjing_ye"), true);
        return true;
    }

    private boolean fireMeteorBolt(Level level, Player player) {
        Vec3 look = player.getLookAngle().normalize();
        LivingEntity target = level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(20),
                        entity -> entity != player && entity.isAlive())
                .stream()
                .filter(entity -> look.dot(entity.getEyePosition().subtract(player.getEyePosition()).normalize()) > 0.97)
                .min((left, right) -> Double.compare(player.distanceToSqr(left), player.distanceToSqr(right)))
                .orElse(null);
        if (target == null) {
            player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.no_target"), true);
            return false;
        }
        target.hurt(level.damageSources().playerAttack(player), 6.0f);
        target.igniteForSeconds(3);
        player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.zhiyuan_feixingnu"), true);
        return true;
    }

    private boolean driveBackEnemies(Level level, Player player) {
        int affected = 0;
        for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(6),
                entity -> entity != player && entity.isAlive())) {
            Vec3 push = target.position().subtract(player.position()).normalize().scale(1.25).add(0, 0.3, 0);
            target.push(push.x, push.y, push.z);
            target.hurtMarked = true;
            affected++;
        }
        player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.ganshan_bian", affected), true);
        return affected > 0;
    }

    private boolean revealNearby(Level level, Player player) {
        int affected = 0;
        for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(12),
                entity -> entity != player && entity.isAlive())) {
            target.addEffect(new MobEffectInstance(MobEffects.GLOWING, 20 * 15, 0));
            affected++;
        }
        player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.zhaodan_jing", affected), true);
        return true;
    }

    private LivingEntity aimedTarget(Level level, Player player, double range) {
        Vec3 look = player.getLookAngle().normalize();
        return level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(range),
                        entity -> entity != player && entity.isAlive())
                .stream()
                .filter(entity -> look.dot(entity.getEyePosition().subtract(player.getEyePosition()).normalize()) > 0.94)
                .min((left, right) -> Double.compare(player.distanceToSqr(left), player.distanceToSqr(right)))
                .orElse(null);
    }

    private boolean suppressWithMountain(Level level, Player player) {
        LivingEntity target = aimedTarget(level, player, 18);
        if (target == null) return noTarget(player);
        target.hurt(level.damageSources().playerAttack(player), 8.0f);
        target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20 * 6, 4));
        target.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 20 * 6, 2));
        player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.shanhe_cuo"), true);
        return true;
    }

    private boolean kindleEarthFire(Level level, Player player) {
        LivingEntity target = aimedTarget(level, player, 16);
        if (target == null) return noTarget(player);
        target.igniteForSeconds(12);
        target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 20 * 8, 0));
        player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.lihuo_fentian_ling"), true);
        return true;
    }

    private boolean summonRain(Level level, Player player) {
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.setWeatherParameters(0, 20 * 60 * 5, true, false);
        }
        player.clearFire();
        player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 20 * 60, 0));
        player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.fuyu_fanyun_gai"), true);
        return true;
    }

    private boolean bindDragon(Level level, Player player) {
        LivingEntity target = aimedTarget(level, player, 20);
        if (target == null) return noTarget(player);
        double size = target.getBbWidth() * target.getBbHeight();
        int amplifier = Math.min(5, Math.max(1, (int) Math.floor(size)));
        target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20 * 10, amplifier));
        target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 20 * 10, Math.max(0, amplifier - 1)));
        player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.fulong_jinsuo"), true);
        return true;
    }

    private boolean pierceTheSky(Player player) {
        Vec3 motion = player.getLookAngle().normalize().scale(3.2).add(0, 0.35, 0);
        player.setDeltaMovement(motion);
        player.hurtMarked = true;
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20 * 5, 1));
        player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 20 * 8, 0));
        player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.chuanxiao_suo"), true);
        return true;
    }

    private boolean soundDemonQuelling(Level level, Player player) {
        int affected = 0;
        for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(8),
                entity -> entity != player && entity.isAlive())) {
            target.hurt(level.damageSources().playerAttack(player), 5.0f);
            target.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 20 * 6, 0));
            target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 20 * 6, 1));
            affected++;
        }
        player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.zhenmo_yin", affected), true);
        return affected > 0;
    }

    private boolean raiseSpiritField(Level level, Player player) {
        int affected = 0;
        for (Player ally : level.getEntitiesOfClass(Player.class, player.getBoundingBox().inflate(10), Player::isAlive)) {
            ally.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20 * 15, 1));
            ally.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 20 * 30, 1));
            affected++;
        }
        player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.juling_wanqi_zhen", affected), true);
        return true;
    }

    private boolean turnLifeFace(Player player) {
        if (!player.isShiftKeyDown()) {
            player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.yinyang_hint"), true);
            return false;
        }
        player.heal(8.0f);
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20 * 10, 1));
        player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.yinyang_life"), true);
        return true;
    }

    private boolean noTarget(Player player) {
        player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.no_target"), true);
        return false;
    }

    private boolean reforgeEquipment(Player player) {
        ItemStack target = player.getOffhandItem();
        if (!target.isDamageableItem() || !target.isDamaged()) return noTarget(player);
        target.setDamageValue(Math.max(0, target.getDamageValue() - Math.max(50, target.getMaxDamage() / 3)));
        player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.wending_laoxiu"), true);
        return true;
    }

    private boolean scorchDomain(Level level, Player player) {
        int affected = 0;
        for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(12),
                entity -> entity != player && entity.isAlive())) {
            target.hurt(level.damageSources().playerAttack(player), 8.0f);
            target.igniteForSeconds(15);
            affected++;
        }
        player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 20 * 20, 0));
        player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.fentian_zhulong", affected), true);
        return affected > 0;
    }

    private boolean invokeMercy(Level level, Player player) {
        int affected = 0;
        for (Player ally : level.getEntitiesOfClass(Player.class, player.getBoundingBox().inflate(12), Player::isAlive)) {
            ally.heal(10.0f);
            ally.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 20 * 45, 3));
            ally.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20 * 20, 1));
            affected++;
        }
        player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.jiushi_cihang", affected), true);
        return true;
    }

    private boolean defileMinds(Level level, Player player) {
        int affected = 0;
        for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(10),
                entity -> entity != player && entity.isAlive())) {
            target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 20 * 10, 0));
            target.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 20 * 15, 1));
            target.addEffect(new MobEffectInstance(MobEffects.WITHER, 20 * 8, 1));
            affected++;
        }
        player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.zhaohun_qianmian", affected), true);
        return affected > 0;
    }

    private boolean hideFromHeaven(Player player) {
        player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 20 * 45, 0));
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20 * 45, 2));
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20 * 10, 1));
        player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.duntian_wuxing"), true);
        return true;
    }

    private boolean shatterRules(Level level, Player player) {
        LivingEntity target = aimedTarget(level, player, 16);
        if (target == null) return noTarget(player);
        target.removeAllEffects();
        target.hurt(level.damageSources().playerAttack(player), 14.0f);
        player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.xiangmo_numu"), true);
        return true;
    }

    private boolean banishWithTide(Level level, Player player) {
        LivingEntity target = aimedTarget(level, player, 20);
        if (target == null) return noTarget(player);
        Vec3 push = target.position().subtract(player.position()).normalize().scale(4.0).add(0, 1.0, 0);
        target.push(push.x, push.y, push.z);
        target.hurtMarked = true;
        target.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 20 * 3, 1));
        player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.nongchao_yiyin"), true);
        return true;
    }

    private boolean crushWithStarWeight(Level level, Player player) {
        LivingEntity target = aimedTarget(level, player, 14);
        if (target == null) return noTarget(player);
        target.hurt(level.damageSources().playerAttack(player), 20.0f);
        target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20 * 8, 6));
        target.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 20 * 8, 4));
        player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.lietian_bengshan"), true);
        return true;
    }

    private boolean calculateWeakness(Level level, Player player) {
        int affected = 0;
        for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(18),
                entity -> entity != player && entity.isAlive())) {
            target.addEffect(new MobEffectInstance(MobEffects.GLOWING, 20 * 30, 0));
            target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 20 * 30, 2));
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20 * 15, 1));
            affected++;
        }
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 20 * 20, 1));
        player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.suanjin_shensuan", affected), true);
        return true;
    }

    private boolean commandFiveElements(Level level, Player player) {
        int affected = 0;
        for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(14),
                entity -> entity != player && entity.isAlive())) {
            target.removeAllEffects();
            target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 20 * 12, 3));
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20 * 12, 2));
            affected++;
        }
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20 * 20, 2));
        player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 20 * 20, 0));
        player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.wuxing_dingshi_ta", affected), true);
        return true;
    }

    private boolean freezeEpoch(Level level, Player player) {
        int affected = 0;
        for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(12),
                entity -> entity != player && entity.isAlive())) {
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20 * 8, 9));
            target.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 20 * 8, 9));
            target.setTicksFrozen(target.getTicksRequiredToFreeze() + 80);
            affected++;
        }
        player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.suihan_kurong_zhong", affected), true);
        return affected > 0;
    }

    private boolean shiftStars(Level level, Player player) {
        LivingEntity target = aimedTarget(level, player, 24);
        if (target == null) return noTarget(player);
        Vec3 destination = target.position().add(player.getLookAngle().normalize().scale(32)).add(0, 8, 0);
        boolean moved = target.randomTeleport(destination.x, destination.y, destination.z, true);
        if (!moved) return false;
        target.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 20 * 8, 0));
        player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.zhutian_xingxiu_tu"), true);
        return true;
    }

    private boolean reverseCausality(Player player) {
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20 * 20, 3));
        player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 20 * 30, 4));
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 20 * 20, 2));
        player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.hunyuan_qiankun_san"), true);
        return true;
    }

    private boolean commandGravity(Level level, Player player) {
        int affected = 0;
        for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(14),
                entity -> entity != player && entity.isAlive())) {
            target.setDeltaMovement(0, -3.0, 0);
            target.hurtMarked = true;
            target.hurt(level.damageSources().playerAttack(player), 10.0f);
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20 * 10, 5));
            affected++;
        }
        player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.fantian_fudi_yin", affected), true);
        return affected > 0;
    }

    private boolean purifyAllArts(Level level, Player player) {
        int affected = 0;
        for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(15),
                entity -> entity != player && entity.isAlive())) {
            target.removeAllEffects();
            target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 20 * 15, 4));
            affected++;
        }
        player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.qibao_qingjing_shu", affected), true);
        return true;
    }

    private boolean reshapeLife(Player player) {
        player.removeAllEffects();
        player.heal(player.getMaxHealth());
        player.getFoodData().setFoodLevel(20);
        player.getFoodData().setSaturation(20.0f);
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20 * 20, 3));
        player.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, 20 * 60, 2));
        player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.yinyang_chongsu_lu"), true);
        return true;
    }

    private boolean invokeTrueName(Level level, Player player) {
        LivingEntity target = aimedTarget(level, player, 32);
        if (target == null) return noTarget(player);
        target.hurt(level.damageSources().playerAttack(player), 24.0f);
        target.addEffect(new MobEffectInstance(MobEffects.GLOWING, 20 * 20, 0));
        player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.miedu_honghulu"), true);
        return true;
    }

    private boolean stabilizeChaos(Player player) {
        player.removeAllEffects();
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20 * 30, 3));
        player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 20 * 30, 0));
        player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 20 * 30, 0));
        player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 20 * 30, 3));
        player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.guixu_dinghai_zhu"), true);
        return true;
    }

    private boolean reverseTimeField(Level level, Player player) {
        int affected = 0;
        for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(18),
                entity -> entity != player && entity.isAlive())) {
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20 * 12, 9));
            target.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 20 * 12, 9));
            affected++;
        }
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20 * 20, 4));
        player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 20 * 20, 4));
        player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.suiyue_liuzhuan_lun", affected), true);
        return true;
    }

    private boolean invokeOriginArts(Player player) {
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 20 * 30, 3));
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20 * 30, 3));
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20 * 30, 2));
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20 * 30, 2));
        player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 20 * 60, 0));
        player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.wanfa_benyuan_shengjuan"), true);
        return true;
    }

    private boolean turnReincarnation(Level level, Player player) {
        int affected = 0;
        for (Player ally : level.getEntitiesOfClass(Player.class, player.getBoundingBox().inflate(16), Player::isAlive)) {
            ally.removeAllEffects();
            ally.heal(ally.getMaxHealth());
            ally.getFoodData().setFoodLevel(20);
            ally.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20 * 30, 3));
            affected++;
        }
        player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.lunhui_pan", affected), true);
        return true;
    }

    private boolean crossWorldGate(Level level, Player player) {
        if (!(player instanceof ServerPlayer serverPlayer)) return false;
        ResourceKey<Level> targetKey = level.dimension() == Level.OVERWORLD ? LINGXU_REALM
                : level.dimension() == LINGXU_REALM ? DILUO_ABYSS
                : level.dimension() == DILUO_ABYSS ? ETERNAL_IMMORTAL_REALM : Level.OVERWORLD;
        ServerLevel target = serverPlayer.getServer().getLevel(targetKey);
        if (target == null) return false;
        int x = player.blockPosition().getX();
        int z = player.blockPosition().getZ();
        int y = target.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z) + 1;
        if (targetKey == ETERNAL_IMMORTAL_REALM && y <= target.getMinBuildHeight() + 2) {
            x = 0;
            z = 0;
            y = target.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z) + 1;
        }
        y = Math.max(target.getMinBuildHeight() + 2, Math.min(target.getMaxBuildHeight() - 2, y));
        boolean moved = serverPlayer.teleportTo(target, x + 0.5, y, z + 0.5, Set.of(),
                player.getYRot(), player.getXRot());
        if (!moved) return false;
        player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 20 * 10, 0));
        player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.geshi_zhimen",
                Component.translatable("dimension.abyssredemptiondaomod." + targetKey.location().getPath())), true);
        return true;
    }

    private static ResourceKey<Level> dimension(String name) {
        return ResourceKey.create(Registries.DIMENSION,
                ResourceLocation.fromNamespaceAndPath(AbsDaoMod.MODID, name));
    }

    private boolean shakeWorld(Level level, Player player) {
        int affected = 0;
        for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(20),
                entity -> entity != player && entity.isAlive())) {
            target.hurt(level.damageSources().playerAttack(player), 18.0f);
            Vec3 push = target.position().subtract(player.position()).normalize().scale(3.5).add(0, 1.0, 0);
            target.push(push.x, push.y, push.z);
            target.hurtMarked = true;
            affected++;
        }
        player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.hundun_zhenjie_zhong", affected), true);
        return affected > 0;
    }

    private boolean sacrificeKarma(Level level, Player player) {
        if (player.getHealth() <= 8.0f) return false;
        player.hurt(level.damageSources().magic(), 6.0f);
        int affected = 0;
        for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(16),
                entity -> entity != player && entity.isAlive())) {
            target.hurt(level.damageSources().playerAttack(player), 16.0f);
            target.addEffect(new MobEffectInstance(MobEffects.WITHER, 20 * 12, 2));
            affected++;
        }
        player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.zhongsheng_yinguo_jitan", affected), true);
        return affected > 0;
    }

    private boolean anchorVoid(Level level, Player player) {
        int affected = 0;
        for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(20),
                entity -> entity != player && entity.isAlive())) {
            Vec3 pull = player.position().subtract(target.position()).normalize().scale(1.5);
            target.setDeltaMovement(pull.x, 0.1, pull.z);
            target.hurtMarked = true;
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20 * 15, 7));
            affected++;
        }
        player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.xukong_dingxing_mao", affected), true);
        return affected > 0;
    }

    private boolean judgeByHeaven(Level level, Player player) {
        LivingEntity target = aimedTarget(level, player, 32);
        if (target == null) return noTarget(player);
        target.hurt(level.damageSources().playerAttack(player), 30.0f);
        if (level instanceof ServerLevel serverLevel) {
            var lightning = EntityType.LIGHTNING_BOLT.create(serverLevel);
            if (lightning != null) {
                lightning.moveTo(target.position());
                lightning.setCause(player instanceof net.minecraft.server.level.ServerPlayer serverPlayer ? serverPlayer : null);
                serverLevel.addFreshEntity(lightning);
            }
        }
        player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.tiandao_caijue_chui"), true);
        return true;
    }

    private boolean bloomCreation(Level level, Player player) {
        int affected = 0;
        for (Player ally : level.getEntitiesOfClass(Player.class, player.getBoundingBox().inflate(20), Player::isAlive)) {
            ally.heal(ally.getMaxHealth());
            ally.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20 * 60, 4));
            ally.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, 20 * 60, 4));
            ally.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 20 * 60, 4));
            affected++;
        }
        player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.hongmeng_chuangshi_lian", affected), true);
        return true;
    }

    private boolean consumeSpiritHerb(Player player, ItemStack stack) {
        switch (abilityId) {
            case "juling_cao" -> player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20 * 15, 0));
            case "huoxue_teng" -> { player.heal(6.0f); player.removeEffect(MobEffects.POISON); }
            case "yingguang_tai" -> player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 20 * 60 * 3, 0));
            case "ningqi_hua" -> { player.getFoodData().setFoodLevel(Math.min(20, player.getFoodData().getFoodLevel() + 6)); player.giveExperiencePoints(3); }
            case "bihan_cao" -> player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 20 * 60 * 2, 0));
            case "shexian_guo" -> player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 20 * 60, 0));
            case "tiegu_hua" -> player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20 * 60, 1));
            case "zuixian_tao" -> { player.addEffect(new MobEffectInstance(MobEffects.LUCK, 20 * 60 * 2, 2)); player.giveExperiencePoints(12); }
            case "bihuo_shenzao" -> player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 20 * 60 * 5, 0));
            case "qianji_yin" -> player.addEffect(new MobEffectInstance(MobEffects.GLOWING, 20 * 30, 0));
            case "jianyi_cao" -> player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 20 * 60 * 2, 1));
            case "tunling_teng" -> player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 20 * 60 * 2, 2));
            case "qiqiao_linglong_guo" -> { player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 20 * 60 * 3, 2)); player.giveExperiencePoints(30); }
            case "kurong_shen" -> { player.heal(player.getMaxHealth()); player.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, 20 * 60 * 5, 2)); }
            case "jihan_binglian" -> { player.removeEffect(MobEffects.CONFUSION); player.removeEffect(MobEffects.WITHER); player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20 * 60 * 2, 2)); }
            case "niepan_hua" -> { player.heal(player.getMaxHealth()); player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 20 * 60 * 5, 0)); player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20 * 30, 3)); }
            case "minghe_duya_hua" -> { player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 20 * 60, 0)); player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 20 * 60, 0)); }
            case "yinyang_nizhuan_cao" -> { player.removeAllEffects(); player.heal(player.getMaxHealth()); player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 20 * 60 * 3, 3)); }
            case "wudao_cha" -> { player.addEffect(new MobEffectInstance(MobEffects.LUCK, 20 * 60 * 5, 4)); player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 20 * 60 * 5, 3)); player.giveExperiencePoints(80); }
            case "zaohua_qinglian" -> { player.removeAllEffects(); player.heal(player.getMaxHealth()); player.getFoodData().setFoodLevel(20); player.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, 20 * 60 * 8, 4)); }
            case "sansheng_shishang_hua" -> { player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 20 * 60 * 8, 0)); player.addEffect(new MobEffectInstance(MobEffects.LUCK, 20 * 60 * 8, 3)); }
            case "hundun_qingti" -> { player.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, 20 * 60 * 10, 4)); player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20 * 60, 2)); }
            default -> { return false; }
        }
        Component herbName = stack.getHoverName();
        if (!player.getAbilities().instabuild) stack.shrink(1);
        player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.consume_herb", herbName), true);
        return true;
    }

    private boolean isArtifact() {
        return categoryKey.endsWith("artifact");
    }

    private int requiredRealm() {
        if (rankKey.endsWith("divine")) return 8;
        if (rankKey.endsWith("ancient")) return 7;
        if (rankKey.endsWith("heaven")) return 4;
        if (rankKey.endsWith("earth")) return 2;
        return 0;
    }

    private boolean canControl(Player player) {
        return !isArtifact() || player.getAbilities().instabuild
                || player.getData(ModAttachments.CULTIVATION).getRealm() >= requiredRealm();
    }

    private long qiCost() {
        if (rankKey.endsWith("divine")) return 300;
        if (rankKey.endsWith("ancient")) return 150;
        if (rankKey.endsWith("heaven")) return 60;
        if (rankKey.endsWith("earth")) return 20;
        return 5;
    }

    private boolean hasEnoughQi(Player player) {
        return !isArtifact() || player.getAbilities().instabuild
                || player.getData(ModAttachments.CULTIVATION).getQi() >= qiCost();
    }

    private void consumeQi(Player player) {
        if (!isArtifact() || player.getAbilities().instabuild) return;
        var data = player.getData(ModAttachments.CULTIVATION);
        data.setQi(Math.max(0, data.getQi() - qiCost()));
        player.setData(ModAttachments.CULTIVATION, data);
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.connection.send(new CultivationPayload(data.getRealm(), data.getQi(), data.getSectOrthodoxy(),
                    data.getStage(), data.getRealmProgress(), data.getKarma(), data.getSect()));
        }
    }
}
