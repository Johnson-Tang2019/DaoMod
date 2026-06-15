package com.abyssredemption.daomod.client;

import com.abyssredemption.daomod.AbsDaoMod;
import com.abyssredemption.daomod.client.render.SwordBeamRenderer;
import com.abyssredemption.daomod.registry.ModEntities;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = AbsDaoMod.MODID, value = Dist.CLIENT)
public class ModClientEvents {
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.SWORD_BEAM.get(), SwordBeamRenderer::new);
    }
}

