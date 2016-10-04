package projectaurora.world.gen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

public class WorldGenBigTree extends WorldGenAbstractTree {
	private static final byte[] otherCoordPairs = { 2, 0, 0, 1, 2, 1 };
	private Random rand = new Random();
	private World worldObj;
	private int[] basePos = { 0, 0, 0 };
	private int heightLimit;
	private int height;
	private double heightAttenuation = 0.618D;
	private double branchDensity = 1.0D;
	private double branchSlope = 0.381D;
	private double scaleWidth = 1.0D;
	private double leafDensity = 1.0D;
	private int heightLimitLimit = 12;
	private int leafDistanceLimit = 4;
	private int[][] leafNodes;
	private Block wood;
	private int woodMeta;
	private Block leaf;
	private int leafMeta;
	private boolean isVanillaLog;

	public WorldGenBigTree(boolean flag, Block block, int meta, Block block2, int meta2) {
		super(flag);
		this.wood = block;
		this.woodMeta = meta;
		this.leaf = block2;
		this.leafMeta = meta2;
	}

	@Override
	public boolean generate(World world, Random rand, int x, int y, int z) {
		if(wood instanceof BlockLog) {
			this.isVanillaLog = true;
		}
		
		this.worldObj = world;
		long l = rand.nextLong();
		this.rand.setSeed(l);
		this.basePos[0] = x;
		this.basePos[1] = y;
		this.basePos[2] = z;
		
		if (this.heightLimit == 0) {
			this.heightLimit = (5 + this.rand.nextInt(this.heightLimitLimit));
		}

		if (!validTreeLocation()) {
			return false;
		}

		generateLeafNodeList();
		generateLeaves();
		generateTrunk();
		generateLeafNodeBases();
		return true;
	}

	private void generateLeafNodeList() {
		this.height = ((int)(this.heightLimit * this.heightAttenuation));
		
		if (this.height >= this.heightLimit) {
			this.height = (this.heightLimit - 1);
		}

		int i = (int)(1.382D + Math.pow(this.leafDensity * this.heightLimit / 13.0D, 2.0D));

		if (i < 1) {
			i = 1;
		}

		int[][] aint = new int[i * this.heightLimit][4];
		int j = this.basePos[1] + this.heightLimit - this.leafDistanceLimit;
		int k = 1;
		int l = this.basePos[1] + this.height;
		int i1 = j - this.basePos[1];
		aint[0][0] = this.basePos[0];
		aint[0][1] = j;
		aint[0][2] = this.basePos[2];
		aint[0][3] = l;
		j--;

		while (i1 >= 0) {
			int j1 = 0;
			float f = layerSize(i1);

			if (f < 0.0F) {
				j--;
		        i1--;
			} else {
				for (double d0 = 0.5D; j1 < i; j1++) {
					double d1 = this.scaleWidth * f * (this.rand.nextFloat() + 0.328D);
					double d2 = this.rand.nextFloat() * 2.0D * 3.141592653589793D;
					int k1 = MathHelper.floor_double(d1 * Math.sin(d2) + this.basePos[0] + d0);
					int l1 = MathHelper.floor_double(d1 * Math.cos(d2) + this.basePos[2] + d0);
					int[] aint1 = { k1, j, l1 };
					int[] aint2 = { k1, j + this.leafDistanceLimit, l1 };

					if (checkBlockLine(aint1, aint2) == -1) {
						int[] aint3 = { this.basePos[0], this.basePos[1], this.basePos[2] };
						double d3 = Math.sqrt(Math.pow(Math.abs(this.basePos[0] - aint1[0]), 2.0D) + Math.pow(Math.abs(this.basePos[2] - aint1[2]), 2.0D));
						double d4 = d3 * this.branchSlope;

						if (aint1[1] - d4 > l) {
							aint3[1] = l;
						} else {
							aint3[1] = ((int)(aint1[1] - d4));
						}

						if (checkBlockLine(aint3, aint1) == -1) {
							aint[k][0] = k1;
							aint[k][1] = j;
							aint[k][2] = l1;
							aint[k][3] = aint3[1];
							k++;
						}
					}
		        }
		        j--;
		        i1--;
			}
		}
		    this.leafNodes = new int[k][4];
		    System.arraycopy(aint, 0, this.leafNodes, 0, k);
	}
	
