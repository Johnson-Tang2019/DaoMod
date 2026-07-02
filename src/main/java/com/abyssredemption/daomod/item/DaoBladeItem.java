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
        tooltipComponents.add(Component.literal("品阶：" + SoulSwordItem.getRealmName(bladeRealm)).withStyle(ChatFormatting.AQUA));
        tooltipComponents.add(Component.literal("刀类兵刃伤害更高，但无法催动剑气或御剑飞行。").withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.literal("境界不足时近战伤害降低。").withStyle(ChatFormatting.DARK_GRAY));
    }
}
