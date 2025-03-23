# MineWorldGenerator - Minecraft自定义世界生成插件

![Minecraft](https://img.shields.io/badge/Minecraft-1.12--1.20-blueviolet) ![License](https://img.shields.io/badge/License-MIT-green)

## 📖 项目简介
这是一个基于Bukkit/Spigot API开发的自定义世界生成插件，通过分形噪声算法生成动态地形，支持可配置的矿石分布系统[1,2](@ref)。适用于需要自定义矿场世界的服务器场景。

## ✨ 核心特性
### 🏔️ 动态地形生成
- 使用分形噪声算法生成起伏地形（BASE_HEIGHT=60，高度波动±50）
- 四层地层结构：基岩层→表层岩石→中间层→深层岩石
- 强制设置生物群系为平原（Biome.PLAINS）

### ⛏️ 智能矿石系统
```yaml
# 示例配置（config.yml）
ores:
  COAL_ORE:
    minY: 1
    maxY: 60
    chance: 0.015
  DIAMOND_ORE:
    minY: 5
    maxY: 16
    chance: 0.005