	private void genTreeLayer(int par1, int par2, int par3, float par4, byte par5, Block par6, int meta) {
		int i1 = (int)(par4 + 0.618D);
	    byte b1 = otherCoordPairs[par5];
	    byte b2 = otherCoordPairs[(par5 + 3)];
	    int[] aint = { par1, par2, par3 };
	    int[] aint1 = { 0, 0, 0 };
	    int j1 = -i1;
	    int k1 = -i1;

	    for (aint1[par5] = aint[par5]; j1 <= i1; j1++) {
	    	aint[b1] += j1;
	    	k1 = -i1;

	    	while (k1 <= i1) {
	    		double d0 = Math.pow(Math.abs(j1) + 0.5D, 2.0D) + Math.pow(Math.abs(k1) + 0.5D, 2.0D);

	    		if (d0 > par4 * par4) {
	    			k1++;
	    		} else {
	    			aint[b2] += k1;
	    			Block block = this.worldObj.getBlock(aint1[0], aint1[1], aint1[2]);
	          
	    			if ((block.getMaterial() != Material.air) && (!block.isLeaves(this.worldObj, aint1[0], aint1[1], aint1[2]))) {
	    				k1++;
	    			} else {
	    				setBlockAndNotifyAdequately(this.worldObj, aint1[0], aint1[1], aint1[2], par6, meta);
	    				k1++;
	    			}
	    		}
	    	}
	    }
	}

	private float layerSize(int par1) {
		if (par1 < this.heightLimit * 0.3D) {
			return -1.618F;
	    }

	    float f = this.heightLimit / 2.0F;
	    float f1 = this.heightLimit / 2.0F - par1;
	    float f2;
	  
	    if (f1 == 0.0F) {
	      f2 = f;
	    } else {
	    	if (Math.abs(f1) >= f) {
	    		f2 = 0.0F;
	    	} else {
	    		f2 = (float)Math.sqrt(Math.pow(Math.abs(f), 2.0D) - Math.pow(Math.abs(f1), 2.0D));
	    	}
	    }
	    f2 *= 0.5F;
	    return f2;
	}

	private float leafSize(int par1) {
	    return (par1 >= 0) && (par1 < this.leafDistanceLimit) ? 2.0F : (par1 != 0) && (par1 != this.leafDistanceLimit - 1) ? 3.0F : -1.0F;
	}

	private void generateLeafNode(int i, int j, int k) {
	    int j1 = j;
	   
	    for (int j2 = j + this.leafDistanceLimit; j1 < j2; j1++) {
	    	float f = leafSize(j1 - j);
	    	genTreeLayer(i, j1, k, f, (byte)1, this.leaf, this.leafMeta);
	    }
	}

	private void placeBlockLine(int[] par1ArrayOfInteger, int[] par2ArrayOfInteger, Block block, int meta) {
	    int[] aint2 = { 0, 0, 0 };
	    byte b0 = 0;
	    byte b2 = 0;
	    
	    while(b0 < 3) {
	    	aint2[b0] = par2ArrayOfInteger[b0] - par1ArrayOfInteger[b0];
	    	if (Math.abs(aint2[b0]) > Math.abs(aint2[b2])) {
	    		b2 = b0;
	    	}
	    	++b0;
	    }

	    if (aint2[b2] != 0) {
	    	byte b3 = otherCoordPairs[b2];
	    	byte b4 = otherCoordPairs[(b2 + 3)];
	    	byte b5;
	      
	    	if (aint2[b2] > 0) {
	    		b5 = 1;
	    	} else {
	    		b5 = -1;
	    	}

	    	double d0 = aint2[b3] / aint2[b2];
	    	double d1 = aint2[b4] / aint2[b2];
	    	int[] aint3 = { 0, 0, 0 };
	      	int j = 0;
	      	
	      	for (int k = aint2[b2] + b5; j != k; j += b5) {
	      		aint3[b2] = MathHelper.floor_double(par1ArrayOfInteger[b2] + j + 0.5D);
	      		aint3[b3] = MathHelper.floor_double(par1ArrayOfInteger[b3] + j * d0 + 0.5D);
	      		aint3[b4] = MathHelper.floor_double(par1ArrayOfInteger[b4] + j * d1 + 0.5D);
	      		byte b6 = 0;
	      		int l = Math.abs(aint3[0] - par1ArrayOfInteger[0]);
	      		int i1 = Math.abs(aint3[2] - par1ArrayOfInteger[2]);
	      		int j1 = Math.max(l, i1);
	        
	      		if (j1 > 0) {
	      			if (l == j1) {
	      				b6 = 4;
	      			} else if (i1 == j1) {
	      				b6 = 8;
	      			}
	      		}

	        setBlockAndNotifyAdequately(this.worldObj, aint3[0], aint3[1], aint3[2], block, meta | b6);
	      	}
	    }
	}
	
