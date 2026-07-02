package com.abyssredemption.daomod.network;

import com.abyssredemption.daomod.AbsDaoMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record CultivationPayload(int realm, long qi, int sectOrthodoxy, int stage, int realmProgress, int karma,
                                 int sect) implements CustomPacketPayload {
    public static final Type<CultivationPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(AbsDaoMod.MODID, "cultivation_sync"));

    public static final StreamCodec<FriendlyByteBuf, CultivationPayload> STREAM_CODEC = StreamCodec.of(
            (buffer, payload) -> {
                buffer.writeVarInt(payload.realm());
                buffer.writeVarLong(payload.qi());
                buffer.writeVarInt(payload.sectOrthodoxy());
                buffer.writeVarInt(payload.stage());
                buffer.writeVarInt(payload.realmProgress());
                buffer.writeVarInt(payload.karma());
                buffer.writeVarInt(payload.sect());
            },
            buffer -> new CultivationPayload(
                    buffer.readVarInt(),
                    buffer.readVarLong(),
                    buffer.readVarInt(),
                    buffer.readVarInt(),
                    buffer.readVarInt(),
                    buffer.readVarInt(),
                    buffer.readVarInt())
    );

    public CultivationPayload(int realm, long qi, int sectOrthodoxy, int stage, int realmProgress) {
        this(realm, qi, sectOrthodoxy, stage, realmProgress, 0, 0);
    }

    public CultivationPayload(int realm, long qi, int sectOrthodoxy, int stage, int realmProgress, int karma) {
        this(realm, qi, sectOrthodoxy, stage, realmProgress, karma, 0);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}


