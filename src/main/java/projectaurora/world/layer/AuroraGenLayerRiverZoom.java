package projectaurora.world.layer;

import net.minecraft.world.World;
import projectaurora.world.AuroraIntCache;

public class AuroraGenLayerRiverZoom extends BaseGenLayer {

	public AuroraGenLayerRiverZoom(long seed, BaseGenLayer layer) {
		super(seed);
		this.baseParent = layer;
	}

	@Override
	public int[] getInts(World world, int x, int z, int xSize, int zSize) {
		int i1 = (x >> 1) - 1;
	    int k1 = (z >> 1) - 1;
	    int xSizeZoom = (xSize >> 1) + 4;
	    int zSizeZoom = (zSize >> 1) + 4;
	    int[] rivers = this.baseParent.getInts(world, i1, k1, xSizeZoom, zSizeZoom);

	    int i2 = xSizeZoom - 3 << 1;
	    int k2 = zSizeZoom - 3 << 1;
	    int[] ints = AuroraIntCache.get(world).getIntArray(i2 * k2);

	    for (int k3 = 0; k3 < zSizeZoom - 3; k3++) {
	    	for (int i3 = 0; i3 < xSizeZoom - 3; i3++) {
	    		initChunkSeed(i3 + (i1 + 1) << 1, k3 + (k1 + 1) << 1);

	    		for (int i4 = 0; i4 <= 1; i4++) {
	    			for (int k4 = 0; k4 <= 1; k4++) {
	    				int int00 = rivers[(i3 + 1 + (k3 + 1) * xSizeZoom)];
	    				int opp = int00 == 0 ? 2 : 0;

	    				boolean replaceCorner = false;
	            	
	    				if ((i4 == 0) && (k4 == 0)) {
	    					replaceCorner = (rivers[(i3 + 0 + (k3 + 0) * xSizeZoom)] == opp) && (rivers[(i3 + 1 + (k3 + 0) * xSizeZoom)] == opp) && (rivers[(i3 + 0 + (k3 + 1) * xSizeZoom)] == opp);
	    				}
	            
	    				if ((i4 == 1) && (k4 == 0)) {
	    					replaceCorner = (rivers[(i3 + 1 + (k3 + 0) * xSizeZoom)] == opp) && (rivers[(i3 + 2 + (k3 + 0) * xSizeZoom)] == opp) && (rivers[(i3 + 2 + (k3 + 1) * xSizeZoom)] == opp);
	    				}
	           
	    				if ((i4 == 0) && (k4 == 1)) {
	    					replaceCorner = (rivers[(i3 + 0 + (k3 + 1) * xSizeZoom)] == opp) && (rivers[(i3 + 0 + (k3 + 2) * xSizeZoom)] == opp) && (rivers[(i3 + 1 + (k3 + 2) * xSizeZoom)] == opp);
	    				}
	            
	    				if ((i4 == 1) && (k4 == 1)) {
	    					replaceCorner = (rivers[(i3 + 2 + (k3 + 1) * xSizeZoom)] == opp) && (rivers[(i3 + 1 + (k3 + 2) * xSizeZoom)] == opp) && (rivers[(i3 + 2 + (k3 + 2) * xSizeZoom)] == opp);
	    				}

	    				if (replaceCorner) {
	    					int00 = opp;
	    				}

	    				int index = (i3 << 1) + i4 + ((k3 << 1) + k4) * i2;
	    				ints[index] = int00;
	    			}
	    		}
	    	}
	    }

	    int[] zoomedInts = AuroraIntCache.get(world).getIntArray(xSize * zSize);
	    
	    for (int k3 = 0; k3 < zSize; k3++) {
	      System.arraycopy(ints, (k3 + (z & 0x1)) * i2 + (x & 0x1), zoomedInts, k3 * xSize, xSize);
	    }
	    return zoomedInts;
	}
}