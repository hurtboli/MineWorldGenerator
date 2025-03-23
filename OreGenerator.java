package com.excemc.mineworldgenerator;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import java.util.List;
import java.util.Random;

public class OreGenerator extends BlockPopulator {

    public static class OreConfig {
        public final Material type;
        public final int minY;
        public final int maxY;
        public final double chance;

        public OreConfig(Material type, int minY, int maxY, double chance) {
            this.type = type;
            this.minY = minY;
            this.maxY = maxY;
            this.chance = chance;
        }
    }

    private final List<OreConfig> oreConfigs;

    public OreGenerator(ConfigManager configManager) {
        this.oreConfigs = configManager.getOreConfigs();
    }

    @Override
    public void populate(World world, Random random, Chunk chunk) {
        for (OreConfig ore : oreConfigs) {
            generateOre(chunk, random, ore);
        }
    }

    private void generateOre(Chunk chunk, Random rand, OreConfig ore) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = ore.minY; y <= ore.maxY; y++) {
                    if (chunk.getBlock(x, y, z).getType() == Material.STONE && rand.nextDouble() < ore.chance) {
                        chunk.getBlock(x, y, z).setType(ore.type);
                    }
                }
            }
        }
    }
}