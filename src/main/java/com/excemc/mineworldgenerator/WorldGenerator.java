package com.excemc.mineworldgenerator;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

public class WorldGenerator extends ChunkGenerator {
    private final ConfigManager configManager;

    public WorldGenerator(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
        ChunkData chunk = this.createChunkData(world);
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                int worldX = chunkX * 16 + x;
                int worldZ = chunkZ * 16 + z;
                int height = this.calculateTerrainHeight(worldX, worldZ);
                chunk.setBlock(x, 0, z, Material.BEDROCK);
                for (int y = 1; y <= height; ++y) {
                    chunk.setBlock(x, y, z, Material.STONE);
                }
            }
        }
        return chunk;
    }

    private int calculateTerrainHeight(int x, int z) {
        // 主噪声 - 增加频率产生更多起伏
        double mainNoise = this.perlinNoise((double)x * 0.015, (double)z * 0.015, 0);
        
        // 次级噪声 - 大尺度变化
        double secondaryNoise = this.perlinNoise((double)x * 0.007, (double)z * 0.007, 1000);
        
        // 细节噪声 - 小尺度变化，增加权重
        double detailNoise = this.perlinNoise((double)x * 0.06, (double)z * 0.06, 2000);
        
        // 额外的正弦波叠加，产生更规律的波浪形山脉
        double sineWave = Math.sin((double)x * 0.04) * Math.cos((double)z * 0.04) * 0.3;
        double sineWave2 = Math.sin((double)x * 0.008 + 1.5) * Math.cos((double)z * 0.01 + 0.8) * 0.2;
        
        // 调整权重：增加主噪声和正弦波的影响，减少平滑效果
        double combinedNoise = mainNoise * 0.45 + secondaryNoise * 0.10 + detailNoise * 0.1 + sineWave + sineWave2;
        
        // 归一化到 [0, 1]
        double normalizedNoise = (combinedNoise + 1.0) / 2.0;
        
        // 只使用一次 smoothstep，保留更多起伏
        normalizedNoise = this.smoothstep(normalizedNoise);
        
        // 使用更小的指数，减少平顶效果
        normalizedNoise = Math.pow(normalizedNoise, 1.2);
        
        // 限制在合理范围
        normalizedNoise = Math.max(0.0, Math.min(1.0, normalizedNoise));
        
        int minHeight = 100;
        int maxHeight = 240;
        int height = (int)((double)minHeight + normalizedNoise * (double)(maxHeight - minHeight));
        return Math.max(minHeight, Math.min(maxHeight, height));
    }

    private double perlinNoise(double x, double z, int seed) {
        int xi = (int)Math.floor(x);
        int zi = (int)Math.floor(z);
        double xf = x - (double)xi;
        double zf = z - (double)zi;
        double u = this.fade(xf);
        double v = this.fade(zf);
        int aa = this.hash(xi, zi, seed);
        int ab = this.hash(xi, zi + 1, seed);
        int ba = this.hash(xi + 1, zi, seed);
        int bb = this.hash(xi + 1, zi + 1, seed);
        double x1 = this.lerp(this.grad(aa, xf, zf), this.grad(ba, xf - 1.0, zf), u);
        double x2 = this.lerp(this.grad(ab, xf, zf - 1.0), this.grad(bb, xf - 1.0, zf - 1.0), u);
        return this.lerp(x1, x2, v);
    }

    private int hash(int x, int z, int seed) {
        int h = seed + x * 374761393 + z * 668265263;
        h = (h ^ h >> 13) * 1274126177;
        return h ^ h >> 16;
    }

    private double grad(int hash, double x, double z) {
        int h = hash & 7;
        double u = h < 4 ? x : z;
        double v = h < 4 ? z : x;
        return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
    }

    private double fade(double t) {
        return t * t * t * (t * (t * 6.0 - 15.0) + 10.0);
    }

    private double lerp(double a, double b, double t) {
        return a + t * (b - a);
    }

    private double smoothstep(double t) {
        return t * t * (3.0 - 2.0 * t);
    }

    public List<BlockPopulator> getDefaultPopulators(World world) {
        return Arrays.asList(
            new OreGenerator(this.configManager),
            new LiquidGenerator(this.configManager)
        );
    }
}
