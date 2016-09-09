package projectaurora.world.layer;

import net.minecraft.world.World;
import projectaurora.world.AuroraIntCache;
import projectaurora.world.biome.AuroraBiome;

public class AuroraGenLayerExtractRivers extends BaseGenLayer {

	public AuroraGenLayerExtractRivers(long seed, BaseGenLayer layer) {
		super(seed);
		this.baseParent = layer;
	}

	@Override
	public int[] getInts(World world, int x, int z, int xSize, int zSize) {
	    int[] biomes = this.baseParent.getInts(world, x, z, xSize, zSize);
	    int[] ints = AuroraIntCache.get(world).getIntArray(xSize * zSize);

	    for (int k1 = 0; k1 < zSize; k1++) {
	    	for (int i1 = 0; i1 < xSize; i1++) {
	    		initChunkSeed(x + i1, z + k1);

	    		int biomeID = biomes[(i1 + k1 * xSize)];
	        
	    		if (biomeID == AuroraBiome.lavaRiver.biomeID) {//TODO change to generic river
	    			ints[(i1 + k1 * xSize)] = 2;
	    		} else {
	    			ints[(i1 + k1 * xSize)] = 0;
	    		}
	    	}
	    }
	    return ints;
	}
}