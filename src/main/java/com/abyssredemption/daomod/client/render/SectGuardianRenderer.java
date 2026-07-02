package com.abyssredemption.daomod.client.render;

import com.abyssredemption.daomod.AbsDaoMod;
import com.abyssredemption.daomod.entity.SectGuardianEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SectGuardianRenderer extends HumanoidMobRenderer<SectGuardianEntity, HumanoidModel<SectGuardianEntity>> {
    private static final ResourceLocation[] TEXTURES = {
            texture("sword_attendant"),
            texture("sword_attendant"),
            texture("creation_disciple"),
            texture("thunder_monk"),
            texture("rakshasa_cultist")
    };

    public SectGuardianRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.5f);
    }

    private static ResourceLocation texture(String name) {
        return ResourceLocation.fromNamespaceAndPath(AbsDaoMod.MODID, "textures/entity/sect/" + name + ".png");
    }

    @Override
    public ResourceLocation getTextureLocation(SectGuardianEntity entity) {
        return TEXTURES[Math.max(1, Math.min(4, entity.getSect()))];
    }
}
