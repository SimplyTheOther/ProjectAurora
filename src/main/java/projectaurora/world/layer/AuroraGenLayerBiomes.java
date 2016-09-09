package projectaurora.world.layer;

import java.util.List;

import net.minecraft.world.World;
import projectaurora.world.AuroraIntCache;
import projectaurora.world.biome.AuroraBiome;

public class AuroraGenLayerBiomes extends BaseGenLayer {

	public AuroraGenLayerBiomes(long seed, BaseGenLayer layer) {
		super(seed);
		this.baseParent = layer;
	}

	@Override
	public int[] getInts(World world, int x, int z, int xSize, int zSize) {
		int[] oceans = this.baseParent.getInts(world, x, z, xSize, zSize);
	    int[] ints = AuroraIntCache.get(world).getIntArray(xSize * zSize);

	    for (int k1 = 0; k1 < zSize; k1++) {
	    	for (int i1 = 0; i1 < xSize; i1++) {
	    		initChunkSeed(x + i1, z + k1);

	    		int isOcean = oceans[(i1 + k1 * xSize)];
	    		int biomeID;
	       
	        	if (isOcean == 1) {
	        		biomeID = AuroraBiome.lavaOcean.biomeID;//TODO change to ocean function
	        	} else {
	        		List biomeList = AuroraBiome.getBiomesForCurrentDimension(world);
	        		int randIndex = nextInt(biomeList.size());
	        		AuroraBiome biome = (AuroraBiome)biomeList.get(randIndex);
	        		biomeID = biome.biomeID;
	        	}

	        	ints[(i1 + k1 * xSize)] = biomeID;
	    	}
	    }
	    return ints;
	}
}