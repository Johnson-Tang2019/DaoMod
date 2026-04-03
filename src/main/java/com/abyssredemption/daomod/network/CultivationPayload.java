package com.abyssredemption.daomod.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record CultivationPayload(int realm, double qi, int sectOrthodoxy) implements CustomPacketPayload {
    public static final Type<CultivationPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("daomod", "cultivation_sync"));

    public static final StreamCodec<FriendlyByteBuf, CultivationPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, CultivationPayload::realm,
            ByteBufCodecs.DOUBLE, CultivationPayload::qi,
            ByteBufCodecs.VAR_INT, CultivationPayload::sectOrthodoxy,
            CultivationPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
