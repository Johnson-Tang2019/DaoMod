package com.abyssredemption.daomod.client;

import com.abyssredemption.daomod.AbsDaoMod;
import com.abyssredemption.daomod.util.SwordFlightHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;

@EventBusSubscriber(modid = AbsDaoMod.MODID, value = Dist.CLIENT)
public class SwordFlightClientEvents {
    @SubscribeEvent
    public static void onRenderPlayer(RenderPlayerEvent.Post event) {
        ItemStack sword = SwordFlightHelper.getVisibleFlightSword(event.getEntity());
        if (sword.isEmpty()) {
            return;
        }

        PoseStack poseStack = event.getPoseStack();
        poseStack.pushPose();
        poseStack.translate(0.0, -0.12, 0.0);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0f - event.getEntity().getYRot()));
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0f));
        poseStack.mulPose(Axis.ZP.rotationDegrees(45.0f));
        poseStack.scale(1.45f, 1.45f, 1.45f);

        Minecraft.getInstance().getItemRenderer().renderStatic(
                sword,
                ItemDisplayContext.GROUND,
                event.getPackedLight(),
                OverlayTexture.NO_OVERLAY,
                poseStack,
                event.getMultiBufferSource(),
                event.getEntity().level(),
                event.getEntity().getId()
        );
        poseStack.popPose();
    }
}
