package com.abyssredemption.daomod.capability;

import net.neoforged.neoforge.capabilities.Capability;
import net.neoforged.neoforge.capabilities.CapabilityManager;
import net.neoforged.neoforge.capabilities.CapabilityToken;

public class ModCapabilities {

    public static final Capability<ICultivation> CULTIVATION =
            CapabilityManager.get(new CapabilityToken<>() {});

}
