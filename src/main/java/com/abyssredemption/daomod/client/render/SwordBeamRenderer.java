package com.abyssredemption.daomod.client.render;

import com.abyssredemption.daomod.AbsDaoMod;
import com.abyssredemption.daomod.entity.SwordBeamEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class SwordBeamRenderer extends EntityRenderer<SwordBeamEntity> {
    // 纹理文件路径：assets/abyssredemptiondaomod/textures/entity/sword_beam.png
    // 建议使用半透明的蓝色或青色弧形贴图
    public static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(AbsDaoMod.MODID, "textures/entity/sword_beam.png");

    public SwordBeamRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(SwordBeamEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        // 1. 让剑气始终面向玩家（类似粒子效果，但我们只让它绕Y轴旋转）
        float yaw = Mth.lerp(partialTick, entity.yRotO, entity.getYRot());
        float pitch = Mth.lerp(partialTick, entity.xRotO, entity.getXRot());
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - yaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(-pitch));

        // 2. 动态效果：随时间缩放和淡出（模拟灵气消散）
        float lifeTime = entity.tickCount + partialTick;
        float scale = 1.0F + lifeTime * 0.05F; // 越飞越大
        float alpha = Mth.clamp(1.0F - lifeTime / 40.0F, 0.0F, 1.0F); // 2秒后完全淡出
        poseStack.scale(scale * 1.5f, scale * 0.5f, scale); // 扁平化，看起来像月牙

        PoseStack.Pose lastPose = poseStack.last();
        Matrix4f matrix4f = lastPose.pose();
        Matrix3f matrix3f = lastPose.normal();

        // 3. 使用特定的渲染类型：实体转半透明（entity_translucent），这样可以看见背景且受光照
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(TEXTURE));
        // 它自身发光
        RenderType.beaconBeam(TEXTURE, true);

        // 4. 绘制四边形面片（一个扁平的月牙形）
        // 颜色设为青色 (85, 255, 255)，并加上动态透明度
        int r = 85, g = 255, b = 255;
        int a = (int)(alpha * 255);

        // 参数：X, Y, Z, R, G, B, A, U, V, Overlay, Light, NormalX, NormalY, NormalZ
        vertex(-1.0F, -1.0F, 0.0F, r, g, b, a, 0.0F, 1.0F, OverlayTexture.NO_OVERLAY, packedLight, 0.0F, 1.0F, 0.0F, vertexConsumer, matrix4f, matrix3f);
        vertex( 1.0F, -1.0F, 0.0F, r, g, b, a, 1.0F, 1.0F, OverlayTexture.NO_OVERLAY, packedLight, 0.0F, 1.0F, 0.0F, vertexConsumer, matrix4f, matrix3f);
        vertex( 1.0F,  1.0F, 0.0F, r, g, b, a, 1.0F, 0.0F, OverlayTexture.NO_OVERLAY, packedLight, 0.0F, 1.0F, 0.0F, vertexConsumer, matrix4f, matrix3f);
        vertex(-1.0F,  1.0F, 0.0F, r, g, b, a, 0.0F, 0.0F, OverlayTexture.NO_OVERLAY, packedLight, 0.0F, 1.0F, 0.0F, vertexConsumer, matrix4f, matrix3f);

        // 5. 绘制背面（防止转身看不见）
        vertex(-1.0F,  1.0F, 0.0F, r, g, b, a, 0.0F, 0.0F, OverlayTexture.NO_OVERLAY, packedLight, 0.0F, -1.0F, 0.0F, vertexConsumer, matrix4f, matrix3f);
        vertex( 1.0F,  1.0F, 0.0F, r, g, b, a, 1.0F, 0.0F, OverlayTexture.NO_OVERLAY, packedLight, 0.0F, -1.0F, 0.0F, vertexConsumer, matrix4f, matrix3f);
        vertex( 1.0F, -1.0F, 0.0F, r, g, b, a, 1.0F, 1.0F, OverlayTexture.NO_OVERLAY, packedLight, 0.0F, -1.0F, 0.0F, vertexConsumer, matrix4f, matrix3f);
        vertex(-1.0F, -1.0F, 0.0F, r, g, b, a, 0.0F, 1.0F, OverlayTexture.NO_OVERLAY, packedLight, 0.0F, -1.0F, 0.0F, vertexConsumer, matrix4f, matrix3f);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }

    // 辅助方法，简化顶点绘制
    private void vertex(float x, float y, float z, int r, int g, int b, int a, float u, float v, int overlay, int light, float normalX, float normalY, float normalZ, VertexConsumer consumer, Matrix4f pose, Matrix3f normal) {
        consumer.addVertex(pose, x, y, z)
                .setColor(r, g, b, a)
                .setUv(u, v)
                .setOverlay(overlay)
                .setLight(light)
                .setNormal(normalX, normalY, normalZ);
    }

    @Override
    public ResourceLocation getTextureLocation(SwordBeamEntity entity) {
        return TEXTURE;
    }
}