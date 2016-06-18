package projectaurora.world.gen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenBoulder extends WorldGenerator {
	private Block rock;
	private int meta;
	private int minWidth;
	private int maxWidth;
	private int heightCheck = 3;
	
	public WorldGenBoulder(Block block, int metadata, int min, int max) {
		super(false);
		this.rock = block;
		this.meta = metadata;
		this.minWidth = min;
		this.maxWidth = max;
	}
	
	public WorldGenBoulder setHeightCheck(int flag) {
		this.heightCheck = flag;
		return this;
	}

	@Override
	public boolean generate(World world, Random rand, int x, int y, int z) {
		BiomeGenBase biome = world.getBiomeGenForCoords(x, z);
		if(world.getBlock(x, y - 1, z) != biome.topBlock) {
			return false;
		}
		
		int boulderWidth = MathHelper.getRandomIntegerInRange(rand, minWidth, maxWidth);
		int highestHeight = y;
		int lowestHeight = y;
		
		for(int i1 = x - boulderWidth; i1 <= x + boulderWidth; i1++) {
			for(int k1 = z - boulderWidth; k1 <= z + boulderWidth; k1++) {
				int heightValue = world.getHeightValue(i1, k1);
				
				if(world.getBlock(i1, heightValue - 1, k1) != biome.topBlock) {
					return false;
				}
				if(heightValue > highestHeight) {
					highestHeight = heightValue;
				}
				if(heightValue < lowestHeight) {
					lowestHeight = heightValue;
				}
			}
		}
		
		if(highestHeight - lowestHeight > this.heightCheck) {
			return false;
		}
		
		int spheres = 1 + rand.nextInt(boulderWidth + 1);
		
		for(int l = 0; l < spheres; l++) {
			int posX = x + MathHelper.getRandomIntegerInRange(rand, -boulderWidth, boulderWidth);
			int posZ = z + MathHelper.getRandomIntegerInRange(rand, -boulderWidth, boulderWidth);
			int posY = world.getTopSolidOrLiquidBlock(posX, posZ);
			int sphereWidth = MathHelper.getRandomIntegerInRange(rand, this.minWidth, this.maxWidth);
			
			for(int i1 = posX - sphereWidth; i1 <= posX + sphereWidth; i1++) {
				for(int j1 = posY - sphereWidth; j1 <= posY + sphereWidth; j1++) {
					for(int k1 = posZ - sphereWidth; k1 <= posZ + sphereWidth; k1++) {
						int i2 = i1 - posX;
						int j2 = j1 - posY;
						int k2 = k1 - posZ;
						
						if(i2 * i2 + j2 * j2 + k2 * k2 < sphereWidth * sphereWidth) {
							while((j2 >= 0) && (!world.getBlock(i1, j2 - 1, k1).isOpaqueCube())) {
								j2--;
							}
							
							setBlockAndNotifyAdequately(world, i1, j2, k1, this.rock, this.meta);
							world.getBlock(i1, j2 - 1, k1).onPlantGrow(world, i1, j2 - 1, k1, i1, j2 - 1, k1);
						}
					}	
				}
			}
		}
		return true;
	}
}