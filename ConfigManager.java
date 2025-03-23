package com.excemc.mineworldgenerator;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {
    private final JavaPlugin plugin;
    private final List<OreGenerator.OreConfig> oreConfigs = new ArrayList<>();

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
        loadConfig();
    }

    private void loadConfig() {
        FileConfiguration config = plugin.getConfig();
        if (!config.contains("ores")) {
            plugin.getLogger().warning("未找到矿石配置，使用默认配置");
            setDefaultOres();
            return;
        }

        for (String key : config.getConfigurationSection("ores").getKeys(false)) {
            Material type = Material.getMaterial(key.toUpperCase());
            if (type != null) {
                oreConfigs.add(new OreGenerator.OreConfig(
                        type,
                        config.getInt("ores." + key + ".minY"),
                        config.getInt("ores." + key + ".maxY"),
                        config.getDouble("ores." + key + ".chance")
                ));
            }
        }
    }

    private void setDefaultOres() {
        oreConfigs.add(new OreGenerator.OreConfig(Material.COAL_ORE, 1, 60, 0.015));
        oreConfigs.add(new OreGenerator.OreConfig(Material.IRON_ORE, 1, 40, 0.01));
    }

    public List<OreGenerator.OreConfig> getOreConfigs() {
        return new ArrayList<>(oreConfigs);
    }
}