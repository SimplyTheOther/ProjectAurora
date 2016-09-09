package projectaurora.world.layer;

import net.minecraft.world.World;
import projectaurora.world.AuroraIntCache;

public class AuroraGenLayerRemoveOcean extends BaseGenLayer {

	public AuroraGenLayerRemoveOcean(long seed, BaseGenLayer layer) {
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

	    		if ((Math.abs(x + i1) <= 1) && (Math.abs(z + k1) <= 1)) {
	    			isOcean = 0;
	    		}

	    		ints[(i1 + k1 * xSize)] = isOcean;
	    	}
	    }
	    return ints;
	}
}