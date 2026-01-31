package com.excemc.mineworldgenerator;

import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private ConfigManager configManager;

    public void onEnable() {
        this.configManager = new ConfigManager(this);
        this.getLogger().info("矿场世界生成器已加载");
    }

    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return new WorldGenerator(this.configManager);
    }
}
