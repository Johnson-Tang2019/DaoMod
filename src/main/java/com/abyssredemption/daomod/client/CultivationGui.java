package com.abyssredemption.daomod.client;

import com.abyssredemption.daomod.AbsDaoMod;
import com.abyssredemption.daomod.attachment.CultivationData;
import com.abyssredemption.daomod.registry.ModAttachments;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;

@EventBusSubscriber(modid = AbsDaoMod.MODID, value = Dist.CLIENT)
public class CultivationGui {
    private static final int WIDTH = 132;

    @SubscribeEvent
    public static void onRenderGui(ScreenEvent.Render.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (!(event.getScreen() instanceof InventoryScreen screen) || mc.player == null) return;

        CultivationData data = mc.player.getData(ModAttachments.CULTIVATION);
        GuiGraphics graphics = event.getGuiGraphics();
        int x = screen.getGuiLeft() + screen.getXSize() + 6;
        if (x + WIDTH > graphics.guiWidth()) x = Math.max(4, screen.getGuiLeft() - WIDTH - 6);
        int y = screen.getGuiTop();

        graphics.pose().pushPose();
        graphics.pose().translate(0, 0, 2000);
        graphics.fill(x, y, x + WIDTH, y + 122, 0xd8181d1a);
        graphics.fill(x, y, x + WIDTH, y + 2, 0xff67d18a);

        draw(graphics, mc, Component.translatable("gui.daomod.attributes"), x + 8, y + 8, 0xffd9efdc);
        draw(graphics, mc, getStageName(data.getStage(), data.getRealm()), x + 8, y + 21, 0xff67d18a);

        int progress = Math.max(0, Math.min(99, data.getRealmProgress()));
        graphics.fill(x + 8, y + 34, x + 124, y + 39, 0xff303832);
        graphics.fill(x + 8, y + 34, x + 8 + Math.round(116 * progress / 99f), y + 39, 0xff59b979);
        draw(graphics, mc, Component.translatable("gui.daomod.progress", progress), x + 8, y + 42, 0xffaeb8b0);

        long maxQi = CultivationData.getMaxQi(data.getRealm(), data.getStage());
        draw(graphics, mc, Component.translatable("gui.daomod.qi_pair", data.getQi(), maxQi), x + 8, y + 54, 0xff65cfe8);
        draw(graphics, mc, Component.translatable("gui.daomod.health_pair",
                Math.round(mc.player.getHealth()), Math.round(mc.player.getMaxHealth())), x + 8, y + 65, 0xffff7777);
        draw(graphics, mc, Component.translatable("gui.daomod.combat_pair",
                oneDecimal(mc.player.getAttributeValue(Attributes.ATTACK_DAMAGE)),
                oneDecimal(mc.player.getAttributeValue(Attributes.ARMOR))), x + 8, y + 76, 0xffffcf70);
        draw(graphics, mc, Component.translatable("gui.daomod.speed",
                oneDecimal(mc.player.getAttributeValue(Attributes.MOVEMENT_SPEED) * 20)), x + 8, y + 87, 0xff8edfa5);
        draw(graphics, mc, Component.translatable("gui.daomod.karma", data.getKarma()), x + 8, y + 98, 0xffc58cff);
        draw(graphics, mc, Component.translatable("gui.daomod.sect", getSectName(data.getSect())), x + 8, y + 109, 0xffe4b95f);
        graphics.pose().popPose();
    }

    private static void draw(GuiGraphics graphics, Minecraft mc, Component text, int x, int y, int color) {
        graphics.drawString(mc.font, text, x, y, color, false);
    }

    private static String oneDecimal(double value) {
        return String.format(java.util.Locale.ROOT, "%.1f", value);
    }

    private static Component getSectName(int sect) {
        return Component.translatable("gui.daomod.sect" + Math.max(0, Math.min(4, sect)));
    }

    private static Component getRealmName(int realm) {
        return Component.translatable("gui.daomod.realm" + Math.max(0, Math.min(8, realm)));
    }

    private static Component getStageName(int stage, int realm) {
        int safeStage = Math.max(0, Math.min(8, stage));
        return Component.translatable("gui.daomod.stage" + safeStage,
                getRealmName(safeStage == 8 ? realm + 1 : realm));
    }
}
