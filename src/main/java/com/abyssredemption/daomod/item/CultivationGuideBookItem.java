package com.abyssredemption.daomod.item;

import com.abyssredemption.daomod.network.OpenGuideBookPayload;
import com.abyssredemption.daomod.registry.ModItems;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.Filterable;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.WrittenBookContent;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

public class CultivationGuideBookItem extends Item {
    public CultivationGuideBookItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    public static ItemStack createBook() {
        ItemStack book = new ItemStack(ModItems.CULTIVATION_GUIDE_BOOK.get());
        book.set(DataComponents.WRITTEN_BOOK_CONTENT, createBookContent());
        return book;
    }

    public static List<Component> createGuidePages() {
        return List.of(
                page("道途指南",
                        "欢迎来到 Ab's Dao Mod。\n\n"
                                + "本指南记录当前模组的主要玩法：修炼境界、蒲团打坐、炁、玄天灵剑、丹药与管理指令。\n\n"
                                + "右键翻页查看各系统说明。"),
                page("境界体系",
                        "修行共有九个大境界：\n\n"
                                + "炼气 → 筑基 → 金丹\n"
                                + "元婴 → 化神 → 炼虚\n"
                                + "合体 → 大乘 → 渡劫\n\n"
                                + "每个大境界包含九个小阶段，死亡会造成境界惩罚。"),
                page("蒲团修炼",
                        "蒲团是基础修炼设施。\n\n"
                                + "放置蒲团后右键坐下，角色会周期性获得修炼进度。\n\n"
                                + "修炼适合稳定提升境界，是比吃丹更稳妥的成长方式。"),
                page("炁",
                        "炁是释放法宝能力的能量。\n\n"
                                + "炁会随时间自动恢复；死亡后炁会归零。\n\n"
                                + "炁不足时，玄天灵剑无法凝聚剑气。"),
                page("玄天灵剑",
                        "玄天灵剑是当前主要战斗法宝。\n\n"
                                + "右键蓄力，松开后消耗炁释放剑气。蓄力越久，消耗和伤害越高。\n\n"
                                + "剑气现在会从剑身处发出，并以竖向斩击呈现。"),
                page("丹药与丹毒",
                        "丹药可快速推进修为，但会积累丹毒。\n\n"
                                + "丹毒过高会触发反噬，使继续服丹和打坐修炼受到限制。\n\n"
                                + "急于突破时可用丹药，长期修行仍建议配合蒲团。"),
                page("常用指令",
                        "/dao get\n查看当前修炼数据。\n\n"
                                + "/dao set realm <0-8>\n设置大境界。\n\n"
                                + "/dao set stage <0-8>\n设置小境界。\n\n"
                                + "/dao set qi <值>\n设置炁量。"),
                page("修行提示",
                        "推荐流程：\n\n"
                                + "1. 制作并放置蒲团。\n"
                                + "2. 打坐积累修炼进度。\n"
                                + "3. 保持炁量，用玄天灵剑远程作战。\n"
                                + "4. 谨慎服丹，避免丹毒反噬。")
        );
    }

    private static Component page(String title, String body) {
        return Component.literal("")
                .append(Component.literal("【" + title + "】\n\n").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD))
                .append(Component.literal(body).withStyle(ChatFormatting.DARK_GRAY));
    }

    private static WrittenBookContent createBookContent() {
        var filteredPages = createGuidePages().stream()
                .map(Filterable::passThrough)
                .toList();

        return new WrittenBookContent(
                Filterable.passThrough("道途指南"),
                "道",
                0,
                filteredPages,
                true
        );
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!stack.has(DataComponents.WRITTEN_BOOK_CONTENT)) {
            stack.set(DataComponents.WRITTEN_BOOK_CONTENT, createBookContent());
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, OpenGuideBookPayload.INSTANCE);
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.literal("《道途指南》").withStyle(ChatFormatting.GOLD);
    }
}
