package com.abyssredemption.daomod.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class CultivationData {
    private int realm;
    private long qi;
    private int sectOrthodoxy;
    private int stage;
    private int realmProgress;

    public CultivationData(int realm, long qi, int sectOrthodoxy, int stage, int realmProgress) {
        this.realm = realm;
        this.qi = qi;
        this.sectOrthodoxy = sectOrthodoxy;
        this.stage = stage;
        this.realmProgress = realmProgress;
    }

    public static final Codec<CultivationData> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.INT.fieldOf("realm").forGetter(CultivationData::getRealm),
                    Codec.LONG.fieldOf("qi").forGetter(CultivationData::getQi),
                    Codec.INT.fieldOf("sect_orthodoxy").forGetter(CultivationData::getSectOrthodoxy),
                    Codec.INT.fieldOf("stage").forGetter(CultivationData::getStage),
                    Codec.INT.fieldOf("realm_progress").forGetter(CultivationData::getRealmProgress)
            ).apply(instance, CultivationData::new)
    );

    public int getRealm() { return realm; }
    public void setRealm(int realm) {
        if (realm > 8) {
            realm = 8;
        }
        this.realm = realm;
    }

    public long getQi() { return qi; }
    public void setQi(long qi) { this.qi = qi; }

    public int getSectOrthodoxy() { return sectOrthodoxy; }
    public void setSectOrthodoxy(int sectOrthodoxy) { this.sectOrthodoxy = sectOrthodoxy; }

    public int getStage() { return stage; }
    public void setStage(int stage) {
        if (stage > 8) {
            stage = 0;
            setRealm(getRealm() + 1);
        }

        this.stage = stage;
    }

    public int getRealmProgress() { return realmProgress; }
    public void setRealmProgress(int realmProgress) {
        if (realmProgress > 99) {
            realmProgress = 0;
            setStage(getStage() + 1);
        }
        this.realmProgress = realmProgress;
    }

    public static long getMaxQi(int realm, int stage) {
        return (long) Math.pow(10, realm + 1) * (stage + 1);
    }
}