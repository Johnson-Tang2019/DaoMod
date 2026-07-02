package com.abyssredemption.daomod.client;

import com.abyssredemption.daomod.AbsDaoMod;
import com.abyssredemption.daomod.client.render.LegendaryCultivatorRenderer;
import com.abyssredemption.daomod.client.render.SectGuardianRenderer;
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
        event.registerEntityRenderer(ModEntities.SWORD_ATTENDANT.get(), SectGuardianRenderer::new);
        event.registerEntityRenderer(ModEntities.CREATION_DISCIPLE.get(), SectGuardianRenderer::new);
        event.registerEntityRenderer(ModEntities.THUNDER_MONK.get(), SectGuardianRenderer::new);
        event.registerEntityRenderer(ModEntities.RAKSHASA_CULTIST.get(), SectGuardianRenderer::new);
        event.registerEntityRenderer(ModEntities.XUANYUAN.get(), LegendaryCultivatorRenderer::new);
        event.registerEntityRenderer(ModEntities.DUGU_QIUBAI.get(), LegendaryCultivatorRenderer::new);
        event.registerEntityRenderer(ModEntities.QINGLIAN_JIANXIAN.get(), LegendaryCultivatorRenderer::new);
        event.registerEntityRenderer(ModEntities.YE_GUCHENG.get(), LegendaryCultivatorRenderer::new);
        event.registerEntityRenderer(ModEntities.SHEN_DUZHOU.get(), LegendaryCultivatorRenderer::new);
        event.registerEntityRenderer(ModEntities.NAMELESS_ARTIFICER.get(), LegendaryCultivatorRenderer::new);
        event.registerEntityRenderer(ModEntities.GONGSHU_BAN.get(), LegendaryCultivatorRenderer::new);
        event.registerEntityRenderer(ModEntities.GE_HONG.get(), LegendaryCultivatorRenderer::new);
        event.registerEntityRenderer(ModEntities.OUYE_ZI.get(), LegendaryCultivatorRenderer::new);
        event.registerEntityRenderer(ModEntities.SU_HONGYI.get(), LegendaryCultivatorRenderer::new);
        event.registerEntityRenderer(ModEntities.MING_WANG.get(), LegendaryCultivatorRenderer::new);
        event.registerEntityRenderer(ModEntities.DIZANG.get(), LegendaryCultivatorRenderer::new);
        event.registerEntityRenderer(ModEntities.DAMO.get(), LegendaryCultivatorRenderer::new);
        event.registerEntityRenderer(ModEntities.BLOOD_RIVER_ANCESTOR.get(), LegendaryCultivatorRenderer::new);
        event.registerEntityRenderer(ModEntities.WUHUA.get(), LegendaryCultivatorRenderer::new);
        event.registerEntityRenderer(ModEntities.MO_XUAN.get(), LegendaryCultivatorRenderer::new);
        event.registerEntityRenderer(ModEntities.GUANGHAN_FAIRY.get(), LegendaryCultivatorRenderer::new);
        event.registerEntityRenderer(ModEntities.FENGSHEN_DAOIST.get(), LegendaryCultivatorRenderer::new);
        event.registerEntityRenderer(ModEntities.GRAVE_KEEPER.get(), LegendaryCultivatorRenderer::new);
        event.registerEntityRenderer(ModEntities.NING_FAN.get(), LegendaryCultivatorRenderer::new);
        event.registerEntityRenderer(ModEntities.KUAFU.get(), LegendaryCultivatorRenderer::new);
        event.registerEntityRenderer(ModEntities.JINGWEI.get(), LegendaryCultivatorRenderer::new);
        event.registerEntityRenderer(ModEntities.HOUYI.get(), LegendaryCultivatorRenderer::new);
        event.registerEntityRenderer(ModEntities.XINGTIAN.get(), LegendaryCultivatorRenderer::new);
        event.registerEntityRenderer(ModEntities.XUANYUAN_FOURTEEN.get(), LegendaryCultivatorRenderer::new);
    }
}

