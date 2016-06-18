package projectaurora.world.gen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

public class WorldGenSimpleTree extends WorldGenAbstractTree {
	private int minHeight;
	private int maxHeight;
	private Block wood;
	private int woodMeta;
	private Block leaf;
	private int leafMeta;
	private int extraTrunkWidth;

	public WorldGenSimpleTree(boolean flag, int min, int max, Block woodBlock, int woodBMeta, Block leafBlock, int leafBMeta) {
		super(flag);
		this.minHeight = min;
		this.maxHeight = max;
		this.wood = woodBlock;
		this.woodMeta = woodBMeta;
		this.leaf = leafBlock;
		this.leafMeta = leafBMeta;
	}
	
	public WorldGenSimpleTree setTrunkWidth(int width) {
		this.extraTrunkWidth = width - 1;
		return this;
	}

	@Override
	public boolean generate(World world, Random rand, int x, int y, int z) {
		int height = MathHelper.getRandomIntegerInRange(rand, minHeight, maxHeight);
		boolean flag = true;
		
		if((y >= 1) && (y + height + 1 <= 256)) {
			for(int j1 = y; j1 <= y + 1 + height; j1++) {
				int range = 1;
				if(j1 == y) {
					range = 0;
				}
				if(j1 >= y + height - 1) {
					range = 2;
				}
				
				for(int i1 = x - range; (i1 <= x + range + this.extraTrunkWidth) && (flag); i1++) {
					for(int k1 = z - range; (k1 <= z + range + this.extraTrunkWidth) && (flag); k1++) {
						if((j1 >= 0) && (j1 < 256)) {
							if(!isReplaceable(world, i1, j1, k1)) {
								flag = false;
							}
						} else {
							flag = false;
						}
					}
				}
			}
			
			if(!flag) {
				return false;
			}
			
			boolean flag1 = true;
			for(int i1 = x; (i1 <= x + this.extraTrunkWidth) && (flag1); i1++) {
				for(int k1 = z; (k1 <= z + this.extraTrunkWidth) && (flag1); k1++) {
					Block block = world.getBlock(i1, y - 1, k1);
					if(!block.canSustainPlant(world, i1, y - 1, k1, ForgeDirection.UP, (IPlantable)Blocks.sapling)) {//TODO custom saplings
						flag1 = false;
					}
				}
			}
			
			if(flag1) {
				for(int i1 = x; i1 <= x + this.extraTrunkWidth; i1++) {
					for(int k1 = z; k1 <= z + this.extraTrunkWidth; k1++) {
						world.getBlock(i1, y - 1, k1).onPlantGrow(world, i1, y - 1, k1, i1, y, k1);
					}
				}
				
				byte leafStart = 3;
				byte leafRangeMin = 0;
				for(int j1 = y - leafStart + height; j1 <= y + height; j1++) {
					int j2 = j1 - (y + height);
					int leafRange = leafRangeMin + 1 - j2 / 2;
					
					for(int i1 = x - leafRange; i1 <= x + leafRange + this.extraTrunkWidth; i1++) {
						int i2 = i1 - x;
						if(i2 > 0) {
							i2 -= this.extraTrunkWidth;
						}
						for(int k1 = z - leafRange; k1 <= z + leafRange + this.extraTrunkWidth; k1++) {
							int k2 = k1 - z;
							if(k2 > 0) {
								k2 -= this.extraTrunkWidth;
							}
							Block block = world.getBlock(i1, j1, k1);
							
							if(((Math.abs(i2) != leafRange) || (Math.abs(k2) != leafRange) || ((rand.nextInt(2) != 0) && (j2 != 0))) && (block.canBeReplacedByLeaves(world, i1, j1, k1))) {
								setBlockAndNotifyAdequately(world, i1, j1, k1, this.leaf, this.leafMeta);
							}
						}
					}
				}
				
				for(int j1 = 0; j1 < height; j1++) {
					for(int i1 = x; i1 <= x + this.extraTrunkWidth; i1++) {
						for(int k1 = z; k1 <= z + this.extraTrunkWidth; k1++) {
							Block block = world.getBlock(i1, y + j1, k1);
							
							if(block.getMaterial() == Material.air || block.isLeaves(world, i1, y + j1, k1)) {
								setBlockAndNotifyAdequately(world, i1, y + j1, k1, this.wood, this.woodMeta);
							}
						}
					}
				}
				return true;
			}
			return false;
		}
		return false;
	}
}