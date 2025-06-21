package com.excemc.mineworldgenerator;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class WorldGenerator extends ChunkGenerator {
    // 地形参数
    private static final int BASE_HEIGHT = 60;
    private static final int HEIGHT_VARIATION = 50;
    private static final double NOISE_SCALE = 0.05;

    // 地层材质
    private static final Material[] STRATUM = {
            Material.STONE,
            Material.STONE,
            Material.STONE,
            Material.STONE
    };

    private final ConfigManager configManager;

    public WorldGenerator(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public byte[][] generateBlockSections(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
        byte[][] chunkSections = new byte[world.getMaxHeight() >> 4][]; // 16x16x16 sections

        // 设置生物群系
        setBiomeGrid(biome);

        // 生成地形
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int worldX = (chunkX << 4) + x;
                int worldZ = (chunkZ << 4) + z;

                // 计算地表高度
                double noise = fractalNoise(worldX, worldZ, 3);
                int surfaceY = BASE_HEIGHT + (int)(noise * HEIGHT_VARIATION);

                // 填充方块
                for (int y = 0; y <= surfaceY; y++) {
                    Material material = getMaterialForLayer(y, surfaceY);
                    setBlock(chunkSections, x, y, z, material);
                }
            }
        }

        return chunkSections;
    }

    private Material getMaterialForLayer(int y, int surfaceY) {
        if (y == 0) return Material.BEDROCK;
        if (y == surfaceY) return STRATUM[0];
        if (y > surfaceY - 3) return STRATUM[1];
        if (y > surfaceY - 6) return STRATUM[2];
        return STRATUM[3];
    }

    private void setBlock(byte[][] chunkSections, int x, int y, int z, Material material) {
        int section = y >> 4;
        if (chunkSections[section] == null) {
            chunkSections[section] = new byte[4096];
        }
        int index = (y & 0xF) << 8 | z << 4 | x;
        chunkSections[section][index] = (byte) material.getId();
    }

    private void setBiomeGrid(BiomeGrid biome) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                biome.setBiome(x, z, Biome.PLAINS);
            }
        }
    }

    private double fractalNoise(int x, int z, int octaves) {
        double total = 0;
        double frequency = 1.0;
        double amplitude = 1.0;
        double maxValue = 0;

        for (int i = 0; i < octaves; i++) {
            total += noise(x * frequency * NOISE_SCALE, z * frequency * NOISE_SCALE) * amplitude;
            maxValue += amplitude;
            amplitude *= 0.5;
            frequency *= 2;
        }

        return total / maxValue;
    }

    private double noise(double x, double z) {
        return (Math.sin(x) * Math.cos(z) + 1) / 2;
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        return Arrays.asList(new OreGenerator(configManager));
    }
}
