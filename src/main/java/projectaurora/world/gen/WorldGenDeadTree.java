package projectaurora.world.gen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

public class WorldGenDeadTree extends WorldGenAbstractTree {
	private Block wood;
	private int woodMeta;
	private boolean isVanillaLog;

	public WorldGenDeadTree(Block block, int meta) {
		super(false);
		this.wood = block;
		this.woodMeta = meta;
	}

	@Override
	public boolean generate(World world, Random rand, int x, int y, int z) {
		if(wood instanceof BlockLog) {
			this.isVanillaLog = true;
		}
		
		Block below = world.getBlock(x, y - 1, z);
		
		if(isVanillaLog) {
		    if ((!below.canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, (IPlantable)Blocks.sapling)) && (below != Blocks.stone) && (below != Blocks.sand) && (below != Blocks.gravel)) {
		    	return false;
		    }
		} else {
			//PUT CUSTOM SAPLING CODE HERE
		}

	    below.onPlantGrow(world, x, y - 1, z, x, y, z);

	    int height = 3 + rand.nextInt(4);
	   
	    for (int j1 = y; j1 < y + height; j1++) {
	    	setBlockAndNotifyAdequately(world, x, j1, z, this.wood, this.woodMeta);
	    }

	    for (int branch = 0; branch < 4; branch++) {
	    	int branchLength = 3 + rand.nextInt(5);
	    	int branchHorizontalPos = 0;
	    	int branchVerticalPos = y + height - 1 - rand.nextInt(2);
	      
	    	for (int l = 0; l < branchLength; l++) {
	    		
	    		if (rand.nextInt(4) == 0) {
	    			branchHorizontalPos++;
	    		}
	    		
	    		if (rand.nextInt(3) != 0) {
	    			branchVerticalPos++;
	    		}

	    		switch (branch) {
	    			case 0:
	    				setBlockAndNotifyAdequately(world, x - branchHorizontalPos, branchVerticalPos, z, this.wood, this.woodMeta | 0xC);
	    				break;
	    			case 1:
	    				setBlockAndNotifyAdequately(world, x, branchVerticalPos, z + branchHorizontalPos, this.wood, this.woodMeta | 0xC);
	    				break;
	    			case 2:
	    				setBlockAndNotifyAdequately(world, x + branchHorizontalPos, branchVerticalPos, z, this.wood, this.woodMeta | 0xC);
	    				break;
	    			case 3:
	    				setBlockAndNotifyAdequately(world, x, branchVerticalPos, z - branchHorizontalPos, this.wood, this.woodMeta | 0xC);
	    		}
	    	}
	    }
	    return true;
	}
}