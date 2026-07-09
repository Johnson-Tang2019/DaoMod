package com.abyssredemption.daomod.client.render;

import com.abyssredemption.daomod.AbsDaoMod;
import com.abyssredemption.daomod.client.model.DaoBeastModel;
import com.abyssredemption.daomod.entity.SectGuardianEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SectGuardianRenderer extends MobRenderer<SectGuardianEntity, DaoBeastModel<SectGuardianEntity>> {
    private final DaoBeastModel<SectGuardianEntity>[] models;
    private static final ResourceLocation[] TEXTURES = {
            texture("sword_attendant"),
            texture("sword_attendant"),
            texture("creation_disciple"),
            texture("thunder_monk"),
            texture("rakshasa_cultist")
    };

    public SectGuardianRenderer(EntityRendererProvider.Context context) {
        super(context, new DaoBeastModel<>(context.bakeLayer(DaoBeastModel.layer(0)), 0), 0.7f);
        models = new DaoBeastModel[4];
        for (int i = 0; i < models.length; i++) {
            models[i] = new DaoBeastModel<>(context.bakeLayer(DaoBeastModel.layer(i)), i);
        }
    }

    @Override
    public void render(SectGuardianEntity entity, float yaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffers, int light) {
        model = models[Math.max(0, Math.min(3, entity.getSect() - 1))];
        super.render(entity, yaw, partialTick, poseStack, buffers, light);
    }

    private static ResourceLocation texture(String name) {
        return ResourceLocation.fromNamespaceAndPath(AbsDaoMod.MODID, "textures/entity/sect/" + name + ".png");
    }

    @Override
    public ResourceLocation getTextureLocation(SectGuardianEntity entity) {
        return TEXTURES[Math.max(1, Math.min(4, entity.getSect()))];
    }
}
