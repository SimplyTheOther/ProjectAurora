package projectaurora.world.layer;

import net.minecraft.world.World;
import projectaurora.world.AuroraIntCache;

public class AuroraGenLayerRiver extends BaseGenLayer {
    public static final int RANDOM_RIVER = 1;
    public static final int MAP_RIVER = 2;

	public AuroraGenLayerRiver(long seed, BaseGenLayer layer) {
		super(seed);
		this.baseParent = layer;
	}

	@Override
	public int[] getInts(World world, int x, int z, int xSize, int zSize) {
		int i2 = x - 1;
        int k2 = z - 1;
        int i3 = xSize + 2;
        int k3 = zSize + 2;
        int[] riverInit = this.baseParent.getInts(world, i2, k2, i3, k3);
        int[] ints = AuroraIntCache.get(world).getIntArray(xSize * zSize);
       
        for (int k4 = 0; k4 < zSize; ++k4) {
            for (int i4 = 0; i4 < xSize; ++i4) {
                int centre = riverInit[i4 + 1 + (k4 + 1) * i3];
                int xn = riverInit[i4 + 0 + (k4 + 1) * i3];
                int xp = riverInit[i4 + 2 + (k4 + 1) * i3];
                int zn = riverInit[i4 + 1 + (k4 + 0) * i3];
                int zp = riverInit[i4 + 1 + (k4 + 2) * i3];
                
                if (centre == xn && centre == zn && centre == xp && centre == zp) {
                    ints[i4 + k4 * xSize] = 0;
                } else {
                    ints[i4 + k4 * xSize] = 1;
                }
            }
        }
        return ints;
  	}
}