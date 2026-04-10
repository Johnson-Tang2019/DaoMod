package com.abyssredemption.daomod.client;

import com.abyssredemption.daomod.AbsDaoMod;
import com.abyssredemption.daomod.attachment.CultivationData;
import com.abyssredemption.daomod.registry.ModAttachments;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@EventBusSubscriber(modid = AbsDaoMod.MODID, value = Dist.CLIENT)
public class CultivetionHud {

    @SubscribeEvent
    public static void CultivationHud(RenderGuiLayerEvent.Post event) {

        Minecraft mc = Minecraft.getInstance();
        if (!VanillaGuiLayers.PLAYER_HEALTH.equals(event.getName())) {
            return;
        }
        if(mc.player == null) return;

        CultivationData data = mc.player.getData(ModAttachments.CULTIVATION);
        GuiGraphics graphics = event.getGuiGraphics();

        int width = event.getGuiGraphics().guiWidth();
        int height = event.getGuiGraphics().guiHeight();

        int x = width / 2 - 91;
        int y = height - 49; // 在血条上方留出间距

        // 1. 绘制背景条（黑色半透明）
        int barWidth = 81;
        graphics.fill(x, y, x + barWidth, y + 5, 0x161546aa);

        float ratio =  ((float)data.getQi() / CultivationData.getMaxQi(data.getRealm(), data.getStage()));
        int fillWidth = (int) (barWidth * ratio);
        if (fillWidth > 0) {
            graphics.fill(x, y, x + fillWidth, y + 5, 0x2129ffff);
            System.out.println("Gui drawn: " + ratio);
            System.out.println("Max Qi: " + CultivationData.getMaxQi(data.getRealm(), data.getStage()));
        }


    }


}

