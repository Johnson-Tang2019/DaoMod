package com.abyssredemption.daomod.capability;

import java.io.Serializable;

public class Cultivation implements ICultivation{

    private int realm = 0;
    private int qi = 0;

    @Override
    public int getRealm() {
        return realm;
    }

    @Override
    public void setRealm(int level) {
        this.realm = level;
    }

    @Override
    public int getQi() {
        return qi;
    }

    @Override
    public void setQi(int qi) {
        this.qi = qi;
    }
}
