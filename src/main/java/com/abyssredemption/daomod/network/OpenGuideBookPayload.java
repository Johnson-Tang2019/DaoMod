package com.abyssredemption.daomod.network;

import com.abyssredemption.daomod.AbsDaoMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record OpenGuideBookPayload() implements CustomPacketPayload {
    public static final OpenGuideBookPayload INSTANCE = new OpenGuideBookPayload();
    public static final Type<OpenGuideBookPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(AbsDaoMod.MODID, "open_guide_book"));
    public static final StreamCodec<FriendlyByteBuf, OpenGuideBookPayload> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
