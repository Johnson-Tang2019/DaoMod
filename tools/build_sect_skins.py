from pathlib import Path
from PIL import Image, ImageDraw


ROOT = Path(__file__).resolve().parents[1]
OUTPUT = ROOT / "src/main/resources/assets/abyssredemptiondaomod/textures/entity/sect"
LEGEND_OUTPUT = ROOT / "src/main/resources/assets/abyssredemptiondaomod/textures/entity/legend"
ITEM_OUTPUT = ROOT / "src/main/resources/assets/abyssredemptiondaomod/textures/item"

SECTS = {
    "sword_attendant": {
        "robe": "#DDE8F2", "shade": "#8199B5", "trim": "#4B78A8", "hair": "#252B38", "accent": "#B9E4FF"
    },
    "creation_disciple": {
        "robe": "#D7C89C", "shade": "#88754E", "trim": "#3F806B", "hair": "#3A2B20", "accent": "#E7C45F"
    },
    "thunder_monk": {
        "robe": "#A54832", "shade": "#672820", "trim": "#D29A35", "hair": "#5B3B25", "accent": "#F2CC68"
    },
    "rakshasa_cultist": {
        "robe": "#2D2339", "shade": "#17121F", "trim": "#9E2639", "hair": "#101014", "accent": "#CF4056"
    },
}

LEGENDS = {
    "xuanyuan": {"robe": "#C9A34D", "shade": "#704529", "trim": "#F0D77A", "hair": "#29231F", "accent": "#FFF0A0"},
    "dugu_qiubai": {"robe": "#D8D8D6", "shade": "#5B5963", "trim": "#24222C", "hair": "#F0F0ED", "accent": "#A88CFF"},
    "qinglian_jianxian": {"robe": "#DDEBD8", "shade": "#4D806A", "trim": "#69B890", "hair": "#252B2A", "accent": "#B8F0D0"},
    "ye_gucheng": {"robe": "#E1EFF7", "shade": "#6687A2", "trim": "#A9D6EF", "hair": "#D5E2E8", "accent": "#FFFFFF"},
    "shen_duzhou": {"robe": "#344A68", "shade": "#182438", "trim": "#7DAFD0", "hair": "#171C26", "accent": "#D4EEFF"},
    "nameless_artificer": {"robe": "#716451", "shade": "#3F382E", "trim": "#D6B86B", "hair": "#302922", "accent": "#F0DD99"},
    "gongshu_ban": {"robe": "#43545B", "shade": "#242E35", "trim": "#6BC7B7", "hair": "#252A2C", "accent": "#A6FFF0"},
    "ge_hong": {"robe": "#EEE5C4", "shade": "#9B8163", "trim": "#B84937", "hair": "#D7D3C6", "accent": "#F3C95A"},
    "ouye_zi": {"robe": "#63312A", "shade": "#351C1A", "trim": "#D87835", "hair": "#242020", "accent": "#FFB34E"},
    "su_hongyi": {"robe": "#A72E48", "shade": "#591A2C", "trim": "#D9B956", "hair": "#171419", "accent": "#FFE398"},
    "ming_wang": {"robe": "#24212F", "shade": "#111019", "trim": "#765AA8", "hair": "#0D0C11", "accent": "#B99AE8"},
    "dizang": {"robe": "#8C482E", "shade": "#4A281D", "trim": "#D3AF55", "hair": "#33251E", "accent": "#F5DA82"},
    "damo": {"robe": "#A75431", "shade": "#5F2B20", "trim": "#E2B45A", "hair": "#2C211C", "accent": "#FFE09A"},
    "blood_river_ancestor": {"robe": "#711523", "shade": "#350B13", "trim": "#D33A45", "hair": "#1D1013", "accent": "#FF6970"},
    "wuhua": {"robe": "#E9E2D9", "shade": "#918279", "trim": "#B8203A", "hair": "#201C1D", "accent": "#F25A68"},
    "mo_xuan": {"robe": "#252532", "shade": "#101017", "trim": "#725D9B", "hair": "#14131A", "accent": "#AE91E5"},
    "guanghan_fairy": {"robe": "#E5F3F5", "shade": "#84AFC4", "trim": "#BFE9F4", "hair": "#D8E2E5", "accent": "#FFFFFF"},
    "fengshen_daoist": {"robe": "#D6C58F", "shade": "#716B51", "trim": "#59675E", "hair": "#4A443B", "accent": "#F5E8A8"},
    "grave_keeper": {"robe": "#535149", "shade": "#292A27", "trim": "#729083", "hair": "#C6C1AD", "accent": "#9BCDB7"},
    "ning_fan": {"robe": "#353946", "shade": "#1B1D25", "trim": "#944253", "hair": "#1C1B20", "accent": "#DB778B"},
    "kuafu": {"robe": "#9D542C", "shade": "#552B20", "trim": "#E2A441", "hair": "#38251D", "accent": "#FFD86A"},
    "jingwei": {"robe": "#4E7D9B", "shade": "#29475E", "trim": "#D8534C", "hair": "#20252B", "accent": "#F0D95B"},
    "houyi": {"robe": "#5F523B", "shade": "#332E25", "trim": "#88C2CE", "hair": "#26231F", "accent": "#C8F4FA"},
    "xingtian": {"robe": "#6D312B", "shade": "#381A19", "trim": "#C57D39", "hair": "#1C1717", "accent": "#F0A44D"},
    "xuanyuan_fourteen": {"robe": "#484B50", "shade": "#272A2E", "trim": "#81796B", "hair": "#292929", "accent": "#B4AA92"},
}

