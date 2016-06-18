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

public class WorldGenHugeTree extends WorldGenAbstractTree {
	private Block wood;
	private int woodMeta;
	private Block leaf;
	private int leafMeta;
	private boolean restrictions = true;

	public WorldGenHugeTree(Block block, int meta, Block block2, int meta2) {
		super(false);
		this.wood = block;
		this.woodMeta = meta;
		this.leaf = block2;
		this.leafMeta = meta2;
	}
	
	public WorldGenHugeTree disableRestrictions() {
		this.restrictions = false;
		return this;
	}

	@Override
	public boolean generate(World world, Random rand, int x, int y, int z) {
		int trunkWidth = 1;
	    int height = rand.nextInt(12) + 12;
	    boolean flag = true;

	    if (this.restrictions) {
	    	if ((y >= 1) && (y + height + 1 <= 256)) {
	    		for (int j1 = y; j1 <= y + 1 + height; j1++) {
	    			int range = trunkWidth + 1;

	    			if (j1 == y) {
	    				range = trunkWidth;
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
	    		
	    		for (int i1 = x - trunkWidth; (i1 <= x + trunkWidth) && (flag); i1++) {
	    			for (int k1 = z - trunkWidth; (k1 <= z + trunkWidth) && (flag); k1++) {
	    				Block block = world.getBlock(i1, y - 1, k1);
	    				if (!block.canSustainPlant(world, i1, y - 1, k1, ForgeDirection.UP, (IPlantable)Blocks.sapling)) {//TODO custom sapling 
	    					flag = false;
	    				}
	    			}
	    		}

	    		if (!flag) {
	    			return false;
	    		}
	    	} else {
	    		return false;
	    	}
	    }

	    for (int i1 = x - trunkWidth; i1 <= x + trunkWidth; i1++) {
	    	for (int k1 = z - trunkWidth; k1 <= z + trunkWidth; k1++) {
	    		world.getBlock(i1, y - 1, k1).onPlantGrow(world, i1, y - 1, k1, i1, y, k1);
	    	}
	    }

	    for (int j1 = 0; j1 < height; j1++) {
	    	for (int i1 = x - trunkWidth; i1 <= x + trunkWidth; i1++) {
	    		for (int k1 = z - trunkWidth; k1 <= z + trunkWidth; k1++) {
	    			setBlockAndNotifyAdequately(world, i1, y + j1, k1, this.wood, this.woodMeta);
	    		}
	    	}
	    }

	    int angle = 0;
	    while (angle < 360) {
	    	angle += 20 + rand.nextInt(25);
	    	float angleR = angle / 180.0F * 3.141593F;
	    	float sin = MathHelper.sin(angleR);
	    	float cos = MathHelper.cos(angleR);
	    	int boughLength = 6 + rand.nextInt(6);
	    	int boughThickness = Math.round(boughLength / 20.0F * 1.5F);
	    	int boughBaseHeight = y + MathHelper.floor_double(height * (0.75F + rand.nextFloat() * 0.25F));
	    	int boughHeight = 3 + rand.nextInt(4);

	    	for (int l = 0; l < boughLength; l++) {
	    		int i1 = x + Math.round(sin * l);
	    		int k1 = z + Math.round(cos * l);
	    		int j1 = boughBaseHeight + Math.round(l / boughLength * boughHeight);
	    		int range = boughThickness - Math.round(l / boughLength * boughThickness * 0.5F);

	    		for (int i2 = i1 - range; i2 <= i1 + range; i2++) {
	    			for (int j2 = j1 - range; j2 <= j1 + range; j2++) {
	    				for (int k2 = k1 - range; k2 <= k1 + range; k2++) {
	    					Block block = world.getBlock(i2, j2, k2);
	    					if ((block.getMaterial() == Material.air) || (block.isLeaves(world, i2, j2, k2))) {
	    						setBlockAndNotifyAdequately(world, i2, j2, k2, this.wood, this.woodMeta | 0xC);
	    					}
	    				}
	    			}
	    		}

	    		int branch_angle = angle + rand.nextInt(360);
	    		float branch_angleR = branch_angle / 180.0F * 3.141593F;
	    		float branch_sin = MathHelper.sin(branch_angleR);
	    		float branch_cos = MathHelper.cos(branch_angleR);
	    		int branchLength = 4 + rand.nextInt(4);
	    		int branchHeight = rand.nextInt(5);
	    		int leafRange = 3;

	    		for (int l1 = 0; l1 < branchLength; l1++) {
	    			int i2 = i1 + Math.round(branch_sin * l1);
	    			int k2 = k1 + Math.round(branch_cos * l1);
	    			int j2 = j1 + Math.round(l1 / branchLength * branchHeight);

	    			for (int j3 = j2; j3 >= j2 - 1; j3--) {
	    				Block block = world.getBlock(i2, j3, k2);
	    				if ((block.getMaterial() == Material.air) || (block.isLeaves(world, i2, j3, k2))) {
	    					setBlockAndNotifyAdequately(world, i2, j3, k2, this.wood, this.woodMeta | 0xC);
	    				}
	    			}

	    			if (l1 == branchLength - 1) {
	    				for (int i3 = i2 - leafRange; i3 <= i2 + leafRange; i3++) {
	    					for (int j3 = j2 - leafRange; j3 <= j2 + leafRange; j3++) {
	    						for (int k3 = k2 - leafRange; k3 <= k2 + leafRange; k3++) {
	    							int i4 = i3 - i2;
	    							int j4 = j3 - j2;
	    							int k4 = k3 - k2;
	    							int dist = i4 * i4 + j4 * j4 + k4 * k4;
	    							if ((dist < (leafRange - 1) * (leafRange - 1)) || ((dist < leafRange * leafRange) && (rand.nextInt(3) != 0))) {
	    								Block block2 = world.getBlock(i3, j3, k3);
	    								if ((block2.getMaterial() == Material.air) || (block2.isLeaves(world, i3, j3, k3))) {
	    									setBlockAndNotifyAdequately(world, i3, j3, k3, this.leaf, this.leafMeta);
	    								}
	    							}
	    						}
	    					}
	    				}
	    			}
	    		}
	    	}
	    }

	    int roots = 5 + rand.nextInt(5);
	    for (int l = 0; l < roots; l++) {
	    	int i1 = x;
	    	int j1 = y + 1 + rand.nextInt(5);
	    	int k1 = z;
	    	int xDirection = 0;
	    	int zDirection = 0;
	    	int rootLength = 2 + rand.nextInt(4);

	    	if (rand.nextBoolean()) {
	    		if (rand.nextBoolean()) {
	    			i1 -= trunkWidth + 1;
	    			xDirection = -1;
	    		} else {
	    			i1 += trunkWidth + 1;
	    			xDirection = 1;
	    		}
	    		k1 -= trunkWidth + 1;
	    		k1 += rand.nextInt(trunkWidth * 2 + 2);
	    	} else {
	    		if (rand.nextBoolean()) {
	    			k1 -= trunkWidth + 1;
	    			zDirection = -1;
	    		} else {
	    			k1 += trunkWidth + 1;
	    			zDirection = 1;
	    		}
	    		i1 -= trunkWidth + 1;
	    		i1 += rand.nextInt(trunkWidth * 2 + 2);
	    	}

	    	for (int l1 = 0; l1 < rootLength; l1++) {
	    		int rootBlocks = 0;
	    		for (int j2 = j1; !world.getBlock(i1, j2, k1).isOpaqueCube(); j2--) {
	    			setBlockAndNotifyAdequately(world, i1, j2, k1, this.wood, this.woodMeta | 0xC);
	    			world.getBlock(i1, j2 - 1, k1).onPlantGrow(world, i1, j2 - 1, k1, i1, j2, k1);

	    			rootBlocks++;
	    			if (rootBlocks > 5) {
	    				break;
	    			}
	    		}

	    		j1--;
	    		if (rand.nextBoolean()) {
	    			if (xDirection == -1) {
	    				i1--;
	    			} else if (xDirection == 1) {
	    				i1++;
	    			} else if (zDirection == -1) {
	    				k1--;
	    			} else if (zDirection == 1) {
	    				k1++;
	    			}
	    		}
	    	}
	    }
	    return true;
	}
}