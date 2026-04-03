package com.abyssredemption.daomod.client;

import com.abyssredemption.daomod.AbsDaoMod;
import com.abyssredemption.daomod.attachment.CultivationData;
import com.abyssredemption.daomod.registry.ModAttachments;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

@EventBusSubscriber(modid = AbsDaoMod.MODID, value = Dist.CLIENT)
public class CultivationGui {

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.hideGui) return;

        CultivationData data = mc.player.getData(ModAttachments.CULTIVATION);
        GuiGraphics graphics = event.getGuiGraphics();

        int x = 10;
        int y = 10;
        int color = 0xFFFFFF;

        String realmText = "realm: " + getRealmName(data.realm());
        String qiText = "qi: " + data.qi();

        graphics.drawString(mc.font, realmText, x, y, color);
        graphics.drawString(mc.font, qiText, x, y + 10, color);
    }

    private static String getRealmName(int realm) {
        return switch (realm) {
            case 0 -> "Qi Gathering";
            case 1 -> "Foundation Establishment";
            case 2 -> "Golden Core";
            default -> "δ֪";
        };
    }

}
