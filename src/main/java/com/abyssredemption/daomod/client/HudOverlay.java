package com.abyssredemption.daomod.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.abyssredemption.daomod.capability.ModCapabilities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.event.RenderGuiOverlayEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "xiu_mod", value = net.neoforged.api.distmarker.Dist.CLIENT)
public class HudOverlay {

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiOverlayEvent.Post event) {

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (player == null) return;

        if (!(mc.screen instanceof InventoryScreen)) return;

        player.getCapability(ModCapabilities.CULTIVATION).ifPresent(data -> {

            PoseStack poseStack = event.getPoseStack();
            Font font = mc.font;

            String realmText = "Realm: " + getRealmName(data.getRealm());
            String qiText = "Qi: " + data.getQi();

            int x = 10;
            int y = 10;

            font.draw(poseStack, realmText, x, y, 0xFFFFFF);
            font.draw(poseStack, qiText, x, y + 12, 0x00FFFF);
        });
    }

    private static String getRealmName(int realm) {
        return switch (realm) {
            case 0 -> "Qi Refining";
            case 1 -> "Spirit Transformation";
            case 2 -> "Void Refining";
            case 3 -> "Consolidation";
            case 4 -> "Great Perfection";
            case 5 -> "Tribulation";
            default -> "Unknown";
        };
    }

}
