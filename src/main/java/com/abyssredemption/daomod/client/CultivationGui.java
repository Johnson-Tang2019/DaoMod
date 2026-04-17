package com.abyssredemption.daomod.client;

import com.abyssredemption.daomod.AbsDaoMod;
import com.abyssredemption.daomod.attachment.CultivationData;
import com.abyssredemption.daomod.registry.ModAttachments;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;

@EventBusSubscriber(modid = AbsDaoMod.MODID, value = Dist.CLIENT)
public class CultivationGui {

    @SubscribeEvent
    public static void onRenderGui(ScreenEvent.Render.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (!(event.getScreen() instanceof InventoryScreen inventoryScreen)) {
            return;
        }
        if(mc.player == null) return;

        CultivationData data = mc.player.getData(ModAttachments.CULTIVATION);
        GuiGraphics graphics = event.getGuiGraphics();

        int guiLeft = inventoryScreen.getGuiLeft() + 130;
        int guiTop = inventoryScreen.getGuiTop() + 60;
        int color = 0xff67d1;

        graphics.pose().pushPose();
        graphics.pose().translate(0, 0, 2000);

        Component qiLabel = Component.translatable("gui.daomod.qi", data.getQi());

        Component realmText = getStageName(data.getStage(), data.getRealm());

        graphics.drawString(mc.font, realmText, guiLeft, guiTop, color, false);
        graphics.drawString(mc.font, qiLabel, guiLeft, guiTop + 10, color, false);
        graphics.drawString(mc.font, data.getRealmProgress() + "", guiLeft, guiTop + 20, color, false);
    }

    private static Component getRealmName(int realm) {
        return switch (realm) {
            case 1 -> Component.translatable("gui.daomod.realm1");
            case 2 -> Component.translatable("gui.daomod.realm2");
            case 3 -> Component.translatable("gui.daomod.realm3");
            case 4 -> Component.translatable("gui.daomod.realm4");
            case 5 -> Component.translatable("gui.daomod.realm5");
            case 6 -> Component.translatable("gui.daomod.realm6");
            case 7 -> Component.translatable("gui.daomod.realm7");
            case 8 -> Component.translatable("gui.daomod.realm8");
            default -> Component.translatable("gui.daomod.realm0");
        };
    }

    private static Component getStageName(int stage, int realm) {
        if(stage == 9) return Component.translatable("gui.daomod.stage9");
        return switch (stage) {
            case 1 -> Component.translatable("gui.daomod.stage1", getRealmName(realm));
            case 2 -> Component.translatable("gui.daomod.stage2", getRealmName(realm));
            case 3 -> Component.translatable("gui.daomod.stage3", getRealmName(realm));
            case 4 -> Component.translatable("gui.daomod.stage4", getRealmName(realm));
            case 5 -> Component.translatable("gui.daomod.stage5", getRealmName(realm));
            case 6 -> Component.translatable("gui.daomod.stage6", getRealmName(realm));
            case 7 -> Component.translatable("gui.daomod.stage7", getRealmName(realm));
            case 8 -> Component.translatable("gui.daomod.stage8", getRealmName(realm + 1));
            default -> Component.translatable("gui.daomod.stage0", getRealmName(realm));
        };
    }
}
