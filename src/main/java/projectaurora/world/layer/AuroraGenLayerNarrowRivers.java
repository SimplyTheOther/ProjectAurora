package projectaurora.world.layer;

import net.minecraft.world.World;
import projectaurora.world.AuroraIntCache;

public class AuroraGenLayerNarrowRivers extends BaseGenLayer {
	private final int maxRange;

	public AuroraGenLayerNarrowRivers(long seed, BaseGenLayer layer, int range) {
		super(seed);
		this.baseParent = layer;
		this.maxRange = range;
	}

	@Override
	public int[] getInts(World world, int x, int z, int xSize, int zSize) {
        int[] rivers = this.baseParent.getInts(world, x - this.maxRange, z - this.maxRange, xSize + this.maxRange * 2, zSize + this.maxRange * 2);
        int[] ints = AuroraIntCache.get(world).getIntArray(xSize * zSize);
        
        for (int k2 = 0; k2 < zSize; ++k2) {
            for (int i2 = 0; i2 < xSize; ++i2) {
                this.initChunkSeed((long)(x + i2), (long)(z + k2));
                int isRiver = rivers[i2 + this.maxRange + (k2 + this.maxRange) * (xSize + this.maxRange * 2)];
                
                Label_0254: {
                    if (isRiver > 0) {
                        for (int range = 1; range <= this.maxRange; ++range) {
                            for (int k3 = k2 - range; k3 <= k2 + range; ++k3) {
                                for (int i3 = i2 - range; i3 <= i2 + range; ++i3) {
                                    if (Math.abs(i3 - i2) == range || Math.abs(k3 - k2) == range) {
                                        int subRiver = rivers[i3 + this.maxRange + (k3 + this.maxRange) * (xSize + this.maxRange * 2)];
                                        
                                        if (subRiver == 0) {
                                            isRiver = 0;
                                            break Label_0254;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                
                ints[i2 + k2 * xSize] = isRiver;
            }
        }
        return ints;
	}
}