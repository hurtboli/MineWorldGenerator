package com.excemc.mineworldgenerator;

import java.util.List;
import java.util.Random;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

public class LiquidGenerator extends BlockPopulator {
    private final List<LiquidConfig> liquidConfigs;

    public LiquidGenerator(ConfigManager configManager) {
        this.liquidConfigs = configManager.getLiquidConfigs();
    }

    @Override
    public void populate(World world, Random random, Chunk chunk) {
        for (LiquidConfig liquid : this.liquidConfigs) {
            this.generateLiquid(chunk, random, liquid);
        }
    }

    private void generateLiquid(Chunk chunk, Random rand, LiquidConfig liquid) {
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                for (int y = liquid.minY; y <= liquid.maxY; ++y) {
                    Block block = chunk.getBlock(x, y, z);
                    if (block.getType() == Material.STONE && rand.nextDouble() < liquid.chance) {
                        // 检查是否可以生成流体
                        if (canGenerateLiquid(chunk, x, y, z)) {
                            // 只生成单格流体
                            chunk.getBlock(x, y, z).setType(liquid.type);
                        }
                    }
                }
            }
        }
    }

    /**
     * 检查是否可以在该位置生成流体
     * 需要确保周围有足够的石头包围
     */
    private boolean canGenerateLiquid(Chunk chunk, int x, int y, int z) {
        // 检查下方是否有石头支撑
        if (y <= 1) return false;
        Block below = chunk.getBlock(x, y - 1, z);
        if (below.getType() != Material.STONE) return false;
        
        // 检查周围至少有3个方向被石头包围
        int surroundCount = 0;
        if (x > 0 && chunk.getBlock(x - 1, y, z).getType() == Material.STONE) surroundCount++;
        if (x < 15 && chunk.getBlock(x + 1, y, z).getType() == Material.STONE) surroundCount++;
        if (z > 0 && chunk.getBlock(x, y, z - 1).getType() == Material.STONE) surroundCount++;
        if (z < 15 && chunk.getBlock(x, y, z + 1).getType() == Material.STONE) surroundCount++;
        
        return surroundCount >= 3;
    }

    /**
     * 流体配置类
     */
    public static class LiquidConfig {
        public final Material type;
        public final int minY;
        public final int maxY;
        public final double chance;

        public LiquidConfig(Material type, int minY, int maxY, double chance) {
            this.type = type;
            this.minY = minY;
            this.maxY = maxY;
            this.chance = chance;
        }
    }
}
