package projectaurora.world.layer;

import net.minecraft.world.World;
import projectaurora.world.AuroraIntCache;

public class AuroraGenLayerRiver extends BaseGenLayer {

	public AuroraGenLayerRiver(long seed, BaseGenLayer layer) {
		super(seed);
		this.baseParent = layer;
	}

	@Override
	public int[] getInts(World world, int x, int z, int xSize, int zSize) {
	    int i1 = x - 1;
	    int k1 = z - 1;
	    int i2 = xSize + 2;
	    int k2 = zSize + 2;
	    int[] riverInit = this.baseParent.getInts(world, i1, k1, i2, k2);
	    int[] ints = AuroraIntCache.get(world).getIntArray(xSize * zSize);

	    for (int k3 = 0; k3 < zSize; k3++) {
	    	for (int i3 = 0; i3 < xSize; i3++) {
	    		int centre = riverInit[(i3 + 1 + (k3 + 1) * i2)];
	    		int xn = riverInit[(i3 + 0 + (k3 + 1) * i2)];
	    		int xp = riverInit[(i3 + 2 + (k3 + 1) * i2)];
	        	int zn = riverInit[(i3 + 1 + (k3 + 0) * i2)];
	        	int zp = riverInit[(i3 + 1 + (k3 + 2) * i2)];

	        	if ((centre == xn) && (centre == zn) && (centre == xp) && (centre == zp)) {
	        		ints[(i3 + k3 * xSize)] = -1;
	        	} else {
	        		ints[(i3 + k3 * xSize)] = 0;
	        	}
	    	}
	    }
	    return ints;
  	}
}