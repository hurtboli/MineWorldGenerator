package com.excemc.mineworldgenerator;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager {
    private final JavaPlugin plugin;
    private final List<OreGenerator.OreConfig> oreConfigs = new ArrayList<OreGenerator.OreConfig>();
    private final List<LiquidGenerator.LiquidConfig> liquidConfigs = new ArrayList<LiquidGenerator.LiquidConfig>();

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
        this.loadConfig();
    }

    private void loadConfig() {
        FileConfiguration config = this.plugin.getConfig();
        
        // 加载矿石配置
        if (!config.contains("ores")) {
            this.plugin.getLogger().warning("未找到矿石配置，使用默认配置");
            this.setDefaultOres();
        } else {
            for (String key : config.getConfigurationSection("ores").getKeys(false)) {
                Material type = Material.getMaterial(key.toUpperCase());
                if (type == null) continue;
                String amountsStr = config.getString("ores." + key + ".amounts", "1-1");
                String[] parts = amountsStr.split("-");
                int minAmount = Integer.parseInt(parts[0].trim());
                int maxAmount = parts.length > 1 ? Integer.parseInt(parts[1].trim()) : minAmount;
                double specialStructureChance = config.getDouble("ores." + key + ".specialStructureChance", 0.0);
                this.oreConfigs.add(new OreGenerator.OreConfig(type, config.getInt("ores." + key + ".minY"), config.getInt("ores." + key + ".maxY"), config.getDouble("ores." + key + ".chance"), minAmount, maxAmount, specialStructureChance));
            }
        }
        
        // 加载流体配置
        if (!config.contains("liquid")) {
            this.plugin.getLogger().warning("未找到流体配置，使用默认配置");
            this.setDefaultLiquids();
        } else {
            for (String key : config.getConfigurationSection("liquid").getKeys(false)) {
                Material type = null;
                // 处理流体材质名称
                if (key.equalsIgnoreCase("WATER")) {
                    type = Material.WATER;
                } else if (key.equalsIgnoreCase("LAVA")) {
                    type = Material.LAVA;
                } else {
                    type = Material.getMaterial(key.toUpperCase());
                }
                
                if (type == null) continue;
                
                int minY = config.getInt("liquid." + key + ".minY", 1);
                int maxY = config.getInt("liquid." + key + ".maxY", 30);
                double chance = config.getDouble("liquid." + key + ".chance", 0.001);
                
                this.liquidConfigs.add(new LiquidGenerator.LiquidConfig(type, minY, maxY, chance));
                this.plugin.getLogger().info("加载流体配置: " + key + " (Y:" + minY + "-" + maxY + ", 几率:" + chance + ")");
            }
        }
    }

    private void setDefaultOres() {
        this.oreConfigs.add(new OreGenerator.OreConfig(Material.COAL_ORE, 1, 60, 0.015, 1, 5, 0.0));
        this.oreConfigs.add(new OreGenerator.OreConfig(Material.IRON_ORE, 1, 40, 0.01, 1, 5, 0.15));
    }
    
    private void setDefaultLiquids() {
        this.liquidConfigs.add(new LiquidGenerator.LiquidConfig(Material.WATER, 1, 30, 0.001));
        this.liquidConfigs.add(new LiquidGenerator.LiquidConfig(Material.LAVA, 1, 30, 0.001));
    }

    public List<OreGenerator.OreConfig> getOreConfigs() {
        return new ArrayList<OreGenerator.OreConfig>(this.oreConfigs);
    }
    
    public List<LiquidGenerator.LiquidConfig> getLiquidConfigs() {
        return new ArrayList<LiquidGenerator.LiquidConfig>(this.liquidConfigs);
    }
}
