境界（Realm）：
    0：【炼气】（纳气，Qi Gathering Realm）：感知天地灵气，开辟丹田，引气入体。寿一百二十。
    1：【筑基】（铸基，Foundation Establishment Realm）：灵气化液，铸就道基。寿三百。
    2：【金丹】（凝真，Golden Core Realm）：结成混元金丹。寿五百。
    3：【元婴】（化生，Nascent Soul Realm）：丹破生婴，神魂具象化。寿千载。
    4：【化神】（通灵，Spirit Transformation Realm）：神魂与天地共鸣。寿三千。
    5：【炼虚】（返虚，Void Refinement Realm）：领悟法则碎片。寿八千。
    6：【合体】（合道，Dao Integration Realm）：元神与肉身完美融合。寿两万。
    7：【大乘】（圆满，Grand Ascension Realm）：仙体初成，剔除凡尘杂质。寿五万。
    8：【渡劫】（飞升，Heavenly Tribulation Realm）：迎接九天神雷洗礼，成则羽化登仙。

炁（Qi）：
    炁是释放法宝能力的能量。
    炁会随时间自动恢复。
    死亡后炁归零。
    最大炁量 = 10^(realm + 1) * (stage + 1)。
    玄天灵剑释放剑气会消耗炁。

小境界（Stage）：
    0 - 6：一重 - 七重
    7：半步
    8：巅峰

修炼进度（Realm Progress）：
    0 - 99
    打坐会增加修炼进度。
    修炼进度超过 99 时清零，并提升小境界。

宗门道统 / 丹毒（sectOrthodoxy）：
    当前代码中 sectOrthodoxy 用作丹毒层数。
    丹毒反噬期间不可服丹，打坐无法增加修炼进度。

因果（Karma）：
    因果是杀戮产生的永久业力记录。
    因果只能增加，无法消除。
    击杀敌对或普通生物：因果 +1。
    击杀中立生物：因果 +3，并获得反胃。
    击杀友好生物：因果 +5，并获得反胃。
    击杀玩家：因果 +6，并获得反胃。
    因果会保存到 CultivationData，并同步到客户端。
    背包修行面板会显示当前因果。

灵气（Spiritual Aura）：
    灵气浓度是局部环境值。
    玩家打坐时会扫描周围 8 格内的灵气源。
    灵气源越多、距离越近，灵气浓度越高。
    当前灵气浓度上限为 30。
    打坐基础修炼进度为 +1。
    灵气浓度每 6 点提升 1 点打坐进度，最高每次 +6。
    打坐提示会显示当前灵气浓度与本次修炼进度增量。

灵气源：
    灵植（lingzhi_plant）：
        提供温和灵气。
        灵气强度：3。
        作为类似原版花的地表植物生成。
        普通主世界群系中低概率生成。
        寒冷主世界群系中额外生成，更容易出现。

    灵矿（lingshi_ore）：
        提供更浓灵气。
        灵气强度：5。
        作为类似钻石矿的地下矿石生成。
        矿脉大小：4。
        每区块尝试次数：7。
        高度分布使用钻石矿同类 trapezoid 范围。
        需要镐挖掘，至少铁镐等级。

蒲团（putuan）：
    基础修炼设施。
    放置后右键坐下。
    玩家坐在蒲团座位上时，每 100 tick 进行一次打坐判定。
    若没有丹毒反噬，则根据灵气浓度增加修炼进度。
    打坐时播放 ENCHANTED_HIT 粒子，粒子数量随灵气浓度提高。

丹药（Dan）：
    无正面药水效果，仅恢复 1 点饥饿值。
    食用后小境界 stage +1。
    每次服丹增加 1 层丹毒。
    丹毒 1 - 4 层：显示 dan_du 效果图标。
    丹毒达到 5 层：触发丹毒反噬。
    丹毒反噬：
        获得 dan_du_fan_shi 效果 10 分钟。
        获得短暂反胃。
        期间不可服丹。
        期间打坐无法增加修炼进度。
        反噬触发后丹毒层数重置为 0。

自定义效果：
    dan_du：
        丹毒层数显示效果，无实际伤害。

    dan_du_fan_shi：
        丹毒反噬效果。
        锁定修为增长。
        期间不可服丹。

