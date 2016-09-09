package projectaurora.world.layer;

import net.minecraft.world.World;
import projectaurora.world.AuroraIntCache;

@Deprecated
public class AuroraGenLayerBiomeSubTypesSmall extends BaseGenLayer {//TODO deprecated

	/*public AuroraGenLayerBiomeSubTypesSmall(long seed, BaseGenLayer layer) {
		super(seed);
		this.baseParent = layer;
	}*/
	public AuroraGenLayerBiomeSubTypesSmall(long seed) {
		super(seed);
	}

	@Override
	public int[] getInts(World world, int x, int z, int xSize, int zSize) {
		int[] biomes = this.baseParent.getInts(world, x - 1, z - 1, xSize + 2, zSize + 2);
	    int[] ints = AuroraIntCache.get(world).getIntArray(xSize * zSize);

	    for (int k1 = 0; k1 < zSize; k1++) {
	    	for (int i1 = 0; i1 < xSize; i1++) {
	    		initChunkSeed(x + i1, z + k1);
	    		int biome = biomes[(i1 + 1 + (k1 + 1) * (xSize + 2))];

	    		int newBiome = biome;

	    		/*if ((biome == AuroraBiome.ontarine.biomeID) && (nextInt(25) == 1)) {
	    			newBiome = AuroraBiome.ontarineGarden.biomeID;
	    		}*///TODO custom biome gen layer replacements
	    		
	    		if (newBiome != biome) {
	    			int j = biomes[(i1 + 1 + (k1 + 0) * (xSize + 2))];
	    			int j1 = biomes[(i1 + 2 + (k1 + 1) * (xSize + 2))];
	    			int j2 = biomes[(i1 + 0 + (k1 + 1) * (xSize + 2))];
	    			int j3 = biomes[(i1 + 1 + (k1 + 2) * (xSize + 2))];

	    			if ((j == biome) && (j1 == biome) && (j2 == biome) && (j3 == biome)) {
	    				ints[(i1 + k1 * xSize)] = newBiome;
	    			} else {
	    				ints[(i1 + k1 * xSize)] = biome;
	    			}
	    		} else {
	    			ints[(i1 + k1 * xSize)] = biome;
	    		}
	    	}
	    }
	    return ints;
	}
}