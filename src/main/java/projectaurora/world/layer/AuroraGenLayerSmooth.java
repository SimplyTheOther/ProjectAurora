package projectaurora.world.layer;

import net.minecraft.world.World;
import projectaurora.world.AuroraIntCache;

public class AuroraGenLayerSmooth extends BaseGenLayer {

	public AuroraGenLayerSmooth(long seed, BaseGenLayer layer) {
		super(seed);
		this.baseParent = layer;
	}

	@Override
	public int[] getInts(World world, int x, int z, int xSize, int zSize) {
	    int i1 = x - 1;
	    int k1 = z - 1;
	    int xSizeP = xSize + 2;
	    int zSizeP = zSize + 2;
	    int[] biomes = this.baseParent.getInts(world, i1, k1, xSizeP, zSizeP);
	    int[] ints = AuroraIntCache.get(world).getIntArray(xSize * zSize);

	    for (int k2 = 0; k2 < zSize; k2++) {
	    	for (int i2 = 0; i2 < xSize; i2++) {
	    		int centre = biomes[(i2 + 1 + (k2 + 1) * xSizeP)];
	    		int xn = biomes[(i2 + 0 + (k2 + 1) * xSizeP)];
	    		int xp = biomes[(i2 + 2 + (k2 + 1) * xSizeP)];
	    		int zn = biomes[(i2 + 1 + (k2 + 0) * xSizeP)];
	    		int zp = biomes[(i2 + 1 + (k2 + 2) * xSizeP)];

	    		if ((xn == xp) && (zn == zp)) {
	    			initChunkSeed(i2 + x, k2 + z);

	    			if (nextInt(2) == 0) {
	    				centre = xn;
	    			} else {
	    				centre = zn;
	    			}
    			} else {
    				if (xn == xp) {
	    				centre = xn;
	    			}

	    			if (zn == zp) {
	    				centre = zn;
	    			}
	    		}
	    		ints[(i2 + k2 * xSize)] = centre;
	    	}
	    }
	    return ints;
	}
}