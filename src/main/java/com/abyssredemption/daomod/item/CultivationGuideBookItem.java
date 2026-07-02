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
                                + "本指南记录当前模组的主要玩法：修炼境界、蒲团打坐、灵气环境、炁、玄天灵剑、丹药与管理指令。\n\n"
                                + "右键翻页查看各系统说明。"),
                page("境界体系",
                        "修行共有九个大境界：\n\n"
                                + "炼气 -> 筑基 -> 金丹\n"
                                + "元婴 -> 化神 -> 炼虚\n"
                                + "合体 -> 大乘 -> 渡劫\n\n"
                                + "每个大境界包含九个小阶段，修炼进度达到 100 后突破。溢出的进度会保留。\n\n"
                                + "小突破会恢复部分生命；跨越大境界时会引发天地异象，并恢复全部生命与炁。渡劫九重为当前上限。\n\n"
                                + "死亡会造成境界惩罚。"),
                page("蒲团修炼",
                        "蒲团是基础修炼设施。\n\n"
                                + "放置蒲团后右键坐下，角色会周期性获得修炼进度。\n\n"
                                + "修炼适合稳定提升境界，是比吃丹更稳妥的成长方式。"),
                page("灵气环境",
                        "灵气浓度会影响打坐时的修炼速度。\n\n"
                                + "灵植会散发温和灵气，适合布置在蒲团附近。\n\n"
                                + "灵矿蕴含更浓的灵气，距离蒲团越近收益越明显。当前每次打坐最低进度 +1，灵气足够时可提高到 +6。"),
                page("炁",
                        "炁是释放法宝能力的能量。\n\n"
                                + "炁会随时间自动恢复；死亡后炁会归零。\n\n"
                                + "炁不足时，玄天灵剑无法凝聚剑气。"),
                page("玄天灵剑",
                        "玄天灵剑是当前主要战斗法宝。\n\n"
                                + "当前品阶为金丹，金丹及以上才能完整催动。\n\n"
                                + "右键蓄力，松开后消耗炁释放剑气。蓄力越久，消耗和伤害越高。\n\n"
                                + "剑气会从剑身处发出，并以竖向斩击呈现。\n\n"
                                + "剑有品阶，品阶与境界一一对应。境界不足时无法催动剑气，只能当普通剑使用，且近战伤害降低。"),
                page("问灵短剑",
                        "问灵短剑取自设定集中“宗门新进弟子问心所得”的低阶法器。\n\n"
                                + "当前品阶为筑基，筑基及以上才能催动感应。\n\n"
                                + "右键可感应十米内最近的聚灵之物，提示其方向、距离与灵气强度。\n\n"
                                + "它适合寻找灵植、灵矿，并辅助布置蒲团周围的修炼环境。"),
                page("设定集名剑",
                        "已加入设定集中的剑类神兵：斩邪·孤星、轩辕夏禹、纯钧、巨阙。\n\n"
                                + "斩邪·孤星为元婴品阶；纯钧、巨阙为炼虚品阶；轩辕夏禹为渡劫品阶。\n\n"
                                + "这些剑均遵循剑品阶规则，境界不足时无法催动剑气或御剑飞行。"),
                page("设定集刀兵",
                        "已加入设定集中的刀类兵刃：影杀匕、剪秋月、斩神残刃、斩因果天刀。\n\n"
                                + "影杀匕、剪秋月为金丹品阶；斩神残刃为大乘品阶；斩因果天刀为渡劫品阶。\n\n"
                                + "刀类兵刃近战伤害更高，但不能释放剑气，也不能用于御剑飞行。境界不足时近战伤害降低。"),
                page("低阶法器",
                        "避水玉佩：装备在 Curios 的 charm 或 necklace 槽位后，会持续获得水下呼吸与水中疾行，并熄灭身上火焰。\n\n"
                                + "燃薪指环：装备在 Curios 的 ring 槽位后，会持续获得火焰抗性。\n\n"
                                + "扶摇扇：右键向前方卷起小旋风，击退并扰乱敌人视线。"),
                page("丹药与丹毒",
                        "丹药可快速推进修为，但会积累丹毒。\n\n"
                                + "丹毒过高会触发反噬，使继续服丹和打坐修炼受到限制。\n\n"
                                + "急于突破时可用丹药，长期修行仍建议配合蒲团与灵气环境。"),
                page("因果",
                        "杀戮会累积因果，且因果无法消除。\n\n"
                                + "击杀敌对生物会增加少量因果。\n\n"
                                + "击杀中立生物或友好生物会增加更多因果，并立刻受到反胃影响。\n\n"
                                + "大境界突破必引天劫；境界与因果越高，劫雷越多。因果达到二十时还会引发心魔劫。"),
                page("宗门道统",
                        "散修可用 /dao join <1-4> 选择一次道统：\n\n"
                                + "1 天枢剑宗：攻击与身法\n"
                                + "2 造化仙宗：气运与回炁\n"
                                + "3 大雷音寺：护甲与定身\n"
                                + "4 罗刹魔教：杀伐与速度\n\n"
                                + "入宗后不可自行改投，管理员可用 /dao sect set 调整。"),
                page("宗门行走",
                        "宗门弟子会在对应地域行走：\n\n"
                                + "剑侍巡游山地；器徒出没于平原与林地；武僧驻足高原；魔徒潜伏洞穴与恶地。\n\n"
                                + "同宗弟子不会主动攻击你，敌对道统会互相警戒。罗刹魔徒会主动袭击外人。\n\n"
                                + "击败他们可能获得各宗常用灵植和矿材。"),
                page("宗门据点",
                        "探索对应地域时，可发现宗门的小型据点：\n\n"
                                + "山地剑亭供剑侍磨剑；平原林地的造化坊设有炉台；高原禅台悬挂雷音古钟；恶地与洞穴附近偶现罗刹祭坛。\n\n"
                                + "据点十分稀少，屋顶、立柱与核心陈设均带有鲜明宗门风格。"),
                page("剑道传说",
                        "五位剑道领袖已作为传说级敌手现世：\n\n"
                                + "轩辕挥剑碎空；独孤求败斩断术法增益；青莲剑仙以莲意加速自愈；叶孤城冻结气机；沈独舟越过因果瞬移刺杀。\n\n"
                                + "他们拥有独立首领血条、技能粒子、战斗动画与专属外观，目前可通过创造模式生成蛋召出。"),
                page("造化宗师",
                        "五位造化宗师已作为传说级敌手现世：\n\n"
                                + "无名匠师聚灵锻甲；公输班驱动机关自修；葛洪以丹毒侵蚀经脉；欧冶子燃起铸魂剑火；苏红衣推演并封锁术法破绽。\n\n"
                                + "击败他们可获得炼器、丹道与天机相关的高阶物品。"),
                page("佛魔两极",
                        "五位佛魔强者已作为传说级敌手现世：\n\n"
                                + "冥王以业力收割生机；地藏立下镇狱护持；达摩以肉身震退强敌；血河老祖汲取精血重生；无花施展红莲业火。\n\n"
                                + "他们代表慈悲、秩序、苦修、贪生与狂乱的不同道路。"),
                page("禁忌存在",
                        "五位逆命者与秩序守望者已现世：\n\n"
                                + "墨玄展开真空断法；广寒仙子冻结时空；封神道人封锁境界与增益；守墓人写下因果葬录；宁凡遁出天道视线发动潜袭。\n\n"
                                + "这组敌手擅长削弱、控制和破除增益，正面强攻之外更需准备解控手段。"),
                page("荒古遗脉",
                        "五位荒古血脉传承者已现世：\n\n"
                                + "夸父追日燃血；精卫以填海意志封镇敌手；后羿箭意直击本质；刑天越战越勇；轩辕十四以绝对力量击碎术法。\n\n"
                                + "夸父、刑天与轩辕十四拥有更大的模型和碰撞体，近战压迫力远超普通修士。"),
                page("纪元战帖",
                        "五类纪元战帖可由高阶灵植、灵矿与灵石合成。右键下帖后，将从对应人物组中随机召来一位传奇强者。\n\n"
                                + "剑道与造化战帖要求化神；佛魔战帖要求炼虚；禁忌战帖要求合体；荒古战帖要求大乘。\n\n"
                                + "挑战按剑道、造化、佛魔、禁忌、荒古依次解锁。参与击败前一组传奇，才能使用下一组战帖。\n\n"
                                + "挑战在服务端生成，附近玩家可共同作战。九十六格内同时只允许存在一位传奇挑战者。"),
                page("终局奖励",
                        "完成荒古纪元挑战即走完五组传奇试炼。每名实际参战并在场的修士首次完成时，都会获得鸿蒙创世莲与大量历练。\n\n"
                                + "该奖励依托个人进度保存，每人仅能领取一次；普通首领掉落仍只生成一份。"),
                page("四重位面",
                        "主世界对应灵虚界，是修行与宗门争夺的核心。神阶因果器“隔世之门”可依次通往尘寰界、帝落渊与永恒仙域，最终返回灵虚界。\n\n"
                                + "尘寰灵气平淡；帝落渊长夜禁断；永恒仙域仙光恒照。跨界位置会保留，并自动寻找地表落点。"),
                page("传奇遗迹",
                        "新生成的区块中可发现五类稀有地标：剑冢、造化秘库、幽冥古祠、虚空观星台与荒古神碑。\n\n"
                                + "遗迹中心是高浓度灵脉节点。在核心八格内放置蒲团修炼，可获得洞天福地加成。\n\n"
                                + "它们不会自动唤醒传奇人物，需由达到境界的修士主动下帖。"),
                page("常用指令",
                        "/dao join <1-4>\n选择宗门道统。\n\n"
                                + "管理员指令：\n"
                                + "/dao qi set <玩家> <值>\n"
                                + "/dao realm set <玩家> <0-8>\n"
                                + "/dao realmProgress set <玩家> <0-99>\n"
                                + "/dao sect set <玩家> <0-4>"),
                page("修行提示",
                        "推荐流程：\n\n"
                                + "1. 制作并放置蒲团。\n"
                                + "2. 在蒲团附近摆放灵植与灵矿，提高灵气浓度。\n"
                                + "3. 打坐积累修炼进度。\n"
                                + "4. 保持炁量，用玄天灵剑远程作战。\n"
                                + "5. 谨慎服丹，避免丹毒反噬。")
        );
    }

    private static Component page(String title, String body) {
        return Component.literal("")
                .append(Component.literal("《" + title + "》\n\n").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD))
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
