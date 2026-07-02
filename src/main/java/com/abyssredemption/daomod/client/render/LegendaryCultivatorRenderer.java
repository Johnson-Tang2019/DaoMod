package com.abyssredemption.daomod.client.render;

import com.abyssredemption.daomod.AbsDaoMod;
import com.abyssredemption.daomod.entity.LegendaryCultivatorEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;

public class LegendaryCultivatorRenderer extends HumanoidMobRenderer<LegendaryCultivatorEntity,
        HumanoidModel<LegendaryCultivatorEntity>> {
    private static final String[] NAMES = {"xuanyuan", "xuanyuan", "dugu_qiubai", "qinglian_jianxian",
            "ye_gucheng", "shen_duzhou", "nameless_artificer", "gongshu_ban", "ge_hong", "ouye_zi",
            "su_hongyi", "ming_wang", "dizang", "damo", "blood_river_ancestor", "wuhua", "mo_xuan",
            "guanghan_fairy", "fengshen_daoist", "grave_keeper", "ning_fan", "kuafu", "jingwei", "houyi",
            "xingtian", "xuanyuan_fourteen"};

    public LegendaryCultivatorRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.55f);
    }

    @Override
    public ResourceLocation getTextureLocation(LegendaryCultivatorEntity entity) {
        String name = NAMES[Math.max(1, Math.min(NAMES.length - 1, entity.getLegend()))];
        return ResourceLocation.fromNamespaceAndPath(AbsDaoMod.MODID, "textures/entity/legend/" + name + ".png");
    }

    @Override
    protected void scale(LegendaryCultivatorEntity entity, PoseStack poseStack, float partialTickTime) {
        float scale = entity.getLegend() == 21 || entity.getLegend() == 25 ? 1.8f
                : entity.getLegend() == 24 ? 1.3f : 1.0f;
        poseStack.scale(scale, scale, scale);
    }
}
