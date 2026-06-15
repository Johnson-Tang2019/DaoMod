package com.abyssredemption.daomod.client.render;

import com.abyssredemption.daomod.AbsDaoMod;
import com.abyssredemption.daomod.entity.SwordBeamEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LightTexture;
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
    public static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(AbsDaoMod.MODID, "textures/entity/sword_beam.png");

    public SwordBeamRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(SwordBeamEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
        poseStack.scale(0.65F, 0.65F, 0.65F);

        float age = entity.tickCount + partialTick;
        float pulse = 0.88F + 0.12F * Mth.sin(age * 0.55F);
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(TEXTURE));

        // Ink shadow, jade aura, and bright blade core are drawn separately so the beam
        // reads like a brush stroke instead of a single flat crescent.
        drawQuad(poseStack, consumer, 2.35F, 0.78F, 0.006F, 18, 42, 48, alpha(62 * pulse));
        drawQuad(poseStack, consumer, 2.08F, 0.62F, 0.004F, 62, 206, 184, alpha(130 * pulse));
        drawQuad(poseStack, consumer, 1.84F, 0.48F, 0.002F, 232, 255, 240, alpha(228 * pulse));
        drawQuad(poseStack, consumer, 1.58F, 0.34F, 0.000F, 255, 246, 190, alpha(112 * pulse));

        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, buffer, LightTexture.FULL_BRIGHT);
    }

    private static int alpha(float value) {
        return Mth.clamp(Math.round(value), 0, 255);
    }

    private static void drawQuad(PoseStack poseStack, VertexConsumer consumer, float halfWidth, float halfHeight,
                                 float z, int r, int g, int b, int a) {
        PoseStack.Pose pose = poseStack.last();
        Matrix4f matrix4f = pose.pose();
        Matrix3f matrix3f = pose.normal();
        int overlay = OverlayTexture.NO_OVERLAY;
        int light = LightTexture.FULL_BRIGHT;

        vertex(-halfWidth, -halfHeight, z, r, g, b, a, 0.0F, 1.0F, overlay, light, 0.0F, 1.0F, 0.0F, consumer, matrix4f, matrix3f);
        vertex(halfWidth, -halfHeight, z, r, g, b, a, 1.0F, 1.0F, overlay, light, 0.0F, 1.0F, 0.0F, consumer, matrix4f, matrix3f);
        vertex(halfWidth, halfHeight, z, r, g, b, a, 1.0F, 0.0F, overlay, light, 0.0F, 1.0F, 0.0F, consumer, matrix4f, matrix3f);
        vertex(-halfWidth, halfHeight, z, r, g, b, a, 0.0F, 0.0F, overlay, light, 0.0F, 1.0F, 0.0F, consumer, matrix4f, matrix3f);

        vertex(-halfWidth, halfHeight, -z, r, g, b, a, 0.0F, 0.0F, overlay, light, 0.0F, -1.0F, 0.0F, consumer, matrix4f, matrix3f);
        vertex(halfWidth, halfHeight, -z, r, g, b, a, 1.0F, 0.0F, overlay, light, 0.0F, -1.0F, 0.0F, consumer, matrix4f, matrix3f);
        vertex(halfWidth, -halfHeight, -z, r, g, b, a, 1.0F, 1.0F, overlay, light, 0.0F, -1.0F, 0.0F, consumer, matrix4f, matrix3f);
        vertex(-halfWidth, -halfHeight, -z, r, g, b, a, 0.0F, 1.0F, overlay, light, 0.0F, -1.0F, 0.0F, consumer, matrix4f, matrix3f);
    }

    private static void vertex(float x, float y, float z, int r, int g, int b, int a, float u, float v,
                               int overlay, int light, float normalX, float normalY, float normalZ,
                               VertexConsumer consumer, Matrix4f pose, Matrix3f normal) {
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
