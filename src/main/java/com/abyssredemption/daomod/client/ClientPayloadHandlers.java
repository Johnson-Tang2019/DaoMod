package com.abyssredemption.daomod.client;

import com.abyssredemption.daomod.attachment.CultivationData;
import com.abyssredemption.daomod.item.CultivationGuideBookItem;
import com.abyssredemption.daomod.network.CultivationPayload;
import com.abyssredemption.daomod.network.OpenGuideBookPayload;
import com.abyssredemption.daomod.registry.ModAttachments;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class ClientPayloadHandlers {
    private ClientPayloadHandlers() {
    }

    public static void handleCultivationSync(CultivationPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.player != null) {
                minecraft.player.setData(ModAttachments.CULTIVATION, new CultivationData(
                        payload.realm(), payload.qi(), payload.sectOrthodoxy(), payload.stage(),
                        payload.realmProgress(), payload.karma(), payload.sect()));
            }
        });
    }

    public static void handleOpenGuideBook(OpenGuideBookPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> Minecraft.getInstance().setScreen(new BookViewScreen(
                new BookViewScreen.BookAccess(CultivationGuideBookItem.createGuidePages()))));
    }
}
