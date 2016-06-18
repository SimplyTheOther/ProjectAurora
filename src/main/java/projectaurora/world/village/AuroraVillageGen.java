package projectaurora.world.village;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import projectaurora.world.structure.WorldGenStructureBase;

public abstract class AuroraVillageGen {//TODO do villages
	private Random villageRand = new Random();
	private List<StructureInfo> structures = new ArrayList();
	private List<BiomeGenBase> spawnBiomes;
	protected int separation;
	protected int minDistance;
	protected int spawnChance;
	protected int chunkRange;
	
	public AuroraVillageGen(BiomeGenBase biome) {
		registerStructures();
		this.spawnBiomes = new ArrayList();
		this.spawnBiomes.add(biome);
	}

	protected abstract void registerStructures();
	
	protected void addStructure(WorldGenStructureBase structure, int x, int z, int rotation) {
		//structure.restrictions = false;
		this.structures.add(new StructureInfo(structure, x, z, rotation));
	}
	
	private boolean isVillageCentre(World world, int chunkX, int chunkZ) {
	    int i2 = MathHelper.floor_double(chunkX / this.separation);
	    int k2 = MathHelper.floor_double(chunkZ / this.separation);

	    long seed = chunkX * 6890360793007L + chunkZ * 456879569029062L + world.getWorldInfo().getSeed() + 274893855L;
	    this.villageRand.setSeed(seed);
	    i2 *= this.separation;
	    k2 *= this.separation;
	    i2 += this.villageRand.nextInt(this.separation - this.minDistance + 1);
	    k2 += this.villageRand.nextInt(this.separation - this.minDistance + 1);

	    if ((chunkX == i2) && (chunkZ == k2)) {
	    	int i1 = chunkX * 16 + 8;
	    	int k1 = chunkZ * 16 + 8;
	     
	    	if (world.getWorldChunkManager().areBiomesViable(i1, k1, this.chunkRange, this.spawnBiomes)) {
	    		return this.villageRand.nextInt(this.spawnChance) == 0;
	    	}
	    }
	    
	    return false;
	}

	private int[] nearbyVillageCentre(World world, int chunkX, int chunkZ) {
	    for (int i = chunkX - this.chunkRange; i <= chunkX + this.chunkRange; i++) {
	    	for (int k = chunkZ - this.chunkRange; k <= chunkZ + this.chunkRange; k++) {
	    		if (isVillageCentre(world, i, k)) {
	    			return new int[] { i, k };
	    		}
	    	}
	    }
	    return null;
	}

	public void generateInChunk(World world, Random random, int x, int z) {
	    int chunkX = x >> 4;
	    int chunkZ = z >> 4;
	    int[] centre = nearbyVillageCentre(world, chunkX, chunkZ);
	    
	    if (centre != null) {
	    	x += 8;
	    	z += 8;

	    	int centreX = (centre[0] << 4) + 8;
	    	int centreZ = (centre[1] << 4) + 8;

	    	for (int i1 = x; i1 <= x + 15; i1++) {
	    		for (int k1 = z; k1 <= z + 15; k1++) {
	    			for (StructureInfo struct : this.structures) {
	    				if ((struct.posX == i1 - centreX) && (struct.posZ == k1 - centreZ)) {
	    					int j1 = world.getTopSolidOrLiquidBlock(i1, k1);
	    					if (j1 > 62) {
	    						//struct.structure.generateWithSetRotation(world, random, i1, j1, k1, struct.rotation);
	    					}
	    				}
	    			}
	    		}
	    	}
	    }
	}

	private class StructureInfo {
		public WorldGenStructureBase structure;
	    public int posX;
	    public int posZ;
	    public int rotation;

	    public StructureInfo(WorldGenStructureBase structure, int x, int z, int rotate) { 
	    	this.structure = structure;
	    	this.posX = x;
	    	this.posZ = z;
	    	this.rotation = rotate;
	    }
	}
}