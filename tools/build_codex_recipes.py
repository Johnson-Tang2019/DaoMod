from pathlib import Path
import json


ROOT = Path(__file__).resolve().parents[1]
DATA = ROOT / "src/main/resources/data/abyssredemptiondaomod"
RECIPES = DATA / "recipe"
TAGS = DATA / "tags/item/materials"
MOD = "abyssredemptiondaomod:"


MATERIAL_TIERS = {
    "mortal": ["chenyin_jingtie", "hantie_kuang", "chitong_shi", "lingci_sha"],
    "earth": ["gengjin_shenkuang", "lihuo_chijin", "hantan_yintie", "lihuo_sha", "chenyuan_zhongshi", "daoling_yu"],
    "heaven": ["kongming_shi", "wuse_shenshi", "xingchen_yuntie", "xukong_jing", "zhanshen_xuanjin", "longxue_shi"],
    "ancient": ["leijie_jin", "butian_wuse_shi", "suiyue_sha", "hunyuan_zushi"],
}

MATERIAL_RECIPES = {
    "chenyin_jingtie": ["minecraft:iron_ingot", "minecraft:quartz"],
    "hantie_kuang": ["minecraft:iron_ingot", "minecraft:packed_ice"],
    "chitong_shi": ["minecraft:copper_ingot", "minecraft:redstone"],
    "lingci_sha": ["minecraft:iron_nugget", "minecraft:redstone", "minecraft:sand"],
    "gengjin_shenkuang": ["minecraft:gold_ingot", "minecraft:diamond"],
    "lihuo_chijin": ["minecraft:gold_ingot", "minecraft:blaze_powder"],
    "hantan_yintie": ["minecraft:iron_ingot", "minecraft:blue_ice"],
    "lihuo_sha": ["minecraft:sand", "minecraft:blaze_powder", "minecraft:fire_charge"],
    "chenyuan_zhongshi": ["minecraft:deepslate", "minecraft:obsidian"],
    "daoling_yu": ["minecraft:emerald", "minecraft:amethyst_shard"],
}

ARTIFACT_RECIPES = {
    "wenling_dagger": ["hantie_kuang", "chenyin_jingtie", "juling_cao"],
    "bishui_xichanzhu": ["chenyin_jingtie", "lingci_sha", "shexian_guo"],
    "ranxin_ring": ["chitong_shi", "lingci_sha", "bihuo_shenzao"],
    "zhiyuan_feixingnu": ["chenyin_jingtie", "chitong_shi", "jianyi_cao"],
    "naying_ping": ["hantie_kuang", "lingci_sha", "yingguang_tai"],
    "ganshan_bian": ["chenyin_jingtie", "chitong_shi", "huoxue_teng"],
    "cangjing_ye": ["lingci_sha", "juling_cao", "yingguang_tai"],
    "jieling_suo": ["chenyin_jingtie", "lingci_sha", "tunling_teng"],
    "zhaodan_jing": ["chenyin_jingtie", "chitong_shi", "yingguang_tai"],
    "fuyao_fan": ["chitong_shi", "lingci_sha", "jianyi_cao"],
    "shanhe_cuo": ["chenyuan_zhongshi", "daoling_yu", "tiegu_hua"],
    "lihuo_fentian_ling": ["lihuo_chijin", "lihuo_sha", "bihuo_shenzao"],
    "fuyu_fanyun_gai": ["hantan_yintie", "daoling_yu", "shexian_guo"],
    "fulong_jinsuo": ["gengjin_shenkuang", "chenyuan_zhongshi", "tunling_teng"],
    "chuanxiao_suo": ["gengjin_shenkuang", "daoling_yu", "jianyi_cao"],
    "zhenmo_yin": ["gengjin_shenkuang", "daoling_yu", "kurong_shen"],
    "yingsha_bi": ["hantan_yintie", "chenyuan_zhongshi", "qianji_yin"],
    "juling_wanqi_zhen": ["daoling_yu", "lihuo_chijin", "juling_cao"],
    "jianqiuyue": ["hantan_yintie", "gengjin_shenkuang", "jihan_binglian"],
    "yinyang_liangjie_bei": ["chenyuan_zhongshi", "daoling_yu", "yinyang_nizhuan_cao"],
}


def write_json(path, value):
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(value, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")


def shapeless(result, ingredients):
    return {
        "type": "minecraft:crafting_shapeless",
        "category": "misc",
        "ingredients": [{"item": ingredient} for ingredient in ingredients],
        "result": {"id": result},
    }


def main():
    for tier, values in MATERIAL_TIERS.items():
        write_json(TAGS / f"{tier}.json", {"replace": False, "values": [MOD + value for value in values]})

    for result, ingredients in MATERIAL_RECIPES.items():
        write_json(RECIPES / f"material_{result}.json", shapeless(MOD + result, ingredients))

    for result, ingredients in ARTIFACT_RECIPES.items():
        inputs = [MOD + ingredient for ingredient in ingredients]
        inputs.append(MOD + "lingshi_ore")
        write_json(RECIPES / f"artifact_{result}.json", shapeless(MOD + result, inputs))

    print(f"Built {len(MATERIAL_RECIPES)} material and {len(ARTIFACT_RECIPES)} artifact recipes")


if __name__ == "__main__":
    main()
