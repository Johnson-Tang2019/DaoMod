package com.abyssredemption.daomod.block;

import com.abyssredemption.daomod.AbsDaoMod;
import com.abyssredemption.daomod.entity.DaoRegionalBossEntity;
import com.abyssredemption.daomod.registry.ModAttachments;
import com.abyssredemption.daomod.registry.ModEntities;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class BossSealBlock extends Block {
    public BossSealBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED, false));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
                                               BlockHitResult hit) {
        if (level.isClientSide) return InteractionResult.SUCCESS;
        if (state.getValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED)) {
            player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.boss_seal_spent")
                    .withStyle(ChatFormatting.GRAY), true);
            return InteractionResult.FAIL;
        }
        int variant = variantFor(level, pos);
        if (player.getData(ModAttachments.CULTIVATION).getRealm() < DaoRegionalBossEntity.requiredRealm(variant)) {
            player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.boss_seal_realm_low")
                    .withStyle(ChatFormatting.RED), true);
            return InteractionResult.FAIL;
        }
        if (!(level instanceof ServerLevel serverLevel) || !(player instanceof ServerPlayer)) {
            return InteractionResult.FAIL;
        }
        DaoRegionalBossEntity boss = typeFor(variant).create(serverLevel);
        if (boss == null) return InteractionResult.FAIL;
        boss.moveTo(pos.getX() + 0.5, pos.getY() + 2.0, pos.getZ() + 0.5, player.getYRot() + 180.0f, 0.0f);
        boss.setTarget(player);
        serverLevel.addFreshEntity(boss);
        serverLevel.playSound(null, pos, SoundEvents.END_PORTAL_SPAWN, SoundSource.HOSTILE, 1.0f, 0.8f);
        level.setBlock(pos, state.setValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED, true),
                Block.UPDATE_CLIENTS);
        player.displayClientMessage(Component.translatable("message.abyssredemptiondaomod.boss_seal_opened",
                boss.getDisplayName()).withStyle(ChatFormatting.GOLD), false);
        return InteractionResult.CONSUME;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED, false);
    }

    @Override
    protected void createBlockStateDefinition(net.minecraft.world.level.block.state.StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED);
    }

    private static int variantFor(Level level, BlockPos pos) {
        ResourceLocation biome = level.getBiome(pos).unwrapKey()
                .map(key -> key.location()).orElse(ResourceLocation.fromNamespaceAndPath(AbsDaoMod.MODID, "zhongtian_shentu"));
        return switch (biome.getPath()) {
            case "donghai_leize" -> 2;
            case "xiji_jinge" -> 3;
            case "nanjiang_huoyu" -> 4;
            case "beiji_minghai" -> 5;
            case "guixu_shenhai" -> 6;
            case "xukong_tianwai" -> 7;
            default -> 1;
        };
    }

    private static EntityType<DaoRegionalBossEntity> typeFor(int variant) {
        return switch (variant) {
            case 2 -> ModEntities.LEIZE_ANCIENT_DRAGON.get();
            case 3 -> ModEntities.SWORD_TOMB_REMNANT.get();
            case 4 -> ModEntities.FIRE_MANDRILL_KING.get();
            case 5 -> ModEntities.MINGHAI_SOUL_FERRY_MONK.get();
            case 6 -> ModEntities.GUIXU_MERFOLK_EMPEROR_ECHO.get();
            case 7 -> ModEntities.VOID_DEMON_LARVA.get();
            default -> ModEntities.SHENTU_STELE_SPIRIT.get();
        };
    }
}
