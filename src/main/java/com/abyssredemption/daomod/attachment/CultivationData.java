package com.abyssredemption.daomod.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class CultivationData {
    private int realm;
    private long qi;
    private int sectOrthodoxy;
    private int stage;

    public CultivationData(int realm, long qi, int sectOrthodoxy, int stage) {
        this.realm = realm;
        this.qi = qi;
        this.sectOrthodoxy = sectOrthodoxy;
        this.stage = stage;
    }

    public static final Codec<CultivationData> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.INT.fieldOf("realm").forGetter(CultivationData::getRealm),
                    Codec.LONG.fieldOf("qi").forGetter(CultivationData::getQi),
                    Codec.INT.fieldOf("sect_orthodoxy").forGetter(CultivationData::getSectOrthodoxy),
                    Codec.INT.fieldOf("stage").forGetter(CultivationData::getStage)
            ).apply(instance, CultivationData::new)
    );

    public int getRealm() { return realm; }
    public void setRealm(int realm) { this.realm = realm; }

    public long getQi() { return qi; }
    public void setQi(long qi) { this.qi = qi; }

    public int getSectOrthodoxy() { return sectOrthodoxy; }
    public void setSectOrthodoxy(int sectOrthodoxy) { this.sectOrthodoxy = sectOrthodoxy; }

    public int getStage() { return stage; }
    public void setStage(int stage) { this.stage = stage; }

    public static long getMaxQi(int realm, int stage) {
        return (long) Math.pow(10, realm + 1) * (stage + 1);
    }
}