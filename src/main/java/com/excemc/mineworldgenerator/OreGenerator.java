package com.excemc.mineworldgenerator;

import java.util.List;
import java.util.Random;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

public class OreGenerator extends BlockPopulator {
    private final List<OreConfig> oreConfigs;

    public OreGenerator(ConfigManager configManager) {
        this.oreConfigs = configManager.getOreConfigs();
    }

    public void populate(World world, Random random, Chunk chunk) {
        for (OreConfig ore : this.oreConfigs) {
            this.generateOre(chunk, random, ore);
        }
    }

    private void generateOre(Chunk chunk, Random rand, OreConfig ore) {
        int y_rand = (int) (Math.random() * 10); //特殊结构悬空多少格
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                for (int y = ore.minY; y <= ore.maxY; ++y) {
                    if (chunk.getBlock(x, y, z).getType() != Material.STONE || !(rand.nextDouble() < ore.chance)) continue;
                    if (ore.type == Material.IRON_ORE && rand.nextDouble() < ore.specialStructureChance) {
                        this.generateIronOreStructure(chunk, x, y + y_rand, z);
                        continue;
                    }
                    this.generateOreCluster(chunk, rand, ore, x, y + y_rand , z);
                }
            }
        }
    }

    private void generateOreCluster(Chunk chunk, Random rand, OreConfig ore, int startX, int startY, int startZ) {
        int amount = ore.minAmount + rand.nextInt(ore.maxAmount - ore.minAmount + 1);
        int placed = 0;
        int currentX = startX;
        int currentY = startY;
        int currentZ = startZ;
        while (placed < amount && this.isValidPosition(chunk, currentX, currentY, currentZ)) {
            chunk.getBlock(currentX, currentY, currentZ).setType(ore.type);
            if (++placed >= amount) continue;
            int direction = rand.nextInt(6);
            switch (direction) {
                case 0: {
                    ++currentX;
                    break;
                }
                case 1: {
                    --currentX;
                    break;
                }
                case 2: {
                    ++currentY;
                    break;
                }
                case 3: {
                    --currentY;
                    break;
                }
                case 4: {
                    ++currentZ;
                    break;
                }
                case 5: {
                    --currentZ;
                }
            }
            if (currentX >= 0 && currentX < 16 && currentZ >= 0 && currentZ < 16 && currentY >= ore.minY && currentY <= ore.maxY) continue;
            break;
        }
    }

    private void generateIronOreStructure(Chunk chunk, int centerX, int centerY, int centerZ) {
        this.setBlockForStructure(chunk, centerX, centerY, centerZ, Material.COAL_ORE);
        this.setBlockForStructure(chunk, centerX - 2, centerY, centerZ, Material.COAL_ORE);
        this.setBlockForStructure(chunk, centerX - 1, centerY, centerZ, Material.IRON_ORE);
        this.setBlockForStructure(chunk, centerX + 1, centerY, centerZ, Material.IRON_ORE);
        this.setBlockForStructure(chunk, centerX + 2, centerY, centerZ, Material.COAL_ORE);
        this.setBlockForStructure(chunk, centerX, centerY, centerZ - 2, Material.COAL_ORE);
        this.setBlockForStructure(chunk, centerX, centerY, centerZ - 1, Material.IRON_ORE);
        this.setBlockForStructure(chunk, centerX, centerY, centerZ + 1, Material.IRON_ORE);
        this.setBlockForStructure(chunk, centerX, centerY, centerZ + 2, Material.COAL_ORE);
        this.setBlockForStructure(chunk, centerX, centerY - 2, centerZ, Material.COAL_ORE);
        this.setBlockForStructure(chunk, centerX, centerY - 1, centerZ, Material.IRON_ORE);
        this.setBlockForStructure(chunk, centerX, centerY + 1, centerZ, Material.IRON_ORE);
        this.setBlockForStructure(chunk, centerX, centerY + 2, centerZ, Material.COAL_ORE);
    }

    private void setBlockIfStone(Chunk chunk, int x, int y, int z) {
        if (this.isValidPosition(chunk, x, y, z)) {
            chunk.getBlock(x, y, z).setType(Material.STONE);
        }
    }

    private void setBlockIfStone(Chunk chunk, int x, int y, int z, Material material) {
        if (this.isValidPosition(chunk, x, y, z)) {
            chunk.getBlock(x, y, z).setType(material);
        }
    }

    private void setBlockForStructure(Chunk chunk, int x, int y, int z, Material material) {
        if (x < 0 || x >= 16 || z < 0 || z >= 16 || y < 0 || y >= 256) {
            return;
        }
        if (this.isNearSolidBlock(chunk, x, y, z, 5)) {
            chunk.getBlock(x, y, z).setType(material);
        }
    }

    private boolean isNearSolidBlock(Chunk chunk, int x, int y, int z, int maxDistance) {
        for (int dx = -maxDistance; dx <= maxDistance; ++dx) {
            for (int dy = -maxDistance; dy <= maxDistance; ++dy) {
                for (int dz = -maxDistance; dz <= maxDistance; ++dz) {
                    Material blockType;
                    if (dx == 0 && dy == 0 && dz == 0) continue;
                    int checkX = x + dx;
                    int checkY = y + dy;
                    int checkZ = z + dz;
                    if (checkX < 0 || checkX >= 16 || checkZ < 0 || checkZ >= 16 || checkY < 0 || checkY >= 256 || (blockType = chunk.getBlock(checkX, checkY, checkZ).getType()) != Material.STONE && blockType != Material.COAL_ORE && blockType != Material.IRON_ORE && blockType != Material.GOLD_ORE && blockType != Material.DIAMOND_ORE && blockType != Material.REDSTONE_ORE && blockType != Material.LAPIS_ORE && blockType != Material.EMERALD_ORE) continue;
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isValidPosition(Chunk chunk, int x, int y, int z) {
        return x >= 0 && x < 16 && z >= 0 && z < 16 && y >= 0 && y < 256 && chunk.getBlock(x, y, z).getType() == Material.STONE;
    }

    public static class OreConfig {
        public final Material type;
        public final int minY;
        public final int maxY;
        public final double chance;
        public final int minAmount;
        public final int maxAmount;
        public final double specialStructureChance;

        public OreConfig(Material type, int minY, int maxY, double chance, int minAmount, int maxAmount, double specialStructureChance) {
            this.type = type;
            this.minY = minY;
            this.maxY = maxY;
            this.chance = chance;
            this.minAmount = minAmount;
            this.maxAmount = maxAmount;
            this.specialStructureChance = specialStructureChance;
        }
    }
}
