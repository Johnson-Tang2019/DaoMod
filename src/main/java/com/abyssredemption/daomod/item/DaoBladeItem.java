package com.abyssredemption.daomod.item;

import com.abyssredemption.daomod.registry.ModAttachments;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;

public class DaoBladeItem extends SwordItem {
    private final int bladeRealm;

    public DaoBladeItem(Tier tier, Properties properties, int bladeRealm) {
        super(tier, properties);
        this.bladeRealm = Math.max(0, Math.min(8, bladeRealm));
    }

    public int getBladeRealm() {
        return bladeRealm;
    }

    public boolean isAboveRealm(Player player) {
        return player.getData(ModAttachments.CULTIVATION).getRealm() < bladeRealm;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents,
                                TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.abyssredemptiondaomod.weapon_rank",
                Component.translatable("gui.daomod.realm" + bladeRealm)).withStyle(ChatFormatting.AQUA));
        tooltipComponents.add(Component.translatable("tooltip.abyssredemptiondaomod.blade_style").withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable("tooltip.abyssredemptiondaomod.blade_restriction")
                .withStyle(ChatFormatting.DARK_GRAY));
    }
}
