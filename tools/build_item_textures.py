from pathlib import Path
import json
import re
from PIL import Image


ROOT = Path(__file__).resolve().parents[1]
TEXTURES = ROOT / "src/main/resources/assets/abyssredemptiondaomod/textures/item"
MODELS = ROOT / "src/main/resources/assets/abyssredemptiondaomod/models/item"
SOURCES = TEXTURES / "atlas_source"


def registered_entries():
    source = (ROOT / "src/main/java/com/abyssredemption/daomod/registry/ModItems.java").read_text(encoding="utf-8")
    return re.findall(r'new CodexEntry\("([^"]+)", "([^"]+)", "([^"]+)"\)', source)


def split_atlas(source, ids, columns, rows, key):
    atlas = Image.open(source).convert("RGBA")
    cell_width = atlas.width / columns
    cell_height = atlas.height / rows

    for index, item_id in enumerate(ids):
        column = index % columns
        row = index // columns
        bounds = (
            round(column * cell_width),
            round(row * cell_height),
            round((column + 1) * cell_width),
            round((row + 1) * cell_height),
        )
        icon = atlas.crop(bounds)
        pixels = icon.load()
        for y in range(icon.height):
            for x in range(icon.width):
                red, green, blue, _ = pixels[x, y]
                distance = ((red - key[0]) ** 2 + (green - key[1]) ** 2 + (blue - key[2]) ** 2) ** 0.5
                alpha = 0 if distance < 70 else (255 if distance > 150 else round((distance - 70) / 80 * 255))
                pixels[x, y] = (red, green, blue, alpha)

        alpha_bounds = icon.getchannel("A").getbbox()
        if alpha_bounds:
            icon = icon.crop(alpha_bounds)
        side = max(icon.size) + 12
        square = Image.new("RGBA", (side, side))
        square.alpha_composite(icon, ((side - icon.width) // 2, (side - icon.height) // 2))
        square.resize((32, 32), Image.Resampling.NEAREST).save(TEXTURES / f"{item_id}.png")

        model = {
            "parent": "minecraft:item/generated",
            "textures": {"layer0": f"abyssredemptiondaomod:item/{item_id}"},
        }
        (MODELS / f"{item_id}.json").write_text(json.dumps(model, indent=2) + "\n", encoding="utf-8")


def animate_artifact(item_id, rank):
    path = TEXTURES / f"{item_id}.png"
    base = Image.open(path).convert("RGBA")
    aura_color = {
        "heaven": (168, 92, 255),
        "ancient": (76, 224, 255),
        "divine": (255, 211, 86),
    }[rank]
    pulses = (0.0, 0.35, 0.7, 0.35)
    strip = Image.new("RGBA", (base.width, base.height * len(pulses)))

    for frame_index, pulse in enumerate(pulses):
        frame = Image.new("RGBA", base.size)
        source = base.load()
        target = frame.load()
        for y in range(base.height):
            for x in range(base.width):
                red, green, blue, alpha = source[x, y]
                if alpha:
                    light = 1.0 + pulse * 0.22
                    target[x, y] = (
                        min(255, round(red * light + aura_color[0] * pulse * 0.08)),
                        min(255, round(green * light + aura_color[1] * pulse * 0.08)),
                        min(255, round(blue * light + aura_color[2] * pulse * 0.08)),
                        alpha,
                    )
                    continue
                neighbor_alpha = 0
                for offset_x, offset_y in ((-1, 0), (1, 0), (0, -1), (0, 1)):
                    check_x, check_y = x + offset_x, y + offset_y
                    if 0 <= check_x < base.width and 0 <= check_y < base.height:
                        neighbor_alpha = max(neighbor_alpha, source[check_x, check_y][3])
                if neighbor_alpha:
                    target[x, y] = (*aura_color, round(neighbor_alpha * pulse * 0.42))
        strip.alpha_composite(frame, (0, frame_index * base.height))

    strip.save(path)
    metadata = {
        "animation": {
            "frametime": 6,
            "interpolate": False,
            "frames": [0, 1, 2, 3, 2, 1],
        }
    }
    path.with_suffix(path.suffix + ".mcmeta").write_text(
        json.dumps(metadata, indent=2) + "\n", encoding="utf-8")


def main():
    entries = registered_entries()
    ids = [entry[0] for entry in entries]
    artifacts = [item_id for item_id, category, _ in entries if category == "artifact"]
    herbs = [item_id for item_id, category, _ in entries if category == "herb"]
    ores = [item_id for item_id, category, _ in entries if category == "ore"]
    split_atlas(SOURCES / "artifacts.png", artifacts[:35], 8, 5, (0, 255, 0))
    split_atlas(SOURCES / "divine_artifacts.png", artifacts[35:], 4, 2, (0, 255, 0))
    split_atlas(SOURCES / "herbs.png", herbs, 6, 4, (255, 0, 255))
    split_atlas(SOURCES / "ores.png", ores, 5, 4, (0, 255, 0))
    for item_id, category, rank in entries:
        if category == "artifact" and rank in {"heaven", "ancient", "divine"}:
            animate_artifact(item_id, rank)
    preview = Image.new("RGBA", (12 * 48, 7 * 48), (30, 30, 34, 255))
    for index, item_id in enumerate(ids):
        texture = Image.open(TEXTURES / f"{item_id}.png").convert("RGBA")
        icon = texture.crop((0, 0, texture.width, texture.width)).resize((40, 40), Image.Resampling.NEAREST)
        preview.alpha_composite(icon, ((index % 12) * 48 + 4, (index // 12) * 48 + 4))
    preview.save(SOURCES / "preview.png")
    print(f"Built {len(ids)} item textures")


if __name__ == "__main__":
    main()