法宝 / 物品：
    剑品阶：
        剑拥有品阶属性。
        品阶与九个大境界一一对应：
            0 炼气
            1 筑基
            2 金丹
            3 元婴
            4 化神
            5 炼虚
            6 合体
            7 大乘
            8 渡劫
        玩家境界低于剑品阶时，无法催动剑气或特殊能力。
        境界不足时仍可把剑当普通剑近战使用。
        境界不足时近战伤害降低为最终伤害的 40%。
        新增剑类物品时必须通过 registerRankedSword 或等效构造传入品阶。

    御剑飞行：
        筑基及以上玩家可催动剑类法宝御空。
        玩家境界必须不低于剑品阶，否则只能当普通剑使用，无法御剑。
        起飞方式类似鞘翅：玩家离地后手持剑右键，可进入御剑滑翔状态。
        起飞消耗 5 点炁。
        飞行期间每 10 tick 消耗 1 点炁。
        飞行期间每 20 tick 消耗 1 点剑耐久。
        炁不足、剑耐久不足、剑离手或境界不足时会自动中断御剑。
        飞行时剑会在玩家脚下横向显示，并播放灵光粒子。

    玄天灵剑（soul_sword）：
        主要战斗法宝。
        当前品阶：金丹。
        右键蓄力，松开后消耗炁释放剑气。
        蓄力越久，消耗和伤害越高。
        剑气从剑身处发出。
        剑气呈竖向斩击。
        剑气渲染调整为偏中式风格。

    问灵短剑（wenling_dagger）：
        低阶探查法器。
        当前品阶：筑基。
        筑基及以上方可催动感应。
        右键感应 10 米 / 10 格内最近的聚灵之物。
        聚灵之物指能提升灵气浓度的方块，目前为灵植与灵矿。
        命中后提示方位、距离、方块名称与灵气强度。
        目标方块位置会播放粒子提示。
        不再感应附近生灵。
    斩邪·孤星（zhanxie_guxing）：
        来源：设定集灵宝（天阶）“斩邪名：孤星”。
        当前品阶：元婴。
        作为剑类法宝加入，可释放剑气并参与御剑飞行。
    轩辕夏禹（xuanyuan_xiayu）：
        来源：设定集中始源剑祖轩辕所铸世间第一把飞剑。
        当前品阶：渡劫。
        作为剑类法宝加入，可释放剑气并参与御剑飞行。
    纯钧（chunjun）：
        来源：设定集中欧冶子炼制的通天灵宝级神兵。
        当前品阶：炼虚。
        作为剑类法宝加入，可释放剑气并参与御剑飞行。
    巨阙（juque）：
        来源：设定集中欧冶子炼制的通天灵宝级神兵。
        当前品阶：炼虚。
        作为剑类法宝加入，可释放剑气并参与御剑飞行。

    刀品阶：
        刀类兵刃同样拥有品阶属性。
        刀不释放剑气，也不能用于御剑飞行。
        刀的近战伤害高于剑。
        玩家境界低于刀品阶时，仍可近战使用，但伤害降低为最终伤害的 40%。
        新增刀类物品时必须通过 registerRankedBlade 或等效构造传入品阶。
    影杀匕（yingsha_bi）：
        来源：设定集法宝（地阶）“影杀匕”。
        当前品阶：金丹。
    剪秋月（jianqiuyue）：
        来源：设定集法宝（地阶）“剪秋月”。
        当前品阶：金丹。
    斩神残刃（zhanshen_canren）：
        来源：设定集通天灵宝（古阶）“斩神残刃”。
        当前品阶：大乘。
    斩因果天刀（zhanyinguo_tiandao）：
        来源：设定集至尊神兵 / 因果器（神阶）“斩因果天刀”。
        当前品阶：渡劫。

    避水玉佩（bishui_amulet）：
        Curios 饰品。
        可装备在 charm 或 necklace 槽位。
        佩戴后持续获得水下呼吸与海豚的恩惠。
        佩戴时会熄灭身上火焰。
        完全取消右键使用。
        贴图已改为中式玄幻青玉、金扣、红结流苏风格。

    燃薪指环（ranxin_ring）：
        Curios 饰品。
        可装备在 ring 槽位。
        佩戴后持续获得火焰抗性。
        完全取消右键使用。
        贴图已改为中式玄幻金环、赤红火焰宝石风格。

    扶摇扇（fuyao_fan）：
        低阶法器。
        右键向前方卷起小旋风。
        小旋风会击退并扰乱敌人视线。

    道途指南（cultivation_guide_book）：
        模组内指南书。
        右键打开自定义指南界面。
        内容包含境界、蒲团修炼、灵气环境、炁、法宝、丹药、因果与常用指令。

Curios 依赖：
    Curios API 为必要依赖。
    neoforge.mods.toml 中声明 curios 为 required。
    玉佩和戒指通过 ICurioItem 在佩戴槽位中生效。
    已取消 Accessories 兼容与右键触发饰品效果。
    玩家 Curios 槽位：
        ring
        charm
        necklace

世界生成：
    灵矿生成：
        数据文件：
            worldgen/configured_feature/lingshi_ore.json
            worldgen/placed_feature/lingshi_ore.json
            neoforge/biome_modifier/add_lingshi_ore.json
        生物群系：
            #minecraft:is_overworld
        生成阶段：
            underground_ores
        生成逻辑参考原版钻石矿。

    灵植生成：
        数据文件：
            worldgen/configured_feature/lingzhi_plant.json
            worldgen/placed_feature/lingzhi_plant.json
            worldgen/placed_feature/lingzhi_plant_cold.json
            neoforge/biome_modifier/add_lingzhi_plant.json
            neoforge/biome_modifier/add_lingzhi_plant_cold.json
        生物群系：
            普通生成：#minecraft:is_overworld
            寒冷强化生成：#c:is_cold/overworld
        生成阶段：
            vegetal_decoration
        生成逻辑参考原版花。

常用指令：
    /dao qi set <targets> <amount>
        设置玩家炁。

    /dao qi add <targets> <amount>
        增加玩家炁。

    /dao realm set <targets> <realm>
        设置玩家大境界。

    /dao realm add <targets> <realm>
        增加玩家大境界。

    /dao realmProgress set <targets> <realmProgress>
        设置玩家修炼进度。

    /dao realmProgress add <targets> <realmProgress>
        增加玩家修炼进度。
