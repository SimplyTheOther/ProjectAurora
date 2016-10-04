package projectaurora.world.gen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

public class WorldGenDesertTree extends WorldGenAbstractTree {
	private Block wood;	
	private int woodMeta;
	private Block leaf;
	private int leafMeta;
	private boolean isNatural;
	
	public WorldGenDesertTree(boolean flag, Block block, int meta, Block block1, int meta1) {
		super(flag);
		this.isNatural = flag;
		this.wood = block;
		this.woodMeta = meta;
		this.leaf = block1;
		this.leafMeta = meta1;
	}

	@Override
	public boolean generate(World world, Random rand, int x, int y, int z) {
	    int height = 3 + rand.nextInt(3);
	    boolean flag = true;

	    if (!this.isNatural) {
	    	if ((y >= 1) && (height + 1 <= 256)) {
	    		for (int j1 = y; j1 <= y + height + 1; j1++) {
	    			int range = 1;

	    			if (j1 == y) {
	    				range = 0;
	    			}

	    			if (j1 >= y + height - 1) {
	    				range = 2;
	    			}

	    			for (int i1 = x - range; (i1 <= x + range) && (flag); i1++) {
	    				for (int k1 = z - range; (k1 <= z + range) && (flag); k1++) {
	    					if ((j1 >= 0) && (j1 < 256)) {
	    						if (!isReplaceable(world, i1, j1, k1)) {
	    							flag = false;
	    						}
	    					} else {
	    						flag = false;
	    					}
	    				}
	    			}
	    		}
	    	} else {
	    		flag = false;
	    	}
	    }

	    Block below = world.getBlock(x, y - 1, z);
	    boolean isSoil = (below.canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, (IPlantable)Blocks.sapling)) || ((this.isNatural) && ((below == Blocks.sand) || (below == Blocks.stone))); 
	    
	    if (!isSoil) {
	    	flag = false;
	    }

	    if (!flag) {
	    	return false;
	    }

	    below.onPlantGrow(world, x, y - 1, z, x, y, z);

	    for (int branch = 0; branch < 4; branch++) {
	    	int branchLength = 1 + rand.nextInt(3);
	    	int i1 = x;
	    	int j1 = y + height - 1 - rand.nextInt(2);
	    	int k1 = z;

	    	for (int l = 0; l < branchLength; l++) {
	    		if (rand.nextInt(3) != 0) {
	    			j1++;
	    		}

	    		if (rand.nextInt(3) != 0) {
	    			switch (branch) {
	    				case 0:
	    					i1--;
	    					break;
	    				case 1:
	    					k1++;
	    					break;
	    				case 2:
	    					i1++;
	    					break;
	    				case 3:
	    					k1--;
	    			}
	    		}

	    		if (!isReplaceable(world, i1, j1, k1))
	    			break;
	    		setBlockAndNotifyAdequately(world, i1, j1, k1, this.wood, this.woodMeta);
	    	}

	    	byte leafStart = 1;
	    	byte leafRangeMin = 0;
	    	for (int j2 = j1 - leafStart; j2 <= j1 + 1; j2++) {
	    		int j3 = j2 - j1;
	    		int leafRange = leafRangeMin + 1 - j3 / 2;
	    		for (int i2 = i1 - leafRange; i2 <= i1 + leafRange; i2++) {
	    			int i3 = i2 - i1;
	    			for (int k2 = k1 - leafRange; k2 <= k1 + leafRange; k2++) {
	    				int k3 = k2 - k1;
	    				if ((Math.abs(i3) != leafRange) || (Math.abs(k3) != leafRange) || ((rand.nextInt(2) != 0) && (j3 != 0))) {
	    					Block block = world.getBlock(i2, j2, k2);
	    					if ((block.isReplaceable(world, i2, j2, k2)) || (block.isLeaves(world, i2, j2, k2))) {
	    						setBlockAndNotifyAdequately(world, i2, j2, k2, this.leaf, this.leafMeta);
	    					}
	    				}
	    			}
	    		}
	    	}
	    }

	    for (int j1 = y; j1 < y + height; j1++) {
	    	setBlockAndNotifyAdequately(world, x, j1, z, this.wood, this.woodMeta);
	    }
	    return true;
	}
}