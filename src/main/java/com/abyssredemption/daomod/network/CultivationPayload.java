package com.abyssredemption.daomod.network;

import com.abyssredemption.daomod.AbsDaoMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record CultivationPayload(int realm, long qi, int sectOrthodoxy, int stage) implements CustomPacketPayload {
    public static final Type<CultivationPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(AbsDaoMod.MODID, "cultivation_sync"));

    public static final StreamCodec<FriendlyByteBuf, CultivationPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, CultivationPayload::realm,
            ByteBufCodecs.VAR_LONG, CultivationPayload::qi,
            ByteBufCodecs.VAR_INT, CultivationPayload::sectOrthodoxy,
            ByteBufCodecs.VAR_INT, CultivationPayload::stage,
            CultivationPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}


