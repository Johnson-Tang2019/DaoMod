package com.abyssredemption.daomod.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record CultivationData(int realm, double qi, int sectOrthodoxy) {

    public static final Codec<CultivationData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("realm").forGetter(CultivationData::realm),
                    Codec.DOUBLE.fieldOf("qi").forGetter(CultivationData::qi),
                    Codec.INT.fieldOf("sect orthodoxy").forGetter(CultivationData::sectOrthodoxy)
            ).apply(instance, CultivationData::new)
    );

    public static final CultivationData DEFAULT = new CultivationData(0, 0, 0);

}
