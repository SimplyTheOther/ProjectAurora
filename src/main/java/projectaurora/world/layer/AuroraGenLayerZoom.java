package projectaurora.world.layer;

import net.minecraft.world.World;
import projectaurora.world.AuroraIntCache;

public class AuroraGenLayerZoom extends BaseGenLayer {

	public AuroraGenLayerZoom(long seed, BaseGenLayer layer) {
		super(seed);
		this.baseParent = layer;
	}

	@Override
	public int[] getInts(World world, int x, int z, int xSize, int zSize) {
		int i1 = x >> 1;
	    int k1 = z >> 1;
	    int xSizeZoom = (xSize >> 1) + 2;
	    int zSizeZoom = (zSize >> 1) + 2;
	    int[] variants = this.baseParent.getInts(world, i1, k1, xSizeZoom, zSizeZoom);

	    int i2 = xSizeZoom - 1 << 1;
	    int k2 = zSizeZoom - 1 << 1;
	    int[] ints = AuroraIntCache.get(world).getIntArray(i2 * k2);

	    for (int k3 = 0; k3 < zSizeZoom - 1; k3++) {
	    	int index = (k3 << 1) * i2;
	    	int i3 = 0;
	    	int int00 = variants[(i3 + 0 + (k3 + 0) * xSizeZoom)];

	    	for (int int01 = variants[(i3 + 0 + (k3 + 1) * xSizeZoom)]; i3 < xSizeZoom - 1; i3++) {
	    		initChunkSeed(i3 + i1 << 1, k3 + k1 << 1);
	    		int int10 = variants[(i3 + 1 + (k3 + 0) * xSizeZoom)];
	    		int int11 = variants[(i3 + 1 + (k3 + 1) * xSizeZoom)];
	    		ints[index] = int00;
	    		ints[(index + i2)] = selectRandom(new int[] { int00, int01 });
	    		index++;
	    		ints[index] = selectRandom(new int[] { int00, int10 });
	    		ints[(index + i2)] = selectModeOrRandom(int00, int10, int01, int11);
	    		index++;
	        	int00 = int10;
	        	int01 = int11;
	    	}
	    }

	    int[] zoomedInts = AuroraIntCache.get(world).getIntArray(xSize * zSize);
	    
	    for (int k3 = 0; k3 < zSize; k3++) {
	    	System.arraycopy(ints, (k3 + (z & 0x1)) * i2 + (x & 0x1), zoomedInts, k3 * xSize, xSize);
	    }
	    
	    return zoomedInts;
    }

	public static BaseGenLayer magnify(long seed, BaseGenLayer source, int zooms) {
	    BaseGenLayer layer = source;
	    
	    for (int i = 0; i < zooms; i++) {
	    	layer = new AuroraGenLayerZoom(seed + i, layer);
	    }
	    return layer;
	}
}