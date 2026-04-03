package com.abyssredemption.daomod.event;

import com.yourname.xiu_mod.capability.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.AttachCapabilitiesEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@Mod.EventBusSubscriber(modid = "xiu_mod")
public class ModEvents {


    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(
                    new ResourceLocation("xiu_mod", "cultivation"),
                    new CultivationProvider()
            );
        }
    }

    @SubscribeEvent
    public static void clonePlayer(PlayerEvent.Clone event) {
        event.getOriginal().getCapability(ModCapabilities.CULTIVATION).ifPresent(oldData -> {
            event.getEntity().getCapability(ModCapabilities.CULTIVATION).ifPresent(newData -> {
                newData.setRealm(oldData.getRealm());
                newData.setQi(oldData.getQi());
            });
        });
    }
}
