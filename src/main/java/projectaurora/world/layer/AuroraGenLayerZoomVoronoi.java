package projectaurora.world.layer;

import net.minecraft.world.World;
import projectaurora.world.AuroraIntCache;

public class AuroraGenLayerZoomVoronoi extends BaseGenLayer {
	private int zoomScale = 1024;
	private double zoomDivisor = this.zoomScale - 0.5D;

	public AuroraGenLayerZoomVoronoi(long seed, BaseGenLayer layer) {
		super(seed);
		this.baseParent = layer;
	}

	@Override
	public int[] getInts(World world, int x, int z, int xSize, int zSize) {
		x -= 2;
	    z -= 2;
	    int i1 = x >> 2;
	    int k1 = z >> 2;
	    int xSizeZoom = (xSize >> 2) + 2;
	    int zSizeZoom = (zSize >> 2) + 2;
	    int[] variants = this.baseParent.getInts(world, i1, k1, xSizeZoom, zSizeZoom);
	    int i2 = xSizeZoom - 1 << 2;
	    int k2 = zSizeZoom - 1 << 2;

	    int[] ints = AuroraIntCache.get(world).getIntArray(i2 * k2);

	    for (int k3 = 0; k3 < zSizeZoom - 1; k3++) {
	    	int i3 = 0;
	    	int int00 = variants[(i3 + 0 + (k3 + 0) * xSizeZoom)];

	    	for (int int01 = variants[(i3 + 0 + (k3 + 1) * xSizeZoom)]; i3 < xSizeZoom - 1; i3++) {
	    		double d0 = 3.6D;

	    		initChunkSeed(i3 + i1 << 2, k3 + k1 << 2);
	    		double d00_a = nextInt(this.zoomScale) / this.zoomDivisor * d0;
	        	double d00_b = nextInt(this.zoomScale) / this.zoomDivisor * d0;

	        	initChunkSeed(i3 + i1 + 1 << 2, k3 + k1 << 2);
	        	double d10_a = nextInt(this.zoomScale) / this.zoomDivisor * d0 + 4.0D;
	        	double d10_b = nextInt(this.zoomScale) / this.zoomDivisor * d0;

	        	initChunkSeed(i3 + i1 << 2, k3 + k1 + 1 << 2);
	        	double d01_a = nextInt(this.zoomScale) / this.zoomDivisor * d0;
	        	double d01_b = nextInt(this.zoomScale) / this.zoomDivisor * d0 + 4.0D;

	        	initChunkSeed(i3 + i1 + 1 << 2, k3 + k1 + 1 << 2);
	        	double d11_a = nextInt(this.zoomScale) / this.zoomDivisor * d0 + 4.0D;
	        	double d11_b = nextInt(this.zoomScale) / this.zoomDivisor * d0 + 4.0D;

	        	int int10 = variants[(i3 + 1 + (k3 + 0) * xSizeZoom)];
	        	int int11 = variants[(i3 + 1 + (k3 + 1) * xSizeZoom)];

	        	for (int k4 = 0; k4 < 4; k4++) {
	        		int index = ((k3 << 2) + k4) * i2 + (i3 << 2);

	        		for (int i4 = 0; i4 < 4; i4++) {
	        			double d00 = (k4 - d00_b) * (k4 - d00_b) + (i4 - d00_a) * (i4 - d00_a);
	        			double d10 = (k4 - d10_b) * (k4 - d10_b) + (i4 - d10_a) * (i4 - d10_a);
	        			double d01 = (k4 - d01_b) * (k4 - d01_b) + (i4 - d01_a) * (i4 - d01_a);
	        			double d11 = (k4 - d11_b) * (k4 - d11_b) + (i4 - d11_a) * (i4 - d11_a);

	        			if ((d00 < d10) && (d00 < d01) && (d00 < d11)) {
	        				ints[index] = int00;
	        				index++;
	        			} else if ((d10 < d00) && (d10 < d01) && (d10 < d11)) {
	        				ints[index] = int10;
	        				index++;
	        			} else if ((d01 < d00) && (d01 < d10) && (d01 < d11)) {
	        				ints[index] = int01;
	        				index++;
	        			} else {
	        				ints[index] = int11;
	        				index++;
	        			}
	        		}
	        	}
	        	int00 = int10;
	        	int01 = int11;
	    	}
	    }

	    int[] zoomedInts = AuroraIntCache.get(world).getIntArray(xSize * zSize);

	    for (int k3 = 0; k3 < zSize; k3++) {
	    	System.arraycopy(ints, (k3 + (z & 0x3)) * i2 + (x & 0x3), zoomedInts, k3 * xSize, xSize);
	    }
	    
	    return zoomedInts;
	}
}