	private void generateLeaves() {
	    int i = 0;
	    for (int j = this.leafNodes.length; i < j; i++) {
	    	int k = this.leafNodes[i][0];
	    	int l = this.leafNodes[i][1];
	    	int i1 = this.leafNodes[i][2];
	    	generateLeafNode(k, l, i1);
	    }
	}

	private boolean leafNodeNeedsBase(int par1) {
	    return par1 >= this.heightLimit * 0.2D;
	}

	private void generateTrunk() {
	    int i = this.basePos[0];
	    int j = this.basePos[1];
	    int j1 = this.basePos[1] + this.height;
	    int k = this.basePos[2];
	    int[] aint = { i, j, k };
	    int[] aint1 = { i, j1, k };
	    placeBlockLine(aint, aint1, this.wood, this.woodMeta);
	    this.worldObj.getBlock(i, j - 1, k).onPlantGrow(this.worldObj, i, j - 1, k, i, j, k);
	}

	private void generateLeafNodeBases() {
	    int i = 0;
	    int j = this.leafNodes.length;

	    for (int[] aint = { this.basePos[0], this.basePos[1], this.basePos[2] }; i < j; i++) {
	    	int[] aint1 = this.leafNodes[i];
	    	int[] aint2 = { aint1[0], aint1[1], aint1[2] };
	    	aint[1] = aint1[3];
	    	int k = aint[1] - this.basePos[1];

	    	if (leafNodeNeedsBase(k)) {
	    		placeBlockLine(aint, aint2, this.wood, this.woodMeta);
	    	}
	    }
	}
	
	private int checkBlockLine(int[] par1ArrayOfInteger, int[] par2ArrayOfInteger) {
	    int[] aint2 = { 0, 0, 0 };
	    byte b0 = 0;
	    byte b1 = 0;
	    
	    while(b0 < 3) {
	    	aint2[b0] = par2ArrayOfInteger[b0] - par1ArrayOfInteger[b0];
	    	if (Math.abs(aint2[b0]) > Math.abs(aint2[b1])) {
	    		b1 = b0;
	    	}
	    	++b0;
	    }

	    if (aint2[b1] == 0) {
	    	return -1;
	    }

	    byte b2 = otherCoordPairs[b1];
	    byte b3 = otherCoordPairs[(b1 + 3)];
	    byte b4;
	    if (aint2[b1] > 0) {
	    	b4 = 1;
	    } else {
	    	b4 = -1;
	    }

	    double d0 = aint2[b2] / aint2[b1];
	    double d1 = aint2[b3] / aint2[b1];
	    int[] aint3 = { 0, 0, 0 };
	    int i;
	    int j;

	    for (i = 0, j = aint2[b1] + b4; i != j; i += b4) {
	    	par1ArrayOfInteger[b1] += i;
	    	aint3[b2] = MathHelper.floor_double(par1ArrayOfInteger[b2] + i * d0);
	    	aint3[b3] = MathHelper.floor_double(par1ArrayOfInteger[b3] + i * d1);
	    	Block block = this.worldObj.getBlock(aint3[0], aint3[1], aint3[2]);
	      
	    	if ((block.getMaterial() != Material.air) && (!block.isLeaves(this.worldObj, aint3[0], aint3[1], aint3[2]))) {
	    		break;
	    	}
	    }

	    if(i == j) {
	    	return -1;
	    } else {
	    	return Math.abs(i);
	    }
	}

	private boolean validTreeLocation() {
	    int[] aint = { this.basePos[0], this.basePos[1], this.basePos[2] };
	    int[] aint1 = { this.basePos[0], this.basePos[1] + this.heightLimit - 1, this.basePos[2] };
	    Block block = this.worldObj.getBlock(this.basePos[0], this.basePos[1] - 1, this.basePos[2]);
	    
	    if(isVanillaLog) {
		    if (!block.canSustainPlant(this.worldObj, this.basePos[0], this.basePos[1] - 1, this.basePos[2], ForgeDirection.UP, (IPlantable)Blocks.sapling)) {
		    	return false;
		    }
	    } else {
	    	//PUT CUSTOM SAPLINGS HERE
	    }

	    int j = checkBlockLine(aint, aint1);

	    if (j == -1) {
	    	return true;
	    }
	    if (j < 6) {
	    	return false;
	    }

	    this.heightLimit = j;
	    return true;
	}
}