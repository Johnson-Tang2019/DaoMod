# GPT Texture Sources

All runtime PNG textures under `src/main/resources/assets/abyssredemptiondaomod/textures/` are derived from online GPT image generation. Local processing is limited to atlas cropping, chroma-key removal, transparency cleanup, and nearest-neighbor resizing.

| Source | Runtime assets |
| --- | --- |
| `artifacts_atlas.png` | Mortal and earth artifact item textures |
| `high_rank_artifacts_atlas.png` | Heaven and ancient artifact item textures |
| `divine_artifacts_atlas.png` | Divine artifact item textures |
| `herbs_atlas.png` | 22 herb item textures |
| `ores_atlas.png` | 20 ore item textures |
| `hostile_beasts_atlas.png` | Four sect beasts and 25 legendary beasts |
| `legend_texture_repairs.png` | Repaired legend texture variants |
| `challenge_talismans.png` | Five challenge talismans |
| `dan_du_effects.png` | Two pill-toxin effect icons |
| `world_basics_atlas.png` | Putuan, lingzhi plant, lingshi ore, and sword beam |
| `equipment_atlas.png` | Nine named weapons, Wenling dagger, two Curios accessories, Fuyao fan, and pill |
| `ore_blocks_atlas.png` | 21 opaque full-face ore block textures |

The herb and ore world blocks reuse their corresponding GPT item textures in block models. No generated source atlas is shipped in the runtime texture namespace.