CHALLENGE_TALISMANS = {
    "sword_legends_challenge": ("#DCECF4", "#4C7FA3", "#E6C65C"),
    "creation_masters_challenge": ("#E6D4A0", "#4F806A", "#BB6D35"),
    "buddha_demon_challenge": ("#E7C96B", "#8A2438", "#F2E5BD"),
    "forbidden_ones_challenge": ("#393044", "#9B70C5", "#B7D7D0"),
    "ancient_bloodline_challenge": ("#74503A", "#D28A42", "#D7C7A1"),
}


def rgb(value):
    value = value.lstrip("#")
    return tuple(int(value[index:index + 2], 16) for index in (0, 2, 4)) + (255,)


def build_skin(name, palette, output=OUTPUT):
    image = Image.new("RGBA", (64, 64))
    draw = ImageDraw.Draw(image)
    skin = (214, 174, 132, 255)
    robe, shade, trim, hair, accent = (rgb(palette[key]) for key in ("robe", "shade", "trim", "hair", "accent"))

    # Base UV islands for head, torso, arms and legs.
    draw.rectangle((0, 0, 31, 15), fill=skin)
    draw.rectangle((16, 16, 39, 31), fill=robe)
    draw.rectangle((40, 16, 55, 31), fill=robe)
    draw.rectangle((0, 16, 15, 31), fill=shade)
    draw.rectangle((16, 48, 31, 63), fill=shade)
    draw.rectangle((32, 48, 47, 63), fill=robe)

    # Hair, brows and eyes on the front face.
    draw.rectangle((8, 8, 15, 10), fill=hair)
    draw.point((9, 11), fill=(38, 45, 55, 255))
    draw.point((14, 11), fill=(38, 45, 55, 255))
    draw.line((8, 8, 15, 8), fill=accent, width=1)
    draw.rectangle((0, 8, 7, 11), fill=hair)
    draw.rectangle((16, 8, 23, 11), fill=hair)
    draw.rectangle((24, 8, 31, 15), fill=hair)

    # Robe lapels, belt, cuffs and boots across their UV islands.
    draw.line((20, 20, 23, 25), fill=trim, width=1)
    draw.line((27, 20, 24, 25), fill=trim, width=1)
    draw.rectangle((20, 26, 27, 28), fill=trim)
    draw.rectangle((44, 28, 47, 31), fill=trim)
    draw.rectangle((36, 60, 39, 63), fill=trim)
    draw.rectangle((4, 28, 7, 31), fill=hair)
    draw.rectangle((20, 60, 23, 63), fill=hair)

    # Outer robe layer and sect crest.
    draw.rectangle((16, 32, 39, 47), fill=(0, 0, 0, 0))
    draw.rectangle((20, 36, 27, 47), outline=(*trim[:3], 170))
    draw.point((23, 39), fill=accent)
    draw.point((24, 38), fill=accent)
    draw.point((24, 40), fill=accent)
    draw.point((25, 39), fill=accent)

    # Headwear on the outer head layer differs by sect.
    if name == "thunder_monk":
        draw.rectangle((40, 8, 47, 9), fill=accent)
    else:
        draw.rectangle((40, 8, 47, 10), fill=hair)
        draw.rectangle((43, 6, 44, 8), fill=accent)

    output.mkdir(parents=True, exist_ok=True)
    image.save(output / f"{name}.png")


def build_talisman(name, colors):
    paper, ink, seal = (rgb(color) for color in colors)
    image = Image.new("RGBA", (16, 16), (0, 0, 0, 0))
    draw = ImageDraw.Draw(image)
    draw.polygon(((3, 1), (12, 1), (13, 3), (12, 14), (9, 13), (7, 15), (5, 13), (3, 14), (2, 3)), fill=paper)
    draw.line((4, 4, 11, 4), fill=ink, width=1)
    draw.line((7, 3, 7, 11), fill=ink, width=1)
    draw.line((5, 7, 10, 7), fill=ink, width=1)
    draw.rectangle((5, 10, 10, 13), outline=seal)
    draw.point((7, 11), fill=seal)
    draw.point((8, 12), fill=seal)
    ITEM_OUTPUT.mkdir(parents=True, exist_ok=True)
    image.save(ITEM_OUTPUT / f"{name}.png")


def main():
    for name, palette in SECTS.items():
        build_skin(name, palette)
    for name, palette in LEGENDS.items():
        build_skin(name, palette, LEGEND_OUTPUT)
    for name, colors in CHALLENGE_TALISMANS.items():
        build_talisman(name, colors)
    print(f"Built {len(SECTS)} sect guardian, {len(LEGENDS)} legendary skins and "
          f"{len(CHALLENGE_TALISMANS)} challenge talismans")


if __name__ == "__main__":
    main()
