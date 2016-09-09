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
        int i2 = x >> 2;
        int k2 = z >> 2;
        int xSizeZoom = (xSize >> 2) + 2;
        int zSizeZoom = (zSize >> 2) + 2;
        int[] variants = this.baseParent.getInts(world, i2, k2, xSizeZoom, zSizeZoom);
        int i3 = xSizeZoom - 1 << 2;
        int k3 = zSizeZoom - 1 << 2;
        
        int[] ints = AuroraIntCache.get(world).getIntArray(i3 * k3);
        
        for (int k4 = 0; k4 < zSizeZoom - 1; ++k4) {
            int i4 = 0;
            int int00 = variants[i4 + 0 + (k4 + 0) * xSizeZoom];
            int int2 = variants[i4 + 0 + (k4 + 1) * xSizeZoom];
            
            while (i4 < xSizeZoom - 1) {
                double d0 = 3.6;
                
                this.initChunkSeed((long)(i4 + i2 << 2), (long)(k4 + k2 << 2));
                double d00_a = this.nextInt(this.zoomScale) / this.zoomDivisor * d0;
                double d00_b = this.nextInt(this.zoomScale) / this.zoomDivisor * d0;
                
                this.initChunkSeed((long)(i4 + i2 + 1 << 2), (long)(k4 + k2 << 2));
                double d10_a = this.nextInt(this.zoomScale) / this.zoomDivisor * d0 + 4.0;
                double d10_b = this.nextInt(this.zoomScale) / this.zoomDivisor * d0;
                
                this.initChunkSeed((long)(i4 + i2 << 2), (long)(k4 + k2 + 1 << 2));
                double d01_a = this.nextInt(this.zoomScale) / this.zoomDivisor * d0;
                double d01_b = this.nextInt(this.zoomScale) / this.zoomDivisor * d0 + 4.0;
                
                this.initChunkSeed((long)(i4 + i2 + 1 << 2), (long)(k4 + k2 + 1 << 2));
                double d11_a = this.nextInt(this.zoomScale) / this.zoomDivisor * d0 + 4.0;
                double d11_b = this.nextInt(this.zoomScale) / this.zoomDivisor * d0 + 4.0;
                
                int int3 = variants[i4 + 1 + (k4 + 0) * xSizeZoom];
                int int4 = variants[i4 + 1 + (k4 + 1) * xSizeZoom];
                
                for (int k5 = 0; k5 < 4; ++k5) {
                    int index = ((k4 << 2) + k5) * i3 + (i4 << 2);
                    
                    for (int i5 = 0; i5 < 4; ++i5) {
                        double d2 = (k5 - d00_b) * (k5 - d00_b) + (i5 - d00_a) * (i5 - d00_a);
                        double d3 = (k5 - d10_b) * (k5 - d10_b) + (i5 - d10_a) * (i5 - d10_a);
                        double d4 = (k5 - d01_b) * (k5 - d01_b) + (i5 - d01_a) * (i5 - d01_a);
                        double d5 = (k5 - d11_b) * (k5 - d11_b) + (i5 - d11_a) * (i5 - d11_a);
                        
                        if (d2 < d3 && d2 < d4 && d2 < d5) {
                            ints[index] = int00;
                            ++index;
                        } else if (d3 < d2 && d3 < d4 && d3 < d5) {
                            ints[index] = int3;
                            ++index;
                        } else if (d4 < d2 && d4 < d3 && d4 < d5) {
                            ints[index] = int2;
                            ++index;
                        } else {
                            ints[index] = int4;
                            ++index;
                        }
                    }
                }
                int00 = int3;
                int2 = int4;
                ++i4;
            }
        }
        int[] zoomedInts = AuroraIntCache.get(world).getIntArray(xSize * zSize);
        
        for (int k6 = 0; k6 < zSize; ++k6) {
            System.arraycopy(ints, (k6 + (z & 0x3)) * i3 + (x & 0x3), zoomedInts, k6 * xSize, xSize);
        }
        
        return zoomedInts;
	}
}