# MineWorldGenerator - MC自定义矿场世界生成插件

![Minecraft](https://img.shields.io/badge/Minecraft-1.12-LightGreen) ![Minecraft](https://img.shields.io/badge/Minecraft-1.21.11-LightGreen) ![License](https://img.shields.io/badge/License-MIT-green)

![Static Badge](https://img.shields.io/badge/Java-8-orange) ![Static Badge](https://img.shields.io/badge/Java-21-orange) ![Static Badge](https://img.shields.io/badge/Java-25-orange)

## 📖 项目简介
这是一个基于SpigotAPI开发的自定义矿场世界生成插件，通过分形噪声算法生成动态地形，支持可配置的矿石分布系统。适用于需要自定义矿场世界的服务器场景。

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
```

##  🚀 版本支持
该插件在以下版本测试可用：
- Spigot 1.12.2
- Paper 1.12.2 1.21.11

##  🛠️ 快速安装
1.  将编译后的MineWorldGenerator.jar放入服务器plugins/目录
2.  重启服务器自动生成配置文件
3.  新建世界时选择本生成器：
```Command
/world create 自定义世界名称 normal -g MineWorldGenerator
```

##  🧩 开发者文档
###  核心类说明
-  WorldGenerator：地形生成主类，实现ChunkGenerator接口
-  OreGenerator：矿石生成器，继承BlockPopulator
-  ConfigManager：处理YAML配置文件
###  扩展开发
```java
// 自定义矿石生成器示例
public class CustomOreGenerator extends BlockPopulator {
    @Override
    public void populate(World world, Random random, Chunk chunk) {
        // 实现自定义生成逻辑
    }
}
```

##  📜 开源协议
-  本项目采用 MIT License，欢迎二次开发与商业使用。使用本插件时需遵循Minecraft EULA。
-  原作者：hurtboli(From [ExcellentMC](https://www.excemc.com))

##  📦 编译说明
### 🎛️  编译环境
-  Java (With JDK)
-  Maven (3.2+ Better)
### 📘  编译步骤
该插件使用Maven编译，需要将代码下载解压到某一文件夹后通过以下步骤编译：
-  从文件夹打开CMD/PowerShell窗口
-  输入以下命令:
```PowerShell
mvn clean package
```
-  等待出现以下字段：
```PowerShell
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```
- 然后在该文件夹下的`target/`目录下就能找到编译成功的插件
  
## 📥 下载最新版本
[Download](https://github.com/hurtboli/MineWorldGenerator/releases)
