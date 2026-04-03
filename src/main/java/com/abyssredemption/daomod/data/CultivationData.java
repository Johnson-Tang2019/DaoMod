package com.abyssredemption.daomod.data;

import net.minecraft.nbt.CompoundTag;

public class CultivationData {

    private int realm = 0;
    private int qi = 0;

    public int getRealm() {
        return realm;
    }

    public void setRealm(int realm) {
        this.realm = realm;
    }

    public int getQi() {
        return qi;
    }

    public void setQi(int qi) {
        this.qi = qi;
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("realm", realm);
        tag.putInt("qi", qi);
        return tag;
    }

    public void deserializeNBT(CompoundTag tag) {
        realm = tag.getInt("realm");
        qi = tag.getInt("qi");
    }

}
