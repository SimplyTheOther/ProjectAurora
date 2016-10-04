package projectaurora.world.gen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenerator;
import projectaurora.world.biome.AuroraBiome;

public class WorldGenLake extends WorldGenerator {
    private Block liquidToGen;
    private int liquidToGenMeta;
    private Block replaceBlock;
    private int replaceBlockMeta;

    public WorldGenLake(Block liquid, int metaOfLiquid, Block blockToReplace, int metaOfBlockToReplace) {
    	liquidToGen = liquid;
    	liquidToGenMeta = metaOfLiquid;
    	replaceBlock = blockToReplace;
    	replaceBlockMeta = metaOfBlockToReplace;
    }
    
    public WorldGenLake(Block liquid, int metaOfLiquid, AuroraBiome biome) {
    	this(liquid, metaOfLiquid, biome.stoneBlock, biome.stoneBlockMeta);
    }

    @Override
    public boolean generate(World world, Random random, int x, int y, int z) {
        x -= 8;

        for (z -= 8; y > 5 && world.isAirBlock(x, y, z); --y) {
            ;
        }

        if (y <= 4) {
            return false;
        } else {
            y -= 4;
            boolean[] aboolean = new boolean[2048];
            int l = random.nextInt(4) + 4;
            int xOffset;

            for (xOffset = 0; xOffset < l; ++xOffset) {
                double d0 = random.nextDouble() * 6.0D + 3.0D;
                double d1 = random.nextDouble() * 4.0D + 2.0D;
                double d2 = random.nextDouble() * 6.0D + 3.0D;
                double d3 = random.nextDouble() * (16.0D - d0 - 2.0D) + 1.0D + d0 / 2.0D;
                double d4 = random.nextDouble() * (8.0D - d1 - 4.0D) + 2.0D + d1 / 2.0D;
                double d5 = random.nextDouble() * (16.0D - d2 - 2.0D) + 1.0D + d2 / 2.0D;

                for (int k1 = 1; k1 < 15; ++k1) {
                    for (int l1 = 1; l1 < 15; ++l1) {
                        for (int i2 = 1; i2 < 7; ++i2) {
                            double d6 = ((double)k1 - d3) / (d0 / 2.0D);
                            double d7 = ((double)i2 - d4) / (d1 / 2.0D);
                            double d8 = ((double)l1 - d5) / (d2 / 2.0D);
                            double d9 = d6 * d6 + d7 * d7 + d8 * d8;

                            if (d9 < 1.0D) {
                                aboolean[(k1 * 16 + l1) * 8 + i2] = true;
                            }
                        }
                    }
                }
            }

            int yOffset;
            int zOffset;
            boolean flag;

            for (xOffset = 0; xOffset < 16; ++xOffset) {
                for (zOffset = 0; zOffset < 16; ++zOffset) {
                    for (yOffset = 0; yOffset < 8; ++yOffset) {
                        flag = !aboolean[(xOffset * 16 + zOffset) * 8 + yOffset] && (xOffset < 15 && aboolean[((xOffset + 1) * 16 + zOffset) * 8 + yOffset] || xOffset > 0 && aboolean[((xOffset - 1) * 16 + zOffset) * 8 + yOffset] || zOffset < 15 && aboolean[(xOffset * 16 + zOffset + 1) * 8 + yOffset] || zOffset > 0 && aboolean[(xOffset * 16 + (zOffset - 1)) * 8 + yOffset] || yOffset < 7 && aboolean[(xOffset * 16 + zOffset) * 8 + yOffset + 1] || yOffset > 0 && aboolean[(xOffset * 16 + zOffset) * 8 + (yOffset - 1)]);

                        if (flag) {
                            Material material = world.getBlock(x + xOffset, y + yOffset, z + zOffset).getMaterial();

                            if (yOffset >= 4 && material.isLiquid()) {
                                return false;
                            }

                            if (yOffset < 4 && !material.isSolid() && world.getBlock(x + xOffset, y + yOffset, z + zOffset) != this.liquidToGen && world.getBlockMetadata(x + xOffset, y + yOffset, z + zOffset) != this.liquidToGenMeta) {
                                return false;
                            }
                        }
                    }
                }
            }

            for (xOffset = 0; xOffset < 16; ++xOffset) {
                for (zOffset = 0; zOffset < 16; ++zOffset) {
                    for (yOffset = 0; yOffset < 8; ++yOffset) {
                        if (aboolean[(xOffset * 16 + zOffset) * 8 + yOffset]) {
                        	if(yOffset >= 4) {
                        		world.setBlock(x + xOffset, y + yOffset, z + zOffset, Blocks.air, 0, 2);
                        	} else {
                        		world.setBlock(x + xOffset, y + yOffset, z + zOffset, this.liquidToGen, this.liquidToGenMeta, 2);
                        	}
                        }
                    }
                }
            }

            for (xOffset = 0; xOffset < 16; ++xOffset) {
                for (zOffset = 0; zOffset < 16; ++zOffset) {
                    for (yOffset = 4; yOffset < 8; ++yOffset) {
                        if (aboolean[(xOffset * 16 + zOffset) * 8 + yOffset] && world.getBlock(x + xOffset, y + yOffset - 1, z + zOffset) == Blocks.dirt && world.getSavedLightValue(EnumSkyBlock.Sky, x + xOffset, y + yOffset, z + zOffset) > 0) {
                            BiomeGenBase biomegenbase = world.getBiomeGenForCoords(x + xOffset, z + zOffset);

                            if (biomegenbase.topBlock == Blocks.mycelium) {
                                world.setBlock(x + xOffset, y + yOffset - 1, z + zOffset, Blocks.mycelium, 0, 2);
                            } else {
                                world.setBlock(x + xOffset, y + yOffset - 1, z + zOffset, Blocks.grass, 0, 2);
                            }
                        }
                    }
                }
            }

            if (this.liquidToGen.getMaterial() == Material.lava) {
                for (xOffset = 0; xOffset < 16; ++xOffset) {
                    for (zOffset = 0; zOffset < 16; ++zOffset) {
                        for (yOffset = 0; yOffset < 8; ++yOffset) {
                            flag = !aboolean[(xOffset * 16 + zOffset) * 8 + yOffset] && (xOffset < 15 && aboolean[((xOffset + 1) * 16 + zOffset) * 8 + yOffset] || xOffset > 0 && aboolean[((xOffset - 1) * 16 + zOffset) * 8 + yOffset] || zOffset < 15 && aboolean[(xOffset * 16 + zOffset + 1) * 8 + yOffset] || zOffset > 0 && aboolean[(xOffset * 16 + (zOffset - 1)) * 8 + yOffset] || yOffset < 7 && aboolean[(xOffset * 16 + zOffset) * 8 + yOffset + 1] || yOffset > 0 && aboolean[(xOffset * 16 + zOffset) * 8 + (yOffset - 1)]);

                            if (flag && (yOffset < 4 || random.nextInt(2) != 0) && world.getBlock(x + xOffset, y + yOffset, z + zOffset).getMaterial().isSolid()) {
                                world.setBlock(x + xOffset, y + yOffset, z + zOffset, this.replaceBlock, this.replaceBlockMeta, 2);
                            }
                        }
                    }
                }
            }

            if (this.liquidToGen.getMaterial() == Material.water) {
                for (xOffset = 0; xOffset < 16; ++xOffset) {
                    for (zOffset = 0; zOffset < 16; ++zOffset) {
                        byte yOffset2 = 4;

                        if (world.isBlockFreezable(x + xOffset, y + yOffset2, z + zOffset)) {
                            world.setBlock(x + xOffset, y + yOffset2, z + zOffset, Blocks.ice, 0, 2);
                        }
                    }
                }
            }

            return true;
        }
    }
}