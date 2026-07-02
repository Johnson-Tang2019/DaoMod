package com.abyssredemption.daomod.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class CultivationData {
    private int realm;
    private long qi;
    private int sectOrthodoxy;
    private int stage;
    private int realmProgress;
    private int karma;
    private int sect;

    public CultivationData(int realm, long qi, int sectOrthodoxy, int stage, int realmProgress) {
        this(realm, qi, sectOrthodoxy, stage, realmProgress, 0);
    }

    public CultivationData(int realm, long qi, int sectOrthodoxy, int stage, int realmProgress, int karma) {
        this(realm, qi, sectOrthodoxy, stage, realmProgress, karma, 0);
    }

    public CultivationData(int realm, long qi, int sectOrthodoxy, int stage, int realmProgress, int karma, int sect) {
        this.realm = realm;
        this.qi = qi;
        this.sectOrthodoxy = sectOrthodoxy;
        this.stage = stage;
        this.realmProgress = realmProgress;
        this.karma = karma;
        this.sect = sect;
    }

    public static final Codec<CultivationData> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.INT.fieldOf("realm").forGetter(CultivationData::getRealm),
                    Codec.LONG.fieldOf("qi").forGetter(CultivationData::getQi),
                    Codec.INT.fieldOf("sect_orthodoxy").forGetter(CultivationData::getSectOrthodoxy),
                    Codec.INT.fieldOf("stage").forGetter(CultivationData::getStage),
                    Codec.INT.fieldOf("realm_progress").forGetter(CultivationData::getRealmProgress),
                    Codec.INT.optionalFieldOf("karma", 0).forGetter(CultivationData::getKarma),
                    Codec.INT.optionalFieldOf("sect", 0).forGetter(CultivationData::getSect)
            ).apply(instance, CultivationData::new)
    );

    public int getRealm() { return realm; }
    public void setRealm(int realm) {
        this.realm = Math.max(0, Math.min(8, realm));
    }

    public long getQi() { return qi; }
    public void setQi(long qi) { this.qi = qi; }

    public int getSectOrthodoxy() { return sectOrthodoxy; }
    public void setSectOrthodoxy(int sectOrthodoxy) { this.sectOrthodoxy = sectOrthodoxy; }

    public int getStage() { return stage; }
    public void setStage(int stage) {
        int normalized = Math.max(0, stage);
        while (normalized > 8 && realm < 8) {
            normalized -= 9;
            setRealm(realm + 1);
        }
        this.stage = realm >= 8 ? Math.min(8, normalized) : normalized;
    }

    public int getRealmProgress() { return realmProgress; }
    public void setRealmProgress(int realmProgress) {
        int remaining = Math.max(0, realmProgress);
        while (remaining >= 100) {
            if (realm >= 8 && stage >= 8) {
                remaining = 99;
                break;
            }
            remaining -= 100;
            setStage(stage + 1);
        }
        this.realmProgress = remaining;
    }

    public int getKarma() { return karma; }
    public int getSect() { return sect; }
    public void setSect(int sect) { this.sect = Math.max(0, Math.min(4, sect)); }
    public void addKarma(int amount) {
        if (amount <= 0) {
            return;
        }
        this.karma = Math.min(Integer.MAX_VALUE, this.karma + amount);
    }

    public static long getMaxQi(int realm, int stage) {
        return (long) Math.pow(10, realm + 1) * (stage + 1);
    }
}
