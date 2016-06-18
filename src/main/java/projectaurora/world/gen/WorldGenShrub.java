package projectaurora.world.gen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

public class WorldGenShrub extends WorldGenAbstractTree {
	private Block wood;
	private int woodMeta;
	private Block leaf;
	private int leafMeta;

	public WorldGenShrub(Block block, int meta, Block block1, int meta1) {
		super(false);
		this.wood = block;
		this.woodMeta = meta;
		this.leaf = block1;
		this.leafMeta = meta1;
	}

	@Override
	public boolean generate(World world, Random rand, int x, int y, int z) {
		do {
			Block block = world.getBlock(x, y, z);
			if ((!block.isLeaves(world, x, y, z)) && (!block.isAir(world, x, y, z))) {
				break;
			}
			y--;
		}
	    while (y > 0);

	    Block below = world.getBlock(x, y, z);
	    if (below.canSustainPlant(world, x, y, z, ForgeDirection.UP, (IPlantable)Blocks.sapling)) {//TODO custom saplings
	    	y++;
	    	setBlockAndNotifyAdequately(world, x, y, z, this.wood, this.woodMeta);

	    	for (int j1 = y; j1 <= y + 2; j1++) {
	    		int j2 = j1 - y;
	    		int range = 2 - j2;

	    		for (int i1 = x - range; i1 <= x + range; i1++) {
	    			for (int k1 = z - range; k1 <= z + range; k1++) {
	    				int i2 = i1 - x;
	    				int k2 = k1 - z;

	    				if (((Math.abs(i2) != range) || (Math.abs(k2) != range) || (rand.nextInt(2) != 0)) && (world.getBlock(i1, j1, k1).canBeReplacedByLeaves(world, i1, j1, k1))) {
	    					setBlockAndNotifyAdequately(world, i1, j1, k1, this.leaf, this.leafMeta);
	    				}
	    			}
	    		}
	    	}
	    }
	    return true;
	}
}