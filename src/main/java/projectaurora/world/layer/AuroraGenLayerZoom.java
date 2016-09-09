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
		int i2 = x >> 1;
        int k2 = z >> 1;
        int xSizeZoom = (xSize >> 1) + 2;
        int zSizeZoom = (zSize >> 1) + 2;
        int[] variants = this.baseParent.getInts(world, i2, k2, xSizeZoom, zSizeZoom);
        
        int i3 = xSizeZoom - 1 << 1;
        int k3 = zSizeZoom - 1 << 1;
        int[] ints = AuroraIntCache.get(world).getIntArray(i3 * k3);
        
        for (int k4 = 0; k4 < zSizeZoom - 1; ++k4) {
            for (int i4 = 0; i4 < xSizeZoom - 1; ++i4) {
                this.initChunkSeed((long)(i4 + i2 << 1), (long)(k4 + k2 << 1));
                int int00 = variants[i4 + 0 + (k4 + 0) * xSizeZoom];
                int int2 = variants[i4 + 0 + (k4 + 1) * xSizeZoom];
                int int3 = variants[i4 + 1 + (k4 + 0) * xSizeZoom];
                int int4 = variants[i4 + 1 + (k4 + 1) * xSizeZoom];
                int index = (i4 << 1) + (k4 << 1) * i3;
                ints[index] = int00;
                ints[index + 1] = this.selectRandom(new int[] { int00, int3 });
                ints[index + i3] = this.selectRandom(new int[] { int00, int2 });
                ints[index + i3 + 1] = this.selectModeOrRandom(int00, int3, int2, int4);
            }
        }
        
        int[] zoomedInts = AuroraIntCache.get(world).getIntArray(xSize * zSize);
        
        for (int k5 = 0; k5 < zSize; ++k5) {
            System.arraycopy(ints, (k5 + (z & 0x1)) * i3 + (x & 0x1), zoomedInts, k5 * xSize, xSize);
